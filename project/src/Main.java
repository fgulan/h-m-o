import algorithms.evaluator.InstanceEvaluator;
import algorithms.genetic.SteadyStateGeneticAlgorithm;
import algorithms.genetic.crossover.ICrossoverOperator;
import algorithms.genetic.crossover.NPointPermutationMutationCrossover;
import algorithms.genetic.mutation.IMutationOperator;
import algorithms.genetic.mutation.NoMutationOperator;
import algorithms.interfaces.IAlgorithm;
import algorithms.interfaces.IEvaluator;
import algorithms.solutions.InstanceSolution;
import algorithms.greedy.SupremeSolutionGenerator;
import models.AlgorithmResult;
import models.Instance;
import parser.InstanceParser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final long MAX_GENERATIONS = Long.MAX_VALUE;
    private static final long MAX_WITHOUT_CHANGE = Long.MAX_VALUE;
    private static final float MUTATION = 0.15f;
    private static final int N_POINT = 2;
    private static final int TOURNAMENT_SIZE = 3;

    public static void main(String[] args) throws IOException {
        String inputPathString = "C:\\Users\\gulan_filip\\Documents\\h-m-o\\instances\\ts10.txt";
        String filename = Paths.get(inputPathString).getFileName().toString();

        System.out.println("Working on file: " + filename);
        Instance instance = InstanceParser.parseInstanceFile(inputPathString);

        SupremeSolutionGenerator solutionGenerator = new SupremeSolutionGenerator(instance);
        List<InstanceSolution> population = new ArrayList<>();
        for (int i = 0, size = 100; i < size; i++) {
            InstanceSolution solution = solutionGenerator.generate();
            population.add(solution);
        }

        ICrossoverOperator pointCrossover = new NPointPermutationMutationCrossover(N_POINT, MUTATION);
        IMutationOperator mutation = new NoMutationOperator();
        IEvaluator evaluator = new InstanceEvaluator();

        IAlgorithm algorithm = new SteadyStateGeneticAlgorithm(population, mutation, pointCrossover, evaluator,
                MAX_GENERATIONS, MAX_WITHOUT_CHANGE, TOURNAMENT_SIZE);
        algorithm.run();

        printResults(algorithm.getResults(), filename);
    }

    private static void printResults(List<AlgorithmResult<InstanceSolution>> results, String filename) throws FileNotFoundException {
        AlgorithmResult<InstanceSolution> first = results.get(0);
        AlgorithmResult<InstanceSolution> second = results.get(1);
        AlgorithmResult<InstanceSolution> third = results.get(2);

        // Print first
        String outputPath = "res-1m-" + filename;
        try (PrintWriter out = new PrintWriter(outputPath)) {
            out.println(first.getSolution().printIt());
        }

        // Print second
        outputPath = "res-5m-" + filename;
        try (PrintWriter out = new PrintWriter(outputPath)) {
            out.println(second.getSolution().printIt());
        }

        // Print third
        outputPath = "res-ne-" + filename;
        try (PrintWriter out = new PrintWriter(outputPath)) {
            out.println(third.getSolution().printIt());
        }

        // Print statistics
        outputPath = "res-stats-" + filename;
        try (PrintWriter out = new PrintWriter(outputPath)) {
            out.println(results);
        }
    }
}
