package algorithms.greedy;

import models.*;
import utils.ArrayUtils;

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
            process(machine, machineTimeTable.get(machine));
        }
        return this;
    }

    private void process(Machine machine, List<TaskTimeEntry> entries) {
        Integer size = entries.size();

        for (int i = 0; i < size; i++) {
            TaskTimeEntry entry = entries.get(i);
            int newStartTime = findNextAvailableStartTime(entry);
            if (entry.getStartTime() < newStartTime) {
                shiftEntriesFromIndex(entries, i, newStartTime - entry.getStartTime());
            }
            addResourceEntry(entry);
        }
    }

    private void addResourceEntry(TaskTimeEntry entry) {
        List<Resource> neededResources = entry.getTask().getResources();
        for (Resource resource : neededResources) {
            List<ResourceTimeTableEntry> timeTable = resourceTimeTable.get(resource);
            for (ResourceTimeTableEntry timeTableEntry : timeTable) {
                if (timeTableEntry.addEntry(entry)) {
                    break;
                }
            }
        }
    }

    private void shiftEntriesFromIndex(List<TaskTimeEntry> entries, int index, int offset) {
        Integer size = entries.size();
        for (int i = index; i < size; i++) {
            entries.get(i).shift(offset);
        }
    }

    private int findNextAvailableStartTime(TaskTimeEntry entry) {
        int newStartTime = entry.getStartTime();
        List<Resource> neededResources = entry.getTask().getResources();
        Collections.shuffle(neededResources);
        for (Resource resource : neededResources) {
            int resourceStart = findNextAvailableStartTimeForResource(resource, entry);
            if (resourceStart > newStartTime) {
                newStartTime = resourceStart;
            }
        }
        return newStartTime;
    }

    private int findNextAvailableStartTimeForResource(Resource resource, TaskTimeEntry entry) {
        List<ResourceTimeTableEntry> timeTable = resourceTimeTable.get(resource);

        int newStartTime = 0;
        for (ResourceTimeTableEntry timeTableEntry : timeTable) {
            int size = timeTableEntry.entries.size();
            if (size == 0) {
                continue;
            } else if (size == 1) {
                TaskTimeEntry currentEntry = timeTableEntry.entries.get(0);
                if (currentEntry.getEndTime() > newStartTime) {
                    newStartTime = currentEntry.getEndTime();
                }
                continue;
            }

            for (int i = 0; i < size - 1; i++) {
                TaskTimeEntry currentEntry = timeTableEntry.entries.get(i);
                TaskTimeEntry nextEntry = timeTableEntry.entries.get(i + 1);
                int tempTime;
                if (nextEntry.getStartTime() - currentEntry.getEndTime() >= entry.getDuration()) {
                    tempTime = currentEntry.getEndTime();
                } else {
                    tempTime = nextEntry.getEndTime();
                }
                if (tempTime > newStartTime) {
                    newStartTime = tempTime;
                }
            }
        }
        return newStartTime;
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
}
