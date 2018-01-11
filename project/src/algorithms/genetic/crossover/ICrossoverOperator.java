package algorithms.genetic.crossover;

import algorithms.greedy.Solution;
import algorithms.interfaces.ISolution;
import utils.Pair;

public interface ICrossoverOperator {

    Solution crossover(Solution sol1, Solution sol2);
}
