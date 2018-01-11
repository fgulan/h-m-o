package algorithms.genetic;

import algorithms.genetic.crossover.ICrossoverOperator;
import algorithms.genetic.mutation.IMutationOperator;
import algorithms.greedy.Solution;
import utils.Pair;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SteadyStateGeneticAlgorithm {

    private List<Solution> population;
    private IMutationOperator mutation;
    private ICrossoverOperator crossover;
    private int maxGeneration;
    private Random random;

    public SteadyStateGeneticAlgorithm(List<Solution> population, IMutationOperator mutation, ICrossoverOperator crossover, int maxGeneration) {
        this.population = population;
        this.mutation = mutation;
        this.crossover = crossover;
        this.maxGeneration = maxGeneration;
        this.random = new Random();
    }

    public Solution run() {
        int currentGeneration = 0;
        Collections.sort(population, new Comparator<>() {
            @Override
            public int compare(Solution o1, Solution o2) {
                return Integer.compare(o1.totalDuration(),
                        o2.totalDuration());
            }
        });
        Solution bestSolution = population.get(0);
        System.out.println("Nova najbolja: " + bestSolution.totalDuration() + ", generacija: " + currentGeneration);
        while (currentGeneration < maxGeneration) {
            currentGeneration += 1;

            List<Pair<Integer, Solution>> pairs = generateIndexes(3, population.size())
                    .map(index -> new Pair<Integer, Solution>(index, population.get(index)))
                    .collect(Collectors.toList());

            Collections.sort(pairs, new Comparator<>() {
                @Override
                public int compare(Pair<Integer, Solution> o1, Pair<Integer, Solution> o2) {
                    return Integer.compare(o1.getSecond().totalDuration(),
                            o2.getSecond().totalDuration());
                }
            });
            Solution parent0 = pairs.get(0).getSecond();
            Solution parent1 = pairs.get(1).getSecond();
            Solution thrid = pairs.get(2).getSecond();

            Solution child = crossover.crossover(parent0, parent1);
            child = mutation.mutate(child).makeItFeasible().compressClearedTasks();
            if (child.totalDuration() < thrid.totalDuration()) {
                population.set(pairs.get(2).getFirst(), child);
            }

            Collections.sort(population, new Comparator<>() {
                @Override
                public int compare(Solution o1, Solution o2) {
                    return Integer.compare(o1.totalDuration(),
                            o2.totalDuration());
                }
            });
            if (population.get(0) != bestSolution) {
                System.out.println("Nova najbolja: " + bestSolution.totalDuration() + ", generacija: " + currentGeneration);
            }
            bestSolution = population.get(0);
        }
        return bestSolution;
    }

    private Stream<Integer> generateIndexes(int count, int bound) {
        Set<Integer> intSet = new HashSet<>();
        while (intSet.size() < count) {
            intSet.add(random.nextInt(bound));
        }
        return intSet.stream();
    }
}
