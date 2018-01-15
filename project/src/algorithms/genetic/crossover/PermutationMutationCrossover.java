package algorithms.genetic.crossover;

import algorithms.greedy.Solution;
import algorithms.greedy.SupremeSolutionGenerator;
import models.*;
import utils.RandUtils;

import java.util.*;

public class PermutationMutationCrossover implements ICrossoverOperator {

    private final RandUtils rand = RandUtils.getInstance();
    private final float mutationProb;
    private final float crossoverBetterProb;

    public PermutationMutationCrossover(float crossoverBetterProb, float mutationProb) {
        this.crossoverBetterProb = crossoverBetterProb;
        this.mutationProb = mutationProb;
    }

    @Override
    public Solution crossover(Solution sol1, Solution sol2) {

        Map<Task, Machine> taskMachineMap = new HashMap<>();
        Instance instance = sol1.getInstance();
        SupremeSolutionGenerator seg = new SupremeSolutionGenerator(instance);
        List<Task> tasks = instance.getTasksList();
        Collections.shuffle(tasks);

        for (Task task : tasks) {
            // Crossover
            Machine machine = chooseMachine(task, sol1, sol2, taskMachineMap);

            // Mutation
            if (rand.nextFloat() < mutationProb) {
                List<Machine> mcs = task.getMachines();
                Collections.shuffle(mcs);
                machine = mcs.get(rand.nextInt(mcs.size()));
            }
            taskMachineMap.put(task, machine);
        }
        return seg.generate(taskMachineMap);
    }

    private Machine chooseMachine(Task task, Solution sol1, Solution sol2, Map<Task, Machine> taskMachineMap) {
        Map<Task, TaskTimeEntry> tem1 = sol1.getTaskEntryMap();
        Map<Task, TaskTimeEntry> tem2 = sol2.getTaskEntryMap();

        Machine machine1 = tem1.get(task).getMachine();
        Machine machine2 = tem2.get(task).getMachine();

        if (rand.nextFloat() < crossoverBetterProb && sol1.totalDuration() < sol2.totalDuration()) {
            return machine1;
        } else {
            return machine2;
        }
    }

    private int countTasksForMachine(Machine machine, Map<Task, Machine> taskMachineMap) {
        int count = 0;
        for (Task task : taskMachineMap.keySet()) {
            if (taskMachineMap.get(task).equals(machine)) {
                count += 1;
            }
        }
        return count;
    }
}
