package algorithms.genetic.crossover;

import algorithms.solutions.InstanceSolution;
import algorithms.greedy.SupremeSolutionGenerator;
import models.Instance;
import models.Machine;
import models.Task;
import utils.RandUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NPointPermutationMutationCrossover implements ICrossoverOperator {

    private final int pointsCount;
    private final float mutationProb;
    private final RandUtils rand = RandUtils.getInstance();

    public NPointPermutationMutationCrossover(int pointsCount, float mutationProb) {
        this.pointsCount = pointsCount;
        this.mutationProb = mutationProb;
    }

    @Override
    public InstanceSolution crossover(InstanceSolution sol1, InstanceSolution sol2) {
        Map<Task, Machine> taskMachineMap = new HashMap<>();

        Instance instance = sol1.getInstance();

        SupremeSolutionGenerator generator = new SupremeSolutionGenerator(instance);

        List<Task> tasks = instance.getTasksList();
        Collections.shuffle(tasks);
        int tasksCount = tasks.size();

        List<Integer> indexes = rand.generateIndexes(pointsCount, tasksCount)
                .sorted()
                .collect(Collectors.toList());

        int currentIndex = 0;
        InstanceSolution currentSolution = sol1;

        for (Integer breakIndex : indexes) {
            for (int i = currentIndex; i < breakIndex; i++) {
                insertTask(i, tasks, currentSolution, taskMachineMap);
            }

            currentIndex = breakIndex;
            currentSolution = currentSolution == sol1 ? sol2 : sol1;
        }

        for (int i = currentIndex; i < tasksCount; i++) {
            insertTask(i, tasks, currentSolution, taskMachineMap);
        }
        return generator.generate(taskMachineMap);
    }

    private void insertTask(int index, List<Task> tasks, InstanceSolution solution, Map<Task, Machine> taskMachineMap) {
        Task task = tasks.get(index);
        Machine machine = solution.getMachineForTask(task);
        machine = mutate(task, machine);
        taskMachineMap.put(task, machine);
    }

    private Machine mutate(Task task, Machine machine) {
        if (rand.nextFloat() < mutationProb) {
            List<Machine> machines = task.getMachines();
            Collections.shuffle(machines);
            machine = machines.get(rand.nextInt(machines.size()));
        }
        return machine;
    }
}
