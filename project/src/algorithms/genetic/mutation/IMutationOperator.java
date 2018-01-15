package algorithms.genetic.mutation;

import algorithms.solutions.InstanceSolution;

public interface IMutationOperator {

    InstanceSolution mutate(InstanceSolution solution);
}
