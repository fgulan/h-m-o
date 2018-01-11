package algorithms.interfaces;

public interface IAlgorithm<S extends ISolution<?>> {

    S run();
}
