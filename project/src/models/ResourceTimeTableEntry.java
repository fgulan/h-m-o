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
        entries.add(index, entry);
    }
}
