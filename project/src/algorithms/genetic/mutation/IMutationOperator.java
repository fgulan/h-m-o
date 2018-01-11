package algorithms.genetic.mutation;

import algorithms.greedy.Solution;

public interface IMutationOperator {

    Solution mutate(Solution solution);
}
