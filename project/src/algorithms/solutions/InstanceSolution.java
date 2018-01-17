package algorithms.solutions;

import models.*;
import utils.ArrayUtils;
import utils.Pair;

import java.util.*;

public class InstanceSolution {

    private class PrintEntry {
        final String taskID;
        final int startTime;
        final String machineID;

        public PrintEntry(String taskID, int startTime, String machineID) {
            this.taskID = taskID;
            this.startTime = startTime;
            this.machineID = machineID;
        }

        public PrintEntry(TaskTimeEntry entry) {
            this.taskID = entry.getTask().getId();
            this.startTime = entry.getStartTime();
            this.machineID = entry.getMachine().getId();
        }

        @Override
        public String toString() {
            return String.format("'%s',%d,'%s'.\n", taskID, startTime, machineID);
        }
    }

    private final Map<Machine, List<TaskTimeEntry>> machineTimeTable;
    private final Map<Task, TaskTimeEntry> taskEntryMap;
    private final Instance instance;

    private Integer duration;

    public InstanceSolution(Map<Machine, List<TaskTimeEntry>> machineTimeTable, Map<Task, TaskTimeEntry> taskEntryMap, Instance instance) {
        this.machineTimeTable = machineTimeTable;
        this.instance = instance;
        this.taskEntryMap = taskEntryMap;
        this.duration = null;
    }

    public int calculateTotalDuration() {
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

    public String printIt() {
        StringBuilder builder = new StringBuilder();
        List<Machine> keys = new ArrayList<>(machineTimeTable.keySet());

        List<Pair<Task, PrintEntry>> printEntries = new ArrayList<>();

        for (Machine machine : keys) {
            List<TaskTimeEntry> entries = machineTimeTable.get(machine);
            if (entries == null) {
                continue;
            }
            for (TaskTimeEntry entry : entries) {
                PrintEntry printEntry = new PrintEntry(entry);
                printEntries.add(new Pair<>(entry.getTask(), printEntry));
            }
        }

        Collections.sort(printEntries, Comparator.comparingInt(o -> o.getFirst().getIntId()));
        for (Pair<Task, PrintEntry> pair : printEntries) {
            builder.append(pair.getSecond());
        }
        return builder.toString();
    }

    public Machine getMachineForTask(Task task) {
        return taskEntryMap.get(task).getMachine();
    }

    public Instance getInstance() {
        return instance;
    }

    public Map<Task, TaskTimeEntry> getTaskEntryMap() {
        return taskEntryMap;
    }

    public Map<Machine, List<TaskTimeEntry>> getMachineTimeTable() {
        return machineTimeTable;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }
}
