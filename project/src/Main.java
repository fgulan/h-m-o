import algorithms.genetic.SteadyStateGeneticAlgorithm;
import algorithms.genetic.crossover.PermutationCrossover;
import algorithms.genetic.mutation.PermutationMutation;
import algorithms.greedy.GreedySolutionGenerator;
import algorithms.greedy.Solution;
import models.Instance;
import parser.InstanceParser;
import utils.Visualization;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        Instance instance = InstanceParser.parseInstanceFile("/Users/filipgulan/college/h-m-o/instances/ts1.txt");
        GreedySolutionGenerator generator = new GreedySolutionGenerator(instance);

        List<Solution> population = new ArrayList<>();
        for (int i = 0, size = 400; i < size; i++) {
            Solution solution = generator
                    .generateBasicSolution()
                    .makeItFeasible()
                    .compressClearedTasks();
            population.add(solution);
        }
        PermutationCrossover crossover = new PermutationCrossover();
        PermutationMutation mutation = new PermutationMutation(0.2f, 250);

        SteadyStateGeneticAlgorithm ga = new SteadyStateGeneticAlgorithm(population, mutation, crossover, 500374);
        Solution bestSolution = ga.run();

//        Solution sol1 = generator.generateBasicSolution().makeItFeasible();
//        Solution sol2 = generator.generateBasicSolution().makeItFeasible();
//        Solution solution = crossover.crossover(sol1, sol2);
//        solution = mutation.mutate(solution);
//        solution = solution.makeItFeasible().compressClearedTasks();
//        Solution bestSolution = solution;
//        for (int i = 0, end = 10000; i < end; i++) {
//            sol1 = generator.generateBasicSolution().makeItFeasible();
//            sol2 = generator.generateBasicSolution().makeItFeasible();
//            if (sol1.totalDuration() < sol2.totalDuration()) {
//                solution = crossover.crossover(sol1, bestSolution);
//            } else {
//                solution = crossover.crossover(sol2, bestSolution);
//            }
//            solution = mutation.mutate(solution);
//            solution = solution.makeItFeasible().compressClearedTasks();
//            if (solution.totalDuration() < bestSolution.totalDuration()) {
//                System.out.println("iteracija: " + i);
//                System.out.println(solution.totalDuration());
//                bestSolution = solution;
//            }
//        }
        System.out.println(bestSolution.totalDuration());

        try(  PrintWriter out = new PrintWriter( "/Users/filipgulan/filename10.txt" )  ){
            out.println( bestSolution.printIt() );
        }

        try(  PrintWriter out1 = new PrintWriter( "/Users/filipgulan/sol.html" )  ){
            out1.println(Visualization.convertSolutionToHTML(bestSolution, instance));
        }
    }
}
