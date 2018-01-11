package algorithms.genetic.impl;

import algorithms.genetic.crossover.ICrossoverOperator;
import algorithms.genetic.evaluator.IEvaluator;
import algorithms.genetic.mutation.IMutationOperator;
import algorithms.genetic.selection.ISelectionOperator;
import algorithms.interfaces.IAlgorithm;
import algorithms.interfaces.ISolution;
import algorithms.interfaces.ISupplier;

public abstract class AbstractGeneticAlgorithm<S extends ISolution<?>> implements IAlgorithm<S> {

    protected final int populationSize;
    protected final int maxGenerations;

    protected final IMutationOperator<S> mutation;
    protected final ICrossoverOperator<S> crossover;
    protected final ISelectionOperator<S> selection;

    protected final IEvaluator<S> evaluator;
    protected final ISupplier<S> supplier;

    public AbstractGeneticAlgorithm(int populationSize, int maxGenerations,
                                    IMutationOperator<S> mutation, ICrossoverOperator<S> crossover, ISelectionOperator<S> selection,
                                    IEvaluator<S> evaluator, ISupplier<S> supplier) {
        this.populationSize = populationSize;
        this.maxGenerations = maxGenerations;
        this.mutation = mutation;
        this.crossover = crossover;
        this.selection = selection;
        this.evaluator = evaluator;
        this.supplier = supplier;
    }
}