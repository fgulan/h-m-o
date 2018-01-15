import algorithms.genetic.SteadyStateGeneticAlgorithm;
import algorithms.genetic.crossover.ICrossoverOperator;
import algorithms.genetic.crossover.NPointPermutationMutationCrossover;
import algorithms.genetic.crossover.PermutationMutationCrossover;
import algorithms.genetic.mutation.NoMutationOperator;
import algorithms.greedy.Solution;
import algorithms.greedy.SupremeSolutionGenerator;
import models.Instance;
import parser.InstanceParser;
import utils.Visualization;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        Instance instance = InstanceParser.parseInstanceFile("/Users/filipgulan/college/h-m-o/instances/ts10.txt");
        SupremeSolutionGenerator sGen = new SupremeSolutionGenerator(instance);

        List<Solution> population = new ArrayList<>();
        for (int i = 0, size = 100; i < size; i++) {
            Solution solution = sGen.generate();
            population.add(solution);
        }
        ICrossoverOperator crossover = new PermutationMutationCrossover(0.8f, 0.25f);
        ICrossoverOperator pointCrossover = new NPointPermutationMutationCrossover(1, 0.05f);
        SteadyStateGeneticAlgorithm ga = new SteadyStateGeneticAlgorithm(population, new NoMutationOperator(), pointCrossover, 1000000);
        Solution bestSolution = ga.run();
        System.out.println(bestSolution.totalDuration());

        try (PrintWriter out = new PrintWriter("/Users/filipgulan/filename10.txt")) {
            out.println(bestSolution.printIt());
        }

        try (PrintWriter out1 = new PrintWriter("/Users/filipgulan/sol.html")) {
            out1.println(Visualization.convertSolutionToHTML(bestSolution, instance));
        }
    }
}
