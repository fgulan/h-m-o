package models;

import java.util.*;
import java.util.stream.Collectors;

public class Instance {

    private final Map<String, Task> tasks;
    private final Map<String, Machine> machines;
    private final Map<String, Resource> resources;

    private final List<Task> tasksList;
    private final List<Machine> machinesList;
    private final List<Resource> resourcesList;

    private final List<Task> clearTasksList;

    private Random random;

    public List<Task> sorted;

    public Instance(Map<String, Task> tasks, Map<String, Machine> machines, Map<String, Resource> resources, List<Task> sorted) {
        this.tasks = tasks;
        this.machines = machines;
        this.resources = resources;

        this.tasksList = tasks.values().stream().collect(Collectors.toList());
        this.machinesList = machines.values().stream().collect(Collectors.toList());
        this.resourcesList = resources.values().stream().collect(Collectors.toList());

        this.random = new Random();
        this.sorted = sorted;
        this.clearTasksList = tasksList.stream()
                .filter(task -> task.getResources().isEmpty())
                .collect(Collectors.toList());
    }

    public Map<String, Task> getTasks() {
        return tasks;
    }

    public Map<String, Machine> getMachines() {
        return machines;
    }

    public Map<String, Resource> getResources() {
        return resources;
    }

    public List<Task> getTasksList() {
        return new ArrayList<>(tasksList);
    }

    public List<Machine> getMachinesList() {
        return machinesList;
    }

    public List<Resource> getResourcesList() {
        return resourcesList;
    }

    public int getTestCount() {
        return tasksList.size();
    }

    public int getMachineCount() {
        return machinesList.size();
    }

    public int getResourceCount() {
        return resourcesList.size();
    }

    public Machine getRandomMachineForTask(Task task) {
        List<Machine> availableMachines = task.getMachines();
        int index = random.nextInt(availableMachines.size());
        return availableMachines.get(index);
    }

    public List<Task> getClearTasksList() {
        return clearTasksList;
    }

    public Map<Machine, List<TaskTimeEntry>> createInitialMachineTimeTable() {
        Map<Machine, List<TaskTimeEntry>> machineTimeTable = new HashMap<>();
        for (Machine machine : machinesList) {
            machineTimeTable.put(machine, new ArrayList<>());
        }
        return machineTimeTable;
    }
}
