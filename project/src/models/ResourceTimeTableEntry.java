package models;

import utils.ArrayUtils;
import utils.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ResourceTimeTableEntry {

    private final Resource resource;

    public List<TaskTimeEntry> entries;

    public ResourceTimeTableEntry(Resource resource) {
        this.resource = resource;
        this.entries = new ArrayList<>();
    }

    public Insertion findStartForEntry(int startTime, int duration) {
        Collections.sort(entries);
        Insertion ins = new Insertion();
        ins.timeTableEntry = this;
        ins.indexInTimeTable = -1;

        int endTime = startTime + duration;
        int size = entries.size();
        if (size == 0) {
            ins.newStartTime = startTime;
            ins.indexInTimeTable = 0;
        } else if (size == 1) {
            TaskTimeEntry currEntry = entries.get(0);
            if (currEntry.getStartTime() - duration >= startTime) {
                ins.newStartTime = startTime;
                ins.indexInTimeTable = 0;
            } else {
                ins.newStartTime = Math.max(currEntry.getEndTime(), startTime);
                ins.indexInTimeTable = 1;
            }
        } else {
            TaskTimeEntry currEntry = entries.get(0);
            if (currEntry.getStartTime() - duration >= startTime) {
                ins.newStartTime = startTime;
                ins.indexInTimeTable = 0;
            } else {
                for (int i = 0; i < size - 1; i++) {
                    TaskTimeEntry current = entries.get(i);
                    TaskTimeEntry next = entries.get(i + 1);
                    if (startTime >= current.getEndTime() && endTime <= next.getStartTime()) {
                        ins.indexInTimeTable = i + 1;
                        ins.newStartTime = startTime;
                        break;
                    }
                }
            }
        }

        if (ins.indexInTimeTable == -1) {
            TaskTimeEntry last = ArrayUtils.getLast(entries);
            ins.indexInTimeTable = size;
            ins.newStartTime = Math.max(last.getEndTime(), startTime);
        }

        return ins;
    }

    public void insertEntry(int index, TaskTimeEntry entry) {
        if (entries.contains(entry)) {
            System.out.println("Entry already exists");
        }
        entries.add(index, entry);
    }

    public boolean addEntry(TaskTimeEntry entry) {
        int insertIndex = -1;
        int size = entries.size();
//        if (size == 0) {
//            entries.add(entry);
//            return true;
//        } else if (size == 1) {
//        } else {
//            entries.add(entry);
//            return true;
//        }

        if (size == 0) {
            insertIndex = 0;
        } else if (size == 1) {
            TaskTimeEntry currentEntry = entries.get(0);
            if (currentEntry.getStartTime() >= entry.getTask().getDuration()) {
                insertIndex = 0;
            } else {
                insertIndex = 1;
            }
        } else {
            insertIndex = size;
        }

        if (insertIndex >= 0) {
            entries.add(insertIndex, entry);
        }

        return insertIndex >= 0;
//        int insertIndex = -1;
//
//        for (int i = 0; i < size - 1; i++) {
//            TaskTimeEntry current = entries.get(i);
//            TaskTimeEntry next = entries.get(i + 1);
//            if (next.getStartTime() - current.getEndTime() >= entry.getDuration()) {
//                insertIndex = i + 1;
//                break;
//            } else if (i + 1 == size - 1) {
//                if (entry.getStartTime() >= next.getEndTime()) {
//                    insertIndex = size;
//                }
//            }
//        }
//
//        if (insertIndex == -1) {
//            return false;
//        } else {
//            entries.add(insertIndex, entry);
//            return true;
//        }

    }

    public boolean isSorted() {
        boolean sorted = true;
        Collections.sort(entries);
        for (int i = 0; i < entries.size()-1; i++) {
            TaskTimeEntry current = entries.get(i);
            TaskTimeEntry next = entries.get(i + 1);
            sorted = sorted && (current.getEndTime() <= next.getStartTime());
            if (!sorted) {
                System.out.println("Kurko boii");
            }
        }
        return sorted;
    }
}
