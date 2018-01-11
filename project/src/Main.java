import algorithms.greedy.GreedySolutionGenerator;
import algorithms.greedy.Solution;
import models.Instance;
import parser.InstanceParser;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        Instance instance = InstanceParser.parseInstanceFile("/Users/filipgulan/college/h-m-o/instances/ts1.txt");
        GreedySolutionGenerator generator = new GreedySolutionGenerator(instance);

        Solution sol = generator.generateBasicSolution();
//        Solution sol2 = sol.makeItFeasible();
        System.out.println(sol.printIt());
    }
}
