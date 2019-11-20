package com.example.redissionspringbootstarter.service;


public class ConfigService {
    private static final Long valueStart = 0L;
    private static final Long COUNTER_CORRECT = 1l;
    private static final String COUNTER_KEY = "counter";

    public static Long getStartCountValue() {
        return valueStart;
    }

    public static Long getCounterInDB() {
        return COUNTER_CORRECT;
    }

    public static String getCounterKey() {
        return COUNTER_KEY;
    }
}
