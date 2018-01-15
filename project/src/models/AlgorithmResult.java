package models;

public class AlgorithmResult<S> {
    private final S solution;
    private final int evaluationCount;
    private final int bestFitness;
    private final long time;

    public AlgorithmResult(S solution, int evaluationCount, int bestFitness, long time) {
        this.solution = solution;
        this.evaluationCount = evaluationCount;
        this.bestFitness = bestFitness;
        this.time = time;
    }

    public S getSolution() {
        return solution;
    }

    public int getEvaluationCount() {
        return evaluationCount;
    }

    public int getBestFitness() {
        return bestFitness;
    }

    public long getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "AlgorithmResult{" +
                "evaluationCount=" + evaluationCount +
                ", bestFitness=" + bestFitness +
                ", time=" + time +
                '}';
    }
}
