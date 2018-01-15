package algorithms.genetic.crossover;

import algorithms.solutions.InstanceSolution;

public interface ICrossoverOperator {

    InstanceSolution crossover(InstanceSolution sol1, InstanceSolution sol2);
}
