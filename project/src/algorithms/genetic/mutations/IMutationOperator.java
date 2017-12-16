package algorithms.genetic.mutations;

import algorithms.interfaces.ISolution;

public interface IMutationOperator<S extends ISolution<?>> {
    void mutate(S solution);
}
