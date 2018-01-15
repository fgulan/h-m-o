package algorithms.interfaces;

import algorithms.solutions.InstanceSolution;
import models.AlgorithmResult;

import java.util.List;

public interface IAlgorithm {

    InstanceSolution run();
    List<AlgorithmResult<InstanceSolution>> getResults();
}
