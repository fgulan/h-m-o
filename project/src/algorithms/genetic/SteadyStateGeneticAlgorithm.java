package algorithms.genetic;

import algorithms.genetic.crossover.ICrossoverOperator;
import algorithms.genetic.mutation.IMutationOperator;
import algorithms.greedy.Solution;
import utils.Pair;
import utils.RandUtils;

import java.util.*;
import java.util.stream.Collectors;

public class SteadyStateGeneticAlgorithm {

    private List<Solution> population;
    private IMutationOperator mutation;
    private ICrossoverOperator crossover;
    private int maxGeneration;
    private RandUtils random = RandUtils.getInstance();

    public SteadyStateGeneticAlgorithm(List<Solution> population, IMutationOperator mutation, ICrossoverOperator crossover, int maxGeneration) {
        this.population = population;
        this.mutation = mutation;
        this.crossover = crossover;
        this.maxGeneration = maxGeneration;
    }

    public Solution run() {
        int currentGeneration = 0;
        Collections.sort(population, Comparator.comparingInt(Solution::totalDuration));
        Solution bestSolution = population.get(0);
        System.out.println("Najbolja: " + bestSolution.totalDuration() + ", generacija: " + currentGeneration);

        while (currentGeneration < maxGeneration) {
            currentGeneration += 1;

            List<Pair<Integer, Solution>> pairs = random.generateIndexes(5, population.size())
                    .map(index -> new Pair<>(index, population.get(index)))
                    .collect(Collectors.toList());

            Collections.sort(pairs, Comparator.comparingInt(o -> o.getSecond().totalDuration()));

            Solution parent0 = pairs.get(0).getSecond();
            Solution parent1 = pairs.get(1).getSecond();
            Solution last = pairs.get(4).getSecond();

            // Crossover
            Solution child = crossover.crossover(parent0, parent1);
            // Mutation
            child = mutation.mutate(child);

            if (child.totalDuration() < last.totalDuration()) {
                population.set(pairs.get(4).getFirst(), child);
            }
            Collections.sort(population, Comparator.comparingInt(Solution::totalDuration));

            if (population.get(0).totalDuration() < bestSolution.totalDuration()) {
                bestSolution = population.get(0);
                System.out.println("Nova najbolja: " + bestSolution.totalDuration() + ", generacija: " + currentGeneration);
            }
        }
        System.out.println("KRAJ! Najbolja: " + bestSolution.totalDuration());
        return bestSolution;
    }
}
