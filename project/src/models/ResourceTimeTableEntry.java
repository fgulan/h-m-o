package models;

import java.util.ArrayList;
import java.util.List;

public class ResourceTimeTableEntry {

    private final Resource resource;

    public List<TaskTimeEntry> entries;

    public ResourceTimeTableEntry(Resource resource) {
        this.resource = resource;
        this.entries = new ArrayList<>();
    }

    public boolean addEntry(TaskTimeEntry entry) {
        int size = entries.size();

        if (size == 0) {
            entries.add(entry);
            return true;
        } else if (size == 1) {
            TaskTimeEntry current = entries.get(0);
            if (entry.getStartTime() >= current.getEndTime()) {
                entries.add(entry);
                return true;
            }
            return false;
        }

        int insertIndex = -1;

        for (int i = 0; i < size - 1; i++) {
            TaskTimeEntry current = entries.get(i);
            TaskTimeEntry next = entries.get(i + 1);
            if (next.getStartTime() - current.getEndTime() >= entry.getDuration()) {
                insertIndex = i + 1;
                break;
            } else if (i + 1 == size - 1) {
                if (entry.getStartTime() >= next.getEndTime()) {
                    insertIndex = size;
                }
            }
        }

        if (insertIndex == -1) {
            return false;
        } else {
            entries.add(insertIndex, entry);
            return true;
        }

    }

}
