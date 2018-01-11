package parser;

import models.Instance;
import models.Machine;
import models.Resource;
import models.Task;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class InstanceParser {

    private static final String TASK_KEY = "test";
    private static final String MACHINE_KEY = "embedded_board";
    private static final String RESOURCE_KEY = "resource";

    private static final Pattern RESOURCE_PATTERN
            = Pattern.compile("resource\\(\\s*'(.+)',\\s*(\\d+)\\).");
    private static final Pattern MACHINE_PATTERN
            = Pattern.compile("embedded_board\\(\\s*'(.+)'\\).");
    private static final Pattern TASK_PATTERN
            = Pattern.compile("test\\(\\s*'(.+)',\\s*(\\d+),\\s*\\[(.*)\\],\\s*\\[(.*)\\]\\).");

    public static Instance parseInstanceFile(String filePath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));

        Map<String, Resource> resourceMap = new HashMap<>();

        // First extract resources since exams are dependant on them
        lines.forEach(line -> {
            if (line.startsWith(RESOURCE_KEY)) {
                Resource resource = parseResource(line);
                resourceMap.put(resource.getId(), resource);
            }
        });

        // Then parse machines
        Map<String, Machine> machineMap = new HashMap<>();
        lines.forEach(line -> {
           if (line.startsWith(MACHINE_KEY)) {
                Machine machine = parseMachine(line);
                machineMap.put(machine.getId(), machine);
            }
        });

        // And now parse task and asign machines
        Map<String, Task> taskMap = new HashMap<>();

        lines.forEach(line -> {
            if (line.startsWith(TASK_KEY)) {
                Task task = parseTask(line, resourceMap, machineMap);
                taskMap.put(task.getId(), task);
            }
        });

        return new Instance(taskMap, machineMap, resourceMap);
    }

    private static Task parseTask(String line, Map<String, Resource> resourceMap, Map<String, Machine> machineMap) {
        Matcher match = TASK_PATTERN.matcher(line);
        if (match.find()) {
            String id = match.group(1);
            Integer duration = Integer.parseInt(match.group(2));
            List<Machine> machines = parseArrayKeys(match.group(3))
                    .stream()
                    .map(Machine::new)
                    .collect(Collectors.toList());
            if (machines == null || machines.size() == 0) {
                machines = machineMap
                        .values()
                        .stream()
                        .collect(Collectors.toList());
            }
            List<Resource> resoruces = parseArrayKeys(match.group(4))
                    .stream()
                    .map(key -> resourceMap.get(key))
                    .collect(Collectors.toList());
            return new Task(id, duration, machines, resoruces);
        } else {
            throw new RuntimeException("Invalid exam entry! -> " + line);
        }
    }

    private static Machine parseMachine(String line) {
        Matcher match = MACHINE_PATTERN.matcher(line);
        if (match.find()) {
            return new Machine(match.group(1));
        } else {
            throw new RuntimeException("Invalid machine entry! -> " + line);
        }
    }

    private static Resource parseResource(String line) {
        Matcher match = RESOURCE_PATTERN.matcher(line);
        if (match.find()) {
            return new Resource(match.group(1), Integer.parseInt(match.group(2)));
        } else {
            throw new RuntimeException("Invalid resource entry! -> " + line);
        }
    }

    private static List<String> parseArrayKeys(String keys) {
        String[] arrayKeys = keys.trim().replace("'", "").split(",");
        Predicate<String> isEmpty = String::isEmpty;
        Predicate<String> notEmpty = isEmpty.negate();
        return Arrays.asList(arrayKeys)
                .stream()
                .filter(notEmpty)
                .collect(Collectors.toList());
    }
}
