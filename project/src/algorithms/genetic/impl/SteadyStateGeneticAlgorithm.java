package algorithms.genetic.impl;

import algorithms.genetic.crossover.ICrossoverOperator;
import algorithms.genetic.evaluator.IEvaluator;
import algorithms.genetic.mutation.IMutationOperator;
import algorithms.genetic.selection.ISelectionOperator;
import algorithms.interfaces.ISolution;
import algorithms.interfaces.ISupplier;
import algorithms.solutions.Population;
import utils.Pair;

public class SteadyStateGeneticAlgorithm<S extends ISolution<?>> extends AbstractGeneticAlgorithm {


    public SteadyStateGeneticAlgorithm(int populationSize, int maxGenerations,
                                       IMutationOperator mutation, ICrossoverOperator crossover, ISelectionOperator selection,
                                       IEvaluator evaluator, ISupplier supplier) {
        super(populationSize, maxGenerations, mutation, crossover, selection, evaluator, supplier);
    }

    @Override
    public S run() {
        // Create initial population
        Population<S> population = supplier.createPopulation(populationSize);
        // Evaluate (calculate fitness) current population
        evaluatePopulation(population);
        // Get current best from population
        Pair<Integer, S> bestPair = population.getBest();

        for (int i = 0; i < maxGenerations; i++) {
            Population<S> newPopulation = new Population<>(populationSize);



        }
        return null;
    }

    private void evaluatePopulation(Population<S> population) {

    }
}