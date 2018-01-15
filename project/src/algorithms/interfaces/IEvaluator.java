package algorithms.interfaces;

import algorithms.solutions.InstanceSolution;

public interface IEvaluator {

    int evaluate(InstanceSolution solution);
    int getTotalEvaluationCount();
}
