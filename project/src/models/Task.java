package models;

import java.util.Collections;
import java.util.List;

public class Task {

    private final String id;
    private final int duration;
    private final List<String> machines;
    private final List<Resource> resources;

    public Task(String id, int duration, List<String> machines, List<Resource> resources) {
        this.id = id;
        this.duration = duration;
        this.machines = Collections.unmodifiableList(machines);
        this.resources = Collections.unmodifiableList(resources);
    }

    public String getId() {
        return id;
    }

    public int getDuration() {
        return duration;
    }

    public List<String> getMachines() {
        return machines;
    }

    public List<Resource> getResources() {
        return resources;
    }
}
