import algorithms.greedy.GreedySolutionGenerator;
import algorithms.greedy.Solution;
import models.Instance;
import parser.InstanceParser;

import java.io.IOException;
import java.io.PrintWriter;

public class Main {

    public static void main(String[] args) throws IOException {
        Instance instance = InstanceParser.parseInstanceFile("/Users/filipgulan/college/h-m-o/instances/ts2.txt");
        GreedySolutionGenerator generator = new GreedySolutionGenerator(instance);

        Solution solution = generator.generateBasicSolution().makeItFeasible();
        Solution bestSolution = solution;

        for (int i = 0; i < 10000; i++) {
            Solution newOne = generator.generateBasicSolution().makeItFeasible();
            if (newOne.totalDuration() < bestSolution.totalDuration()) {
                System.out.println(newOne.totalDuration());
                bestSolution = newOne;
            }
        }
        try(  PrintWriter out = new PrintWriter( "/Users/filipgulan/filename.txt" )  ){
            out.println( bestSolution.printIt() );
        }

    }
}
