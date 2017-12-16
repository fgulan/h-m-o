package models;

import java.util.List;
import java.util.Map;

public class Instance {

    private final List<Task> tasks;
    private final List<String> machines;
    private final Map<String, Resource> resources;

    public Instance(List<Task> tasks, List<String> machines, Map<String, Resource> resources) {
        this.tasks = tasks;
        this.machines = machines;
        this.resources = resources;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public List<String> getMachines() {
        return machines;
    }

    public Map<String, Resource> getResources() {
        return resources;
    }
}
