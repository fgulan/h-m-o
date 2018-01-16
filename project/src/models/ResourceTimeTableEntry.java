package models;

import utils.ArrayUtils;
import utils.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ResourceTimeTableEntry {

    public List<TaskTimeEntry> entries;

    public ResourceTimeTableEntry() {
        this.entries = new ArrayList<>();
    }

    public void insertEntry(int index, TaskTimeEntry entry) {
        entries.add(index, entry);
    }
}
