package algorithms.genetic.selection;

import algorithms.interfaces.ISolution;
import algorithms.solutions.Population;

import java.util.List;

public interface ISelectionOperator<S extends ISolution<?>> {

    List<S> selectBest(Population<S> population);
    List<S> selectWorst(Population<S> population);
}
