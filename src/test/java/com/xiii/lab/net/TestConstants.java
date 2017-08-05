package com.xiii.lab.net;

import java.util.Random;

/**
 * Created by Sergey on 05.08.2017
 */

public class TestConstants {
    public static final Random RANDOM;
    public static final double F_SEED;
    public static final IActivationFunction F;

    static {
        RANDOM = new Random();
        F_SEED = RANDOM.nextDouble();
        F = (v, c) -> v * F_SEED;
    }
}
