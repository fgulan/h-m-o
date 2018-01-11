package algorithms.greedy;

import models.*;
import utils.ArrayUtils;
import utils.Pair;

import java.util.*;

public class Solution {

    private Map<Machine, List<TaskTimeEntry>> machineTimeTable;
    private Map<Resource, List<ResourceTimeTableEntry>> resourceTimeTable;
    private Instance instance;

    public Solution(Map<Machine, List<TaskTimeEntry>> machineTimeTable, Instance instance) {
        this.machineTimeTable = machineTimeTable;
        this.resourceTimeTable = createInitialResourceTimeTable(instance.getResourcesList());
        this.instance = instance;
    }

    public Solution makeItFeasible() {
        this.resourceTimeTable = createInitialResourceTimeTable(instance.getResourcesList());
        List<Machine> keys = new ArrayList<>(machineTimeTable.keySet());
        Collections.shuffle(keys);
        for (Machine machine : keys) {
            process(machineTimeTable.get(machine));
        }
        return this;
    }

    private void process(List<TaskTimeEntry> entries) {
        Integer size = entries.size();

        for (int i = 0; i < size; i++) {
            TaskTimeEntry entry = entries.get(i);
            if (entry.getTask().getResources().size() == 0) {
                continue;
            }
            Pair<Insertion, List<Insertion>> pair = findNextAvailableStartTime(entry);
            Insertion maxInsertion = pair.getFirst();
            List<Insertion> allInsertions = pair.getSecond();
            shiftEntries(entries, i, maxInsertion.newStartTime - entry.getStartTime());
            insertEntry(entry, allInsertions);
        }
    }

    private void insertEntry(TaskTimeEntry entry, List<Insertion> allInsertions) {
        for (Insertion insertion : allInsertions) {
            insertion.timeTableEntry.insertEntry(insertion.indexInTimeTable, entry);
        }
    }

    private void shiftEntries(List<TaskTimeEntry> entries, int index, int offset) {
        Integer size = entries.size();
        for (int i = index; i < size; i++) {
            entries.get(i).shift(offset);
        }
    }

    private Pair<Insertion, List<Insertion>> findNextAvailableStartTime(TaskTimeEntry entry) {
        int newStartTime = entry.getStartTime();

        List<Resource> neededResources = entry.getTask().getResources();
        Collections.shuffle(neededResources);

        Insertion maxInsertion = null;
        List<Insertion> allInsertions = new ArrayList<>();

        int lastStartTime = newStartTime;
        while (true) {
            allInsertions.clear();
            for (Resource resource : neededResources) {
                Insertion currentInsertion = findNextAvailableStartTimeForResource(resource, newStartTime, entry.getDuration());
                allInsertions.add(currentInsertion);
                if (currentInsertion.newStartTime >= newStartTime) {
                    newStartTime = currentInsertion.newStartTime;
                    maxInsertion = currentInsertion;
                }
            }
            if (newStartTime == lastStartTime) {
                break;
            } else {
                lastStartTime = newStartTime;
            }
        }

        return new Pair<>(maxInsertion, allInsertions);
    }

    private Insertion findNextAvailableStartTimeForResource(Resource resource, int startTime, int duration) {
        List<ResourceTimeTableEntry> timeTable = resourceTimeTable.get(resource);
        int maximalStartTime = Integer.MAX_VALUE;
        Insertion minimalFeasibleInsertion = null;

        Insertion currentInsertion = null;
        for (ResourceTimeTableEntry timeTableEntry : timeTable) {
            currentInsertion = timeTableEntry.findStartForEntry(startTime, duration);
            if (currentInsertion.newStartTime >= startTime
                    && currentInsertion.newStartTime < maximalStartTime) {
                maximalStartTime = currentInsertion.newStartTime;
                minimalFeasibleInsertion = currentInsertion;
            }
        }
        return minimalFeasibleInsertion;
    }

    public Map<Machine, List<TaskTimeEntry>> getMachineTimeTable() {
        return machineTimeTable;
    }

    public int totalDuration() {
        int totalDuration = 0;
        for (Machine machine : instance.getMachinesList()) {
            List<TaskTimeEntry> entries = machineTimeTable.get(machine);
            TaskTimeEntry last = ArrayUtils.getLast(entries);
            if (last != null && last.getEndTime() > totalDuration) {
                totalDuration = last.getEndTime();
            }
        }
        return totalDuration;
    }

    private Map<Resource, List<ResourceTimeTableEntry>> createInitialResourceTimeTable(List<Resource> resources) {
        Map<Resource, List<ResourceTimeTableEntry>> resourceTimeTable = new HashMap<>();

        for (Resource resource : resources) {
            List<ResourceTimeTableEntry> entries = new ArrayList<>();
            for (int i = 0; i < resource.getCount(); i++) {
                entries.add(new ResourceTimeTableEntry(resource));
            }
            resourceTimeTable.put(resource, entries);
        }
        return resourceTimeTable;
    }

    public String printIt() {
        StringBuilder builder = new StringBuilder();
        Map<String, PrintoutModel> printout = new HashMap<>();

        List<Machine> keys = new ArrayList<>(machineTimeTable.keySet());
        for (Machine machine : keys) {
            List<TaskTimeEntry> entries = machineTimeTable.get(machine);
            if (entries == null) {
                continue;
            }
            for (TaskTimeEntry entry : entries) {
                builder.append(String.format("'%s',%d,'%s'.\n",
                        entry.getTask().getId(),
                        entry.getStartTime(),
                        machine.getId()));
            }
        }
        return builder.toString();
    }

    public boolean isSorted() {
        boolean sorted = true;
        List<Resource> resources = instance.getResourcesList();

        for (Resource resource : resources) {
            List<ResourceTimeTableEntry> timeTableEntryList = resourceTimeTable.get(resource);
            for (ResourceTimeTableEntry timeTableEntry : timeTableEntryList) {
                sorted = sorted && timeTableEntry.isSorted();
                if (!sorted) {
                    System.out.println("Kurko boii 22");
                }
            }
        }

        return sorted;
    }
}
