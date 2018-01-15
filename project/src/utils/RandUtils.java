package utils;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RandUtils {

    private static final RandUtils instance = new RandUtils();
    private Random random;

    private RandUtils() {
        random = new Random();
    }

    public boolean nextBool() {
        return random.nextBoolean();
    }

    public float nextFloat() {
        return random.nextFloat();
    }

    public int nextInt(int bound) {
        return random.nextInt(bound);
    }

    public static RandUtils getInstance() {
        return instance;
    }

    public Pair<Integer, Integer> nextIntPair(int bound) {
        List<Integer> indexes = generateIndexes(2, bound).collect(Collectors.toList());
        return new Pair<>(indexes.get(0), indexes.get(1));
    }

    public Stream<Integer> generateIndexes(int count, int bound) {
        Set<Integer> intSet = new HashSet<>();
        while (intSet.size() < count) {
            intSet.add(random.nextInt(bound));
        }
        return intSet.stream();
    }
}
