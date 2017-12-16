package models;

public class TaskEntry {

    private Task task;
    private String machine;
    private int startTime;

    public TaskEntry(Task task, String machine, int startTime) {
        this.task = task;
        this.machine = machine;
        this.startTime = startTime;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public String getMachine() {
        return machine;
    }

    public void setMachine(String machine) {
        this.machine = machine;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }
}
