package algorithms.genetic.mutation;

import algorithms.interfaces.ISolution;

public interface IMutationOperator<S extends ISolution<?>> {
    S mutate(S solution);
}
