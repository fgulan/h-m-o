package algorithms.genetic.crossover;

import algorithms.greedy.Solution;
import models.Instance;
import models.Machine;
import models.Task;
import models.TaskTimeEntry;
import utils.ArrayUtils;
import utils.RandUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PermutationCrossover implements ICrossoverOperator {

    @Override
    public Solution crossover(Solution sol1, Solution sol2) {
        Map<Machine, List<TaskTimeEntry>> mtt1 = sol1.getMachineTimeTable();
        Map<Machine, List<TaskTimeEntry>> mtt2 = sol2.getMachineTimeTable();

        Map<Machine, List<TaskTimeEntry>> iteratingMtt = mtt1;
        if (sol2.totalDuration() < sol1.totalDuration()) {
            iteratingMtt = mtt2;
        }

        Instance instance = sol1.getInstance();
        Map<Machine, List<TaskTimeEntry>> mttChild = instance.createInitialMachineTimeTable();
        Map<Task, TaskTimeEntry> taskEntryMap = new HashMap<>();

        for (Machine oldMachine : iteratingMtt.keySet()) {
            for (TaskTimeEntry oldEntry : iteratingMtt.get(oldMachine)) {

                Task task = oldEntry.getTask();
                Machine machine = chooseMachine(task, sol1, sol2);
                List<TaskTimeEntry> entries = mttChild.get(machine);
                TaskTimeEntry entry = ArrayUtils.getLast(entries);
                if (entry == null) {
                    entry = new TaskTimeEntry(task);
                } else {
                    entry = new TaskTimeEntry(entry, task);
                }
                entries.add(entry);
                entry.setMachine(machine);
                taskEntryMap.put(task, entry);

            }
        }

        return new Solution(mttChild, taskEntryMap, instance);
    }

    private Machine chooseMachine(Task task, Solution sol1, Solution sol2) {
        Map<Task, TaskTimeEntry> tem1 = sol1.getTaskEntryMap();
        Map<Task, TaskTimeEntry> tem2 = sol2.getTaskEntryMap();

        Machine machine1 = tem1.get(task).getMachine();
        Machine machine2 = tem2.get(task).getMachine();
        return RandUtils.getInstance().nextBool() ? machine1 : machine2;
    }
}
