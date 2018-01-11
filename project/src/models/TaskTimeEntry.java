package models;

public class TaskTimeEntry implements Comparable<TaskTimeEntry> {

    private Task task;
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

    public void setTask(Task task) {
        this.task = task;
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
}
