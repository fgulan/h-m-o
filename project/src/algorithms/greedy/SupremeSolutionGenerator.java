package algorithms.greedy;

import algorithms.solutions.InstanceSolution;
import models.*;
import utils.Pair;

import java.util.*;

public class SupremeSolutionGenerator {

    private class TempEntry {
        Pair<Integer, Integer> stim;
        List<Insertion> allInsertions;
        int newStartTime;

        public TempEntry(Pair<Integer, Integer> stim, List<Insertion> allInsertions, int newStartTime) {
            this.stim = stim;
            this.allInsertions = allInsertions;
            this.newStartTime = newStartTime;
        }
    }

    private final Instance instance;
    private Map<Machine, List<TaskTimeEntry>> machineTimeTable;
    private Map<Resource, List<ResourceTimeTableEntry>> resourceTimeTable;
    private Map<Task, TaskTimeEntry> taskEntryMap;

    public SupremeSolutionGenerator(Instance instance) {
        this.instance = instance;
    }

    public InstanceSolution generate() {
        taskEntryMap = new HashMap<>();
        machineTimeTable = instance.createInitialMachineTimeTable();
        resourceTimeTable = instance.createInitialResourceTimeTable();

        List<Task> tasks = instance.getTasksList();
        Collections.sort(tasks, (o1, o2) -> Integer.compare(o2.getDuration(), o1.getDuration()));

        for (Task task : tasks) {
            findMachineAndStartTime(task);
        }
        return new InstanceSolution(machineTimeTable, taskEntryMap, instance);
    }

    public InstanceSolution generate(Map<Task, Machine> taskMachineMap) {
        taskEntryMap = new HashMap<>();
        machineTimeTable = instance.createInitialMachineTimeTable();
        resourceTimeTable = instance.createInitialResourceTimeTable();

        List<Task> tasks = new ArrayList<>(taskMachineMap.keySet());
        Collections.sort(tasks, (o1, o2) -> Integer.compare(o2.getDuration(), o1.getDuration()));
        for (Task task : tasks) {
            Machine machine = taskMachineMap.get(task);
            TempEntry tempEntry = findSlot(machine, task);

            TaskTimeEntry entry = new TaskTimeEntry(tempEntry.stim.getFirst(), task);
            List<TaskTimeEntry> machineEntries = machineTimeTable.get(machine);
            machineEntries.add(tempEntry.stim.getSecond(), entry);
            for (Insertion insertion : tempEntry.allInsertions) {
                insertion.timeTableEntry.insertEntry(insertion.indexInTimeTable, entry);
            }
            entry.setMachine(machine);
            taskEntryMap.put(task, entry);
        }
        return new InstanceSolution(machineTimeTable, taskEntryMap, instance);
    }

    private TempEntry findSlot(Machine machine, Task task) {
        int newStartTime = 0;
        Pair<Integer, Integer> startTime_index_machine;
        List<Insertion> allInsertions;
        while (true) {
            startTime_index_machine = getBestMachineStartTime(task, machine, newStartTime);
            allInsertions = getBestResourcesStartTime(task, task.getResources(), newStartTime);

            if (allInsertions.isEmpty()) {
                newStartTime = startTime_index_machine.getFirst();
                break;
            } else {
                Insertion insertion = allInsertions.get(0);
                if (startTime_index_machine.getFirst() == insertion.newStartTime) {
                    newStartTime = startTime_index_machine.getFirst();
                    break;
                } else {
                    newStartTime = Math.max(startTime_index_machine.getFirst(), insertion.newStartTime);
                }
            }
        }
        return new TempEntry(startTime_index_machine, allInsertions, newStartTime);
    }

    private void findMachineAndStartTime(Task task) {
        List<Machine> possibleMachines = task.getMachines();

        Machine bestMachine = null;
        Pair<Integer, Integer> stim = null;
        List<Insertion> bestInsertions = null;

        int lastStartTime = Integer.MAX_VALUE;

        for (Machine machine : possibleMachines) {
            TempEntry tempEntry = findSlot(machine, task);
            if (tempEntry.newStartTime < lastStartTime) {
                bestMachine = machine;
                stim = tempEntry.stim;
                bestInsertions = tempEntry.allInsertions;
            }
        }

        TaskTimeEntry entry = new TaskTimeEntry(stim.getFirst(), task);
        List<TaskTimeEntry> machineEntries = machineTimeTable.get(bestMachine);
        machineEntries.add(stim.getSecond(), entry);
        for (Insertion insertion : bestInsertions) {
            insertion.timeTableEntry.insertEntry(insertion.indexInTimeTable, entry);
        }
        entry.setMachine(bestMachine);
        taskEntryMap.put(task, entry);
    }

    private Pair<Integer, Integer> getBestMachineStartTime(Task task, Machine machine, int minStartTime) {
        List<TaskTimeEntry> entries = machineTimeTable.get(machine);
        return TaskTimeEntry.firstAvailableSlot(task, minStartTime, entries);
    }

    private List<Insertion> getBestResourcesStartTime(Task task, List<Resource> resources, int minStartTime) {
        List<Insertion> allInsertions = new ArrayList<>();

        int newStartTime = minStartTime;
        boolean shouldContinue = false;
        while (true) {
            allInsertions.clear();
            for (Resource resource : resources) {
                Insertion currentInsertion = getBestResourceStartTime(task, resource, newStartTime);
                allInsertions.add(currentInsertion);
                if (currentInsertion.newStartTime > newStartTime) {
                    newStartTime = currentInsertion.newStartTime;
                    shouldContinue = true;
                    break;
                }
            }
            if (shouldContinue) {
                shouldContinue = false;
                continue;
            }
            if (areInsertionsAtSameTime(allInsertions)) {
                break;
            }
        }
        return allInsertions;
    }

    private boolean areInsertionsAtSameTime(List<Insertion> insertions) {
        if (insertions.isEmpty() || insertions.size() == 1) {
            return true;
        }
        boolean same = true;
        int time = insertions.get(0).newStartTime;
        for (Insertion insertion : insertions) {
            same = same && (insertion.newStartTime == time);
        }
        return same;
    }

    private Insertion getBestResourceStartTime(Task task, Resource resource, int minStartTime) {
        List<ResourceTimeTableEntry> timeTable = resourceTimeTable.get(resource);
        int newStartTime = Integer.MAX_VALUE;

        Insertion bestInsertion = new Insertion();
        for (ResourceTimeTableEntry timeTableEntry : timeTable) {
            Pair<Integer, Integer> currentInsertion = TaskTimeEntry.firstAvailableSlot(task, minStartTime, timeTableEntry.entries);
            if (currentInsertion.getFirst() < newStartTime) {
                newStartTime = currentInsertion.getFirst();
                bestInsertion.newStartTime = currentInsertion.getFirst();
                bestInsertion.indexInTimeTable = currentInsertion.getSecond();
                bestInsertion.timeTableEntry = timeTableEntry;
            }
        }
        return bestInsertion;
    }
}
