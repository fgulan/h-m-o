package models;

import java.util.Objects;

public class Resource {

    private final String id;
    private final int count;

    public Resource(String id, int count) {
        this.id = id;
        this.count = count;
    }

    public String getId() {
        return id;
    }

    public int getCount() {
        return count;
    }

    @Override
    public String toString() {
        return String.format("%s => %s", id, count);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resource resource = (Resource) o;
        return count == resource.count &&
                Objects.equals(id, resource.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, count);
    }
}
