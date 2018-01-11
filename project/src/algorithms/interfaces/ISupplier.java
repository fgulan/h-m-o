package algorithms.interfaces;

import algorithms.solutions.Population;

public interface ISupplier<S extends ISolution<?>> {

    Population createPopulation(int size);
}
