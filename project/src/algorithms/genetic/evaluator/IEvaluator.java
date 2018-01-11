package algorithms.genetic.evaluator;

import algorithms.interfaces.ISolution;

public interface IEvaluator<S extends ISolution<?>> {
    double evaluate(S solution);
}
