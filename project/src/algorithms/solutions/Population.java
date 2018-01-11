package algorithms.solutions;

import algorithms.interfaces.ISolution;
import utils.Pair;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class Population<S extends ISolution<?>> implements Iterable<S> {

    private List<S> population = new ArrayList<>();
    private int capacity;

    public Population(int capacity) {
        if (capacity < 1) {
            throw new IllegalArgumentException("Population needs to have at least one solution");
        }
        this.capacity = capacity;
    }

    public Pair<Integer, S> getBest() {
        return null;
    }

    public List<Pair<Integer, S>> getFirstBests(int n) {
        return null;
    }

    public void add(S solution) {
        Objects.requireNonNull(solution, "Cannot add null to population");
        if (population.size() == capacity) {
            throw new IllegalStateException("Population full");
        }
        population.add(solution);
    }

    public void replace(int index, S solution) {
        Objects.requireNonNull(solution, "Cannot add null to population");
        if (population.size() == capacity) {
            throw new IllegalStateException("Population full");
        } else if (index < 0 || index >= population.size()) {
            throw new IndexOutOfBoundsException("Index out of population bounds");
        }
        population.set(index, solution);
    }

    @Override
    public Iterator<S> iterator() {
        return null;
    }
}
