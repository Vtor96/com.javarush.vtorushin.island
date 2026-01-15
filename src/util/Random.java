package util;

import java.util.concurrent.ThreadLocalRandom;

public class Random {
    public static ThreadLocalRandom getRandom(){
        return ThreadLocalRandom.current();
    }

    public static int nextInt(int bound) {
        return getRandom().nextInt(bound);
    }

    public static int nextInt(int origin, int bound) {
        return getRandom().nextInt(origin, bound);
    }

    public static double nextDouble() {
        return getRandom().nextDouble();
    }

    public static double nextDouble(double origin, double bound) {
        return getRandom().nextDouble(origin, bound);
    }
}
