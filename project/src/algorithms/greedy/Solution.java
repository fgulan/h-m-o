package algorithms.greedy;

import models.*;
import utils.ArrayUtils;

import java.util.*;

public class Solution {

    private Map<Machine, List<TaskTimeEntry>> machineTimeTable;
    private Map<Task, TaskTimeEntry> taskEntryMap;

    private Instance instance;
    private Integer duration;

    public Solution(Map<Machine, List<TaskTimeEntry>> machineTimeTable, Map<Task, TaskTimeEntry> taskEntryMap, Instance instance) {
        this.machineTimeTable = machineTimeTable;
        this.instance = instance;
        this.taskEntryMap = taskEntryMap;
        this.duration = null;
    }

    public int totalDuration() {
        if (duration != null) {
            return duration;
        }
        int totalDuration = 0;
        for (Machine machine : instance.getMachinesList()) {
            List<TaskTimeEntry> entries = machineTimeTable.get(machine);
            TaskTimeEntry last = ArrayUtils.getLast(entries);
            if (last != null && last.getEndTime() > totalDuration) {
                totalDuration = last.getEndTime();
            }
        }
        this.duration = totalDuration;
        return totalDuration;
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
}
