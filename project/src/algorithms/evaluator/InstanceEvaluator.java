package algorithms.evaluator;

import algorithms.interfaces.IEvaluator;
import algorithms.solutions.InstanceSolution;

public class InstanceEvaluator implements IEvaluator {

    private int totalEvaluationCount = 0;

    @Override
    public int evaluate(InstanceSolution solution) {
        Integer duration = solution.getDuration() ;
        if (duration == null) {
            duration = solution.calculateTotalDuration();
            solution.setDuration(duration);
            totalEvaluationCount += 1;
        }
        return duration;
    }

    public int getTotalEvaluationCount() {
        return totalEvaluationCount;
    }
}
