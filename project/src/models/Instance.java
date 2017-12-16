package models;

import java.util.List;
import java.util.Map;

public class Instance {

    private final List<Exam> exams;
    private final List<String> machines;
    private final Map<String, Resource> resources;

    public Instance(List<Exam> exams, List<String> machines, Map<String, Resource> resources) {
        this.exams = exams;
        this.machines = machines;
        this.resources = resources;
    }

    public List<Exam> getExams() {
        return exams;
    }

    public List<String> getMachines() {
        return machines;
    }

    public Map<String, Resource> getResources() {
        return resources;
    }
}
