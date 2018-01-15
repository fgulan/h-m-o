package algorithms.genetic.mutation;

import algorithms.greedy.Solution;

public class NoMutationOperator implements IMutationOperator {

    @Override
    public Solution mutate(Solution solution) {
        return solution;
    }
}
