package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Task {

    private final String id;
    private final int intId;

    private final int duration;
    private final List<Machine> machines;
    private final List<Resource> resources;

    public Task(String id, int duration, List<Machine> machines, List<Resource> resources) {
        this.id = id;
        this.intId = Integer.valueOf(id.replace("t", ""));
        this.duration = duration;
        this.machines = Collections.unmodifiableList(machines);
        this.resources = Collections.unmodifiableList(resources);
    }

    public String getId() {
        return id;
    }

    public int getIntId() {
        return intId;
    }

    public int getDuration() {
        return duration;
    }

    public List<Machine> getMachines() {
        return new ArrayList<>(machines);
    }

    public List<Resource> getResources() {
        return new ArrayList<>(resources);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return duration == task.duration &&
                Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, duration);
    }
}
