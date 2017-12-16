package algorithms.genetic.crossover;

import algorithms.interfaces.ISolution;
import utils.Pair;

public interface ICrossoverOperator<S extends ISolution<?>> {

    Pair<S, S> crossover(S firstParent, S secondParent);
}
