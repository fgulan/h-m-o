package algorithms.solutions;

import algorithms.interfaces.ISolution;

public class AbstractSolution<T> implements ISolution<T> {

    private T data;
    private double fitness;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    @Override
    public int compareTo(ISolution<T> other) {
        return -Double.compare(this.fitness, other.getFitness());
    }
}

