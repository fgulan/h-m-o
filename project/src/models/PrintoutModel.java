package models;

import java.util.Objects;

public class PrintoutModel {

    public String taskID;
    public int startTime;
    public String machineId;

    public PrintoutModel(String taskID, int startTime, String machineId) {
        this.taskID = taskID;
        this.startTime = startTime;
        this.machineId = machineId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PrintoutModel that = (PrintoutModel) o;
        return Objects.equals(taskID, that.taskID);
    }

    @Override
    public int hashCode() {

        return Objects.hash(taskID);
    }
}
