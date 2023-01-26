package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO return filtered list with excess. Implement by cycles
        Map<LocalDate, List<UserMeal>> localDateListMap = new HashMap<>();
        for (UserMeal meal:meals) {
            localDateListMap.computeIfAbsent(meal.getDateTime().toLocalDate(), (k) -> {return new ArrayList<>();});
            localDateListMap.computeIfPresent(meal.getDateTime().toLocalDate(), (k,v)-> {
                v.add(meal);
                return v;
            });
        }
        List<UserMealWithExcess> userMealWithExcesses = new ArrayList<>();
        for (Map.Entry<LocalDate,List<UserMeal>> entry:localDateListMap.entrySet()) {
            List<UserMeal> userMeals = entry.getValue();
            boolean isCaloriesExcess = mealCaloriesSum(userMeals) > caloriesPerDay;

            for (UserMeal userMeal:userMeals) {
                if(TimeUtil.isBetweenHalfOpen(userMeal.getDateTime().toLocalTime(), startTime, endTime)) {
                    if (isCaloriesExcess) {
                        userMealWithExcesses.add(userMealToUserMealWithExcess(userMeal, true));
                    } else {
                        userMealWithExcesses.add(userMealToUserMealWithExcess(userMeal, false));
                    }

                }
            }
        }
        return userMealWithExcesses;
    }

    public static int mealCaloriesSum(List<UserMeal> userMeals) {
        int sum = 0;
        for (UserMeal userMeal:userMeals) {
            sum += userMeal.getCalories();
        }
        return sum;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO Implement by streams
        Map<LocalDate, List<UserMeal>> localDateListMap  = meals.stream().collect(Collectors.groupingBy(userMeal -> userMeal.getDateTime().toLocalDate())); // еда группируется по дням
        List<UserMealWithExcess> userMealWithExcesses = localDateListMap.entrySet().stream().
                flatMap(
                        localDateListEntry -> {
                            List<UserMeal> userMealList = localDateListEntry.getValue();
                            Stream.Builder<UserMealWithExcess> userMealWithExcessBuilder = Stream.builder();
                            boolean isCaloriesExcess = userMealList.stream().mapToInt(UserMeal::getCalories).sum() > caloriesPerDay;
                            userMealList.forEach(userMeal -> {
                                if(TimeUtil.isBetweenHalfOpen(userMeal.getDateTime().toLocalTime(), startTime, endTime)) {
                                    if (isCaloriesExcess) {
                                        userMealWithExcessBuilder.add(userMealToUserMealWithExcess(userMeal, true));
                                    } else {
                                        userMealWithExcessBuilder.add(userMealToUserMealWithExcess(userMeal, false));
                                    }
                                }
                            });
                            return userMealWithExcessBuilder.build();
                        }
                ).collect(Collectors.toList());
        return userMealWithExcesses;
    }

    private static UserMealWithExcess userMealToUserMealWithExcess(UserMeal userMeal, boolean isExceess) {
        return new UserMealWithExcess(
                userMeal.getDateTime(),
                userMeal.getDescription(),
                userMeal.getCalories(),
                isExceess
        );
    }
}
