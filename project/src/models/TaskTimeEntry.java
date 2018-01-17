package models;

import utils.ArrayUtils;
import utils.Pair;

import javax.crypto.Mac;
import java.util.List;
import java.util.Objects;

public class TaskTimeEntry implements Comparable<TaskTimeEntry> {

    private Task task;
    private Machine machine;
    private int startTime;
    private int endTime;

    public TaskTimeEntry(int startTime, int endTime, Task task) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.task = task;
    }

    public TaskTimeEntry(TaskTimeEntry previous, Task task) {
        this(previous.endTime, task);
    }

    public TaskTimeEntry(Task task) {
        this(0, task.getDuration(), task);
    }

    public TaskTimeEntry(int startTime, Task task) {
        this(startTime, startTime + task.getDuration(), task);
    }

    public Task getTask() {
        return task;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public int getDuration() {
        return task.getDuration();
    }

    public TaskTimeEntry shift(int offset) {
        this.startTime += offset;
        this.endTime += offset;
        return this;
    }

    public Machine getMachine() {
        return machine;
    }

    public void setMachine(Machine machine) {
        this.machine = machine;
    }

    @Override
    public int compareTo(TaskTimeEntry other) {
        if (startTime == other.startTime) {
            return 0;
        }
        if (startTime > other.startTime) {
            return 1;
        }
        return -1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskTimeEntry entry = (TaskTimeEntry) o;
        return startTime == entry.startTime;
    }

    @Override
    public int hashCode() {

        return Objects.hash(startTime);
    }

    public static Pair<Integer, Integer> firstAvailableSlot(Task task, int minStartTime, List<TaskTimeEntry> entries) {
        int newStartTime = minStartTime, index = -1;
        int size = entries.size();
        int duration = task.getDuration();

        if (size == 0) {
            newStartTime = minStartTime;
            index = 0;
        } else if (size == 1) {
            TaskTimeEntry currEntry = entries.get(0);
            if (currEntry.getStartTime() - duration >= minStartTime) {
                newStartTime = currEntry.getStartTime() - duration;
                index = 0;
            } else {
                newStartTime = Math.max(currEntry.getEndTime(), minStartTime);
                index = 1;
            }
        } else {
            int hole = Integer.MAX_VALUE;

            TaskTimeEntry currEntry = entries.get(0);
            if (currEntry.getStartTime() - duration >= minStartTime) {
                index = 0;
                newStartTime = currEntry.getStartTime() - duration;
                hole = newStartTime;
            }

            for (int i = 0; i < size - 1; i++) {
                TaskTimeEntry current = entries.get(i);
                TaskTimeEntry next = entries.get(i + 1);
                int startTime = Math.max(minStartTime, current.getEndTime());
                int endTime = startTime + duration;

                if (startTime >= current.getEndTime() && endTime <= next.getStartTime()) {
                    int currentHole = startTime - current.getEndTime();
                    if (currentHole < hole) {
                        index = i + 1;
                        newStartTime = startTime;
                        hole = currentHole;
                    }
                }
            }
        }

        if (index == -1) {
            TaskTimeEntry last = ArrayUtils.getLast(entries);
            index = size;
            newStartTime = Math.max(last.getEndTime(), minStartTime);
        }

        return new Pair<>(newStartTime, index);
    }

}
