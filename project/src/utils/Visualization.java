package utils;

import algorithms.solutions.InstanceSolution;
import models.*;

import java.util.List;

public class Visualization {

    public static String convertSolutionToHTML(InstanceSolution solution, Instance problem) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html>").append("\n");
        sb.append("  <style>").append("\n");
        sb.append(
                "    .line  { height: 50px; background-color: #eee; white-space: nowrap; padding:0px; margin: 5px }\n");
        sb.append(
                "    .event { height: 40px; background-color: #ccc; display:inline-block; margin-left: -2px; }\n");
        sb.append(
                "    .gap   { height:  0px; background-color: #eee; display:inline-block; margin: 0px }\n");
        sb.append("  </style>").append("\n");
        sb.append("  <body>").append("\n");

        sb.append("    test count: " + problem.getTestCount()).append("<br>\n");
        sb.append("    machine count: " + problem.getMachineCount()).append("<br>\n");
        sb.append("    resource count: " + problem.getResourceCount()).append("<br>\n");
        sb.append("    resource multiplicities: ");
        for (Resource resource : problem.getResourcesList()) {
            sb.append(resource).append(", ");
        }

        sb.setLength(sb.length() - 2);
        sb.append("<br>\n");
        for (Machine machine : problem.getMachinesList()) {
            List<TaskTimeEntry> entries = solution.getMachineTimeTable().get(machine);
            sb.append("    <div class=\"line\">").append("\n");
            TaskTimeEntry prev = null;
            for (TaskTimeEntry entry : entries) {
                Task task = entry.getTask();
                sb.append("      ");
                if (entry.getStartTime() > 0 && prev != null) {
                    sb.append("<div class=\"gap\" style=\"");
                    float width = (entry.getStartTime() - prev.getEndTime())/10.f;
                    sb.append("width:" + width + "px\"");
                    sb.append(">").append("</div>");
                }
                float width = entry.getDuration()/10.f;
                sb.append("<div class=\"event\" style=\"");
                sb.append("width:" + width + "px\"");
                sb.append(">").append(task.getId())
                        .append("</div>").append("\n");
                prev = entry;
            }
            sb.append("    </div>").append("\n");
        }
        sb.append("  </body/>").append("\n");
        sb.append("/<html>").append("\n");
        return sb.toString();
    }
}
