package algorithms.interfaces;

public interface ISolution<T> extends Comparable<ISolution<T>>  {
    T getData();
    void setData(T data);
    double getFitness();
    void setFitness(double fitness);
}
