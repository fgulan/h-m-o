package algorithms.genetic;

import algorithms.genetic.crossover.ICrossoverOperator;
import algorithms.genetic.mutation.IMutationOperator;
import algorithms.solutions.InstanceSolution;
import algorithms.interfaces.IAlgorithm;
import utils.Pair;
import utils.RandUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class GenerationalGeneticAlgorithm {
    private List<InstanceSolution> population;
    private final IMutationOperator mutation;
    private final ICrossoverOperator crossover;
    private final int maxGeneration;
    private final RandUtils random = RandUtils.getInstance();
    private final int elitism;

    public GenerationalGeneticAlgorithm(List<InstanceSolution> population, IMutationOperator mutation, ICrossoverOperator crossover, int maxGeneration, int elitism) {
        this.population = population;
        this.mutation = mutation;
        this.crossover = crossover;
        this.maxGeneration = maxGeneration;
        this.elitism = elitism;
    }

    public InstanceSolution run() {
        int currentGeneration = 0;
        Collections.sort(population, Comparator.comparingInt(InstanceSolution::calculateTotalDuration));
        InstanceSolution bestSolution = population.get(0);
        System.out.println("Najbolja: " + bestSolution.calculateTotalDuration() + ", generacija: " + currentGeneration);

        while (currentGeneration < maxGeneration) {
            currentGeneration += 1;

            List<InstanceSolution> newPopulation = new ArrayList<>();
            for (int i = 0; i < elitism; i++) {
                newPopulation.add(population.get(0));
            }

            while (newPopulation.size() != population.size()) {
                List<Pair<Integer, InstanceSolution>> pairs = random.generateIndexes(2, population.size())
                        .map(index -> new Pair<>(index, population.get(index)))
                        .collect(Collectors.toList());

                Collections.sort(pairs, Comparator.comparingInt(o -> o.getSecond().calculateTotalDuration()));

                InstanceSolution parent0 = pairs.get(0).getSecond();
                InstanceSolution parent1 = pairs.get(1).getSecond();

                // Crossover
                InstanceSolution child = crossover.crossover(parent0, parent1);
                // Mutation
                child = mutation.mutate(child);
                newPopulation.add(child);
            }
            population = newPopulation;
            Collections.sort(population, Comparator.comparingInt(InstanceSolution::calculateTotalDuration));

            if (population.get(0).calculateTotalDuration() < bestSolution.calculateTotalDuration()) {
                bestSolution = population.get(0);
                System.out.println("Nova najbolja: " + bestSolution.calculateTotalDuration() + ", generacija: " + currentGeneration);
            }
        }
        System.out.println("KRAJ! Najbolja: " + bestSolution.calculateTotalDuration());
        return bestSolution;
    }
}
