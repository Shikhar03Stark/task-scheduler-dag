package com.shikhar03stark.scheduler.util;

import java.util.Random;

public class RandomGenerator {

    private static final Random randomObj = new Random();

    public static Integer nextInt() {
        return randomObj.nextInt();
    }
}
