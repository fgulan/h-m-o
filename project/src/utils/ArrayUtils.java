package utils;

import java.util.List;

public class ArrayUtils {

    public static <T> T getLast(List<T> list) {
        if (list.isEmpty()) {
            return null;
        }
        return list.get(list.size() - 1);
    }

    public static <T> T getFirst(List<T> list) {
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }
}
