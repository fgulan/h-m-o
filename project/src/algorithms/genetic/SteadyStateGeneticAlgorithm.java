package algorithms.genetic;

import algorithms.genetic.crossover.ICrossoverOperator;
import algorithms.genetic.mutation.IMutationOperator;
import algorithms.interfaces.IEvaluator;
import algorithms.solutions.InstanceSolution;
import algorithms.interfaces.IAlgorithm;
import models.AlgorithmResult;
import utils.Pair;
import utils.RandUtils;

import java.util.*;
import java.util.stream.Collectors;

public class SteadyStateGeneticAlgorithm implements IAlgorithm {

    private static final int MINUTES_1_MILIS = 1 * 60 * 1_000;
    private static final int MINUTES_5_MILIS = 5 * 60 * 1_000;
    private static final int MINUTES_20_MILIS = 20 * 60 * 1_000;

    private List<InstanceSolution> population;
    private final IMutationOperator mutation;
    private final ICrossoverOperator crossover;
    private final IEvaluator evaluator;
    private final long maxGeneration;
    private final long maxWithoutChange;
    private final int tournament;
    private final List<AlgorithmResult<InstanceSolution>> results;
    private final RandUtils random = RandUtils.getInstance();

    public SteadyStateGeneticAlgorithm(List<InstanceSolution> population, IMutationOperator mutation, ICrossoverOperator crossover, IEvaluator evaluator,
                                       long maxGeneration, long maxWithoutChange, int tournament) {
        this.population = population;
        this.mutation = mutation;
        this.crossover = crossover;
        this.evaluator = evaluator;
        this.maxGeneration = maxGeneration;
        this.maxWithoutChange = maxWithoutChange;
        this.tournament = tournament;
        this.results = new ArrayList<>();
    }

    public InstanceSolution run() {
        long currentGeneration = 0;
        long currentWithoutChange = 0;
        results.clear();

        long startTime = System.currentTimeMillis();

        Collections.sort(population, Comparator.comparingInt(this::evaluate));

        InstanceSolution bestSolution = population.get(0);

        while (currentGeneration < maxGeneration && currentWithoutChange < maxWithoutChange) {
            currentGeneration += 1;
            currentWithoutChange += 1;

            List<Pair<Integer, InstanceSolution>> pairs =
                    random.generateIndexes(tournament, population.size())
                    .map(index -> new Pair<>(index, population.get(index)))
                    .collect(Collectors.toList());

            Collections.sort(pairs, Comparator.comparingInt(o -> evaluate(o.getSecond())));

            InstanceSolution parent0 = pairs.get(0).getSecond();
            InstanceSolution parent1 = pairs.get(1).getSecond();
            InstanceSolution last = pairs.get(tournament - 1).getSecond();

            // Crossover
            InstanceSolution child = crossover.crossover(parent0, parent1);
            // Mutation
            child = mutation.mutate(child);

            if (evaluate(child) < evaluate(last)) {
                population.set(pairs.get(tournament - 1).getFirst(), child);
            }

            Collections.sort(population, Comparator.comparingInt(this::evaluate));

            if (evaluate(population.get(0)) < evaluate(bestSolution)) {
                bestSolution = population.get(0);
                currentWithoutChange = 0;
                System.out.println("Nova najbolja: " + evaluate(bestSolution) + ", generacija: " + currentGeneration);
            }

            if (processTime(startTime, bestSolution)) {
                break;
            }
        }
        if (results.size() <= 2) {
            AlgorithmResult<InstanceSolution> result =
                    new AlgorithmResult<>(
                            bestSolution,
                            evaluator.getTotalEvaluationCount(),
                            evaluate(bestSolution),
                            System.currentTimeMillis());
            results.add(result);
        }
        return bestSolution;
    }

    public List<AlgorithmResult<InstanceSolution>> getResults() {
        return Collections.unmodifiableList(results);
    }

    private boolean processTime(long startTime, InstanceSolution bestSolution) {
        long currentTime = System.currentTimeMillis();
        boolean shouldBreak = false;
        if (isPassed(startTime, currentTime, MINUTES_20_MILIS) && results.size() == 2) {
            AlgorithmResult<InstanceSolution> result =
                    new AlgorithmResult<>(
                            bestSolution,
                            evaluator.getTotalEvaluationCount(),
                            evaluate(bestSolution),
                            MINUTES_20_MILIS);
            results.add(result);
            shouldBreak = true;
        } else if (isPassed(startTime, currentTime, MINUTES_5_MILIS) && results.size() == 1) {
            AlgorithmResult<InstanceSolution> result =
                    new AlgorithmResult<>(
                            bestSolution,
                            evaluator.getTotalEvaluationCount(),
                            evaluate(bestSolution),
                            MINUTES_5_MILIS);
            results.add(result);
        } else if (isPassed(startTime, currentTime, MINUTES_1_MILIS) && results.size() == 0) {
            AlgorithmResult<InstanceSolution> result =
                    new AlgorithmResult<>(
                            bestSolution,
                            evaluator.getTotalEvaluationCount(),
                            evaluate(bestSolution),
                            MINUTES_1_MILIS);
            results.add(result);
        }
        return shouldBreak;
    }

    private int evaluate(InstanceSolution solution) {
        return evaluator.evaluate(solution);
    }

    private boolean isPassed(long startTime, long currentTime, long miliseconds) {
        return currentTime - startTime >= miliseconds;
    }
}
