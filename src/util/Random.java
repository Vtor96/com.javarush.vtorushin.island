package util;

import java.util.concurrent.ThreadLocalRandom;

public class Random {
    public static ThreadLocalRandom getRandom(){
        return ThreadLocalRandom.current();
    }

    public static int nextInt(int bound) {
        return getRandom().nextInt(bound);
    }

    public static double nextDouble() {
        return getRandom().nextDouble();
    }
}
