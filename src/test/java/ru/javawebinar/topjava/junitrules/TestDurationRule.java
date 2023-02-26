package ru.javawebinar.topjava.junitrules;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class TestDurationRule implements TestRule {
    private static final Map<String, Long> testsDuration = new LinkedHashMap<>();

    private final static Logger logger = LoggerFactory.getLogger(TestDurationRule.class);

    @Override
    public Statement apply(Statement statement, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                String testName = description.getMethodName();
                Long start = System.currentTimeMillis();
                statement.evaluate();
                Long duration = System.currentTimeMillis() - start;
                logger.info(testName + " - " + duration + " ms\n");
                testsDuration.put(testName, duration);
            }
        };
    }

    public static String getTestDurationStatistic() {
        int maxKeyLength = getMaxLength(testsDuration.keySet());
        return testsDuration
                .entrySet()
                .stream()
                .map(entry -> {
                    String key = entry.getKey();
                    String keyWithSpaces = key + getSpaces(maxKeyLength - key.length());
                    return "\n" + keyWithSpaces + " - " + entry.getValue() + " ms";
                })
                .collect(Collectors.joining(""));
    }

    private static String getSpaces(int spaceQuanity) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < spaceQuanity; i++) {
            stringBuilder.append(" ");
        }
        return stringBuilder.toString();
    }

    private static int getMaxLength(Set<String> strings) {
        return strings.stream()
                .mapToInt(String::length)
                .max()
                .orElse(0);
    }
}
