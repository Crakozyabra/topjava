package ru.javawebinar.topjava.junitrules;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

public class TestDurationRule implements TestRule {
    private final Map<String, Long> testsDuration;

    private final static Logger logger = LoggerFactory.getLogger(TestDurationRule.class);

    public TestDurationRule(Map<String, Long> testsDuration) {
        this.testsDuration = testsDuration;
    }

    @Override
    public Statement apply(Statement statement, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                String testName = description.getMethodName();
                LocalTime start = LocalTime.now();
                statement.evaluate();
                Long duration = ChronoUnit.NANOS.between(start, LocalTime.now());
                if (!testsDuration.containsKey(testName)) {
                    logger.info("{} - {}", testName, duration);
                }
                testsDuration.put(testName, duration);
            }
        };
    }

    public Map<String, Long> getTestDuration() {
        return testsDuration;
    }
}
