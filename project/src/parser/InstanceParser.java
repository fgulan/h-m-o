package parser;

import models.Exam;
import models.Instance;
import models.Resource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class InstanceParser {

    private static final String EXAM_KEY = "test";
    private static final String MACHINE_KEY = "embedded_board";
    private static final String RESOURCE_KEY = "resource";

    private static final Pattern RESOURCE_PATTERN
            = Pattern.compile("resource\\(\\s*'(.+)',\\s*(\\d+)\\).");
    private static final Pattern MACHINE_PATTERN
            = Pattern.compile("embedded_board\\(\\s*'(.+)'\\).");
    private static final Pattern EXAM_PATTERN
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

        // Now parse exams and machines
        List<Exam> exams = new ArrayList<>();
        List<String> machines = new ArrayList<>();
        lines.forEach(line -> {
            if (line.startsWith(EXAM_KEY)) {
                exams.add(parseExam(line, resourceMap));
            } else if (line.startsWith(MACHINE_KEY)) {
                machines.add(parseMachine(line));
            }
        });
        return new Instance(exams, machines, resourceMap);
    }

    private static Exam parseExam(String line, Map<String, Resource> resourceMap) {
        Matcher match = EXAM_PATTERN.matcher(line);
        if (match.find()) {
            String id = match.group(1);
            Integer duration = Integer.parseInt(match.group(2));
            List<String> machines = parseArrayKeys(match.group(3));
            List<Resource> resoruces = parseArrayKeys(match.group(4))
                    .stream()
                    .map(key -> resourceMap.get(key))
                    .collect(Collectors.toList());
            return new Exam(id, duration, machines, resoruces);
        } else {
            throw new RuntimeException("Invalid exam entry! -> " + line);
        }
    }

    private static String parseMachine(String line) {
        Matcher match = MACHINE_PATTERN.matcher(line);
        if (match.find()) {
            return match.group(1);
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
