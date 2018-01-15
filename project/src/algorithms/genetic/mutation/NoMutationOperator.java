package algorithms.genetic.mutation;

import algorithms.solutions.InstanceSolution;

public class NoMutationOperator implements IMutationOperator {

    @Override
    public InstanceSolution mutate(InstanceSolution solution) {
        return solution;
    }
}
