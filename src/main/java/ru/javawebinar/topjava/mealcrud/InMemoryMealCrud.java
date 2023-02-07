package ru.javawebinar.topjava.mealcrud;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

public class InMemoryMealCrud implements MealCrud {

    private static final Logger log = getLogger(InMemoryMealCrud.class);

    private CopyOnWriteArrayList<Meal> meals;

    private AtomicInteger lastId;

    public InMemoryMealCrud() {

        this.meals = new CopyOnWriteArrayList<Meal>() {{
            add(new Meal(1, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));
            add(new Meal(2, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000));
            add(new Meal(3, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500));
            add(new Meal(4, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
            add(new Meal(5, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000));
            add(new Meal(6, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500));
            add(new Meal(7, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));
        }};


        lastId = new AtomicInteger(meals.stream().mapToInt(Meal::getId).max().orElse(0));
    }


    @Override
    public void create(LocalDateTime localDateTime, String description, int calories) {
        log.trace("start");
        Meal meal = new Meal(lastId.incrementAndGet(),localDateTime, description, calories);
        meals.add(meal);
        log.trace("after add meal: {}", meals.stream().map(Meal::toString).collect(Collectors.joining(",")));
    }

    @Override
    public List<Meal> readAll() {
        return meals;
    }

    @Override
    public void updateById(Integer id, LocalDateTime localDateTime, String description, int calories) {
        Spliterator<Meal> mealSplitIterator = meals.spliterator();

        while (mealSplitIterator.tryAdvance(meal -> {
            if (Objects.equals(meal.getId(), id)) {
                meal.setDateTime(localDateTime);
                meal.setDescription(description);
                meal.setCalories(calories);
            }
        })){}
    }

    @Override
    public void deleteById(Integer id) {
        Meal takedMeal = meals.stream().filter(meal -> meal.getId().equals(id)).findFirst().orElse(null);
        if (takedMeal != null) {
            meals.remove(takedMeal);
        }
    }

    @Override
    public Meal getById(Integer id) {
        return meals.stream().filter(meal -> meal.getId().equals(id)).findFirst().orElse(null);
    }

}
