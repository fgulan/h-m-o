package utils;

import java.util.Random;

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

    public static RandUtils getInstance() {
        return instance;
    }

    public Pair<Integer, Integer> nextIntPair(int bound) {
        int first = random.nextInt(bound);
        int second = random.nextInt(bound);
//        while (first == second) {
//            second = random.nextInt(bound);
//        }
        return new Pair<>(first, second);
    }
}
