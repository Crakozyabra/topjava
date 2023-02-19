package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int USER_MEAL_ID = START_SEQ + 10;
    public static final int ADMIN_MEAL_ID = START_SEQ + 20;
    public static final int NOT_FOUND_ID = START_SEQ + 1000;

    public static final Meal[] USER_MEALS = new Meal[]{

            new Meal(100009, LocalDateTime.of(2020, 1, 31, 20, 0), "Ужин (User)", 410),
            new Meal(100008, LocalDateTime.of(2020, 1, 31, 13, 0), "Обед (User)", 500),
            new Meal(100007, LocalDateTime.of(2020, 1, 31, 10, 0), "Завтрак (User)", 1000),
            new Meal(100006, LocalDateTime.of(2020, 1, 31, 0, 0), "Еда на граничное значение (User)", 100),
            new Meal(100005, LocalDateTime.of(2020, 1, 30, 20, 0), "Ужин (User)", 500),
            new Meal(100004, LocalDateTime.of(2020, 1, 30, 13, 0), "Обед (User)", 1000),
            new Meal(100003, LocalDateTime.of(2020, 1, 30, 10, 0), "Завтрак (User)", 500),
            new Meal(100016, LocalDateTime.of(2020, 1, 29, 20, 0), "Ужин (User)", 410),
            new Meal(100015, LocalDateTime.of(2020, 1, 29, 13, 0), "Обед (User)", 500),
            new Meal(100014, LocalDateTime.of(2020, 1, 29, 10, 0), "Завтрак (User)", 1000),
            new Meal(100013, LocalDateTime.of(2020, 1, 29, 0, 0), "Еда на граничное значение (User)", 100),
            new Meal(100012, LocalDateTime.of(2020, 1, 28, 20, 0), "Ужин (User)", 500),
            new Meal(100011, LocalDateTime.of(2020, 1, 28, 13, 0), "Обед (User)", 1000),
            new Meal(100010, LocalDateTime.of(2020, 1, 28, 10, 0), "Завтрак (User)", 500)
    };

    public static final Meal[] ADMIN_MEALS = new Meal[]{
            new Meal(100023, LocalDateTime.of(2020, 1, 31, 20, 0), "Ужин (Admin)", 410),
            new Meal(100022, LocalDateTime.of(2020, 1, 31, 13, 0), "Обед (Admin)", 500),
            new Meal(100021, LocalDateTime.of(2020, 1, 31, 10, 0), "Завтрак (Admin)", 1000),
            new Meal(100020, LocalDateTime.of(2020, 1, 31, 0, 0), "Еда на граничное значение (Admin)", 100),
            new Meal(100019, LocalDateTime.of(2020, 1, 30, 20, 0), "Ужин (Admin)", 500),
            new Meal(100018, LocalDateTime.of(2020, 1, 30, 13, 0), "Обед (Admin)", 1000),
            new Meal(100017, LocalDateTime.of(2020, 1, 30, 10, 0), "Завтрак (Admin)", 500),
            new Meal(100030, LocalDateTime.of(2020, 1, 29, 20, 0), "Ужин (Admin)", 410),
            new Meal(100029, LocalDateTime.of(2020, 1, 29, 13, 0), "Обед (Admin)", 500),
            new Meal(100028, LocalDateTime.of(2020, 1, 29, 10, 0), "Завтрак (Admin)", 1000),
            new Meal(100027, LocalDateTime.of(2020, 1, 29, 0, 0), "Еда на граничное значение (Admin)", 100),
            new Meal(100026, LocalDateTime.of(2020, 1, 28, 20, 0), "Ужин (Admin)", 500),
            new Meal(100025, LocalDateTime.of(2020, 1, 28, 13, 0), "Обед (Admin)", 1000),
            new Meal(100024, LocalDateTime.of(2020, 1, 28, 10, 0), "Завтрак (Admin)", 500)
    };

    public static Meal getNew() {
        return new Meal(LocalDateTime.MIN, "New meal", 100);
    }

    public static Meal getUpdate() {
        return new Meal(LocalDateTime.MIN, "Update meal", 300);
    }

    public static Meal getSaved() {
        return new Meal(USER_MEAL_ID, LocalDateTime.of(2020, 1, 28, 10, 0), "Завтрак (User)", 500);
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).isEqualTo(expected);
    }
}
