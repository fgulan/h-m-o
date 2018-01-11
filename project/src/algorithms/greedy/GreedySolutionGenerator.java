package algorithms.greedy;

import models.*;
import utils.ArrayUtils;

import java.util.*;

public class GreedySolutionGenerator {

    private final Instance instance;

    public GreedySolutionGenerator(Instance instance) {
        this.instance = instance;
    }

    public Solution generateBasicSolution() {
        Map<Machine, List<TaskTimeEntry>> machineTimeTable = instance.createInitialMachineTimeTable();

        Map<Task, TaskTimeEntry> taskEntryMap = new HashMap<>();

        List<Task> tasks = instance.getTasksList();
        Collections.shuffle(tasks);

        for (Task task : tasks) {
            Machine machine = getBestMachine(task, machineTimeTable);
            List<TaskTimeEntry> entries = getEntriesForMachine(machine, machineTimeTable);
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
        return new Solution(machineTimeTable, taskEntryMap, instance);
    }

    private Machine getBestMachine(Task task, Map<Machine, List<TaskTimeEntry>> machineTimeTable) {
        List<Machine> possibleMachines = task.getMachines();
        Collections.shuffle(possibleMachines);

        Machine bestMachine = instance.getRandomMachineForTask(task);
        TaskTimeEntry lastEntry = ArrayUtils.getLast(machineTimeTable.get(bestMachine));
        if (lastEntry == null) {
            return bestMachine;
        }

        for (Machine machine : possibleMachines) {
            TaskTimeEntry currentEntry = ArrayUtils.getLast(machineTimeTable.get(machine));
            if (currentEntry == null) {
                bestMachine = machine;
                break;
            } else if (currentEntry.getStartTime() < lastEntry.getStartTime()) {
                lastEntry = currentEntry;
                bestMachine = machine;
            }
        }
        return bestMachine;
    }

    private List<TaskTimeEntry> getEntriesForMachine(Machine machine, Map<Machine, List<TaskTimeEntry>> machineTimeTable) {
        List<TaskTimeEntry> entries = machineTimeTable.get(machine);
        if (entries == null) {
            entries = new ArrayList<>();
            machineTimeTable.put(machine, entries);
        }
        return entries;
    }

    private Map<Resource, List<ResourceTimeTableEntry>> createInitialResourceTimeTable(List<Resource> resources) {
        Map<Resource, List<ResourceTimeTableEntry>> resourceTimeTable = new HashMap<>();
        for (Resource resource : resources) {
            resourceTimeTable.put(resource, new ArrayList<>());
        }
        return resourceTimeTable;
    }

    public Instance getInstance() {
        return instance;
    }
}
