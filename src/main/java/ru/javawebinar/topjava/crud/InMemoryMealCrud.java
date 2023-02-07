package ru.javawebinar.topjava.crud;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static org.slf4j.LoggerFactory.getLogger;

public class InMemoryMealCrud implements MealCrud {

    private static final Logger log = getLogger(InMemoryMealCrud.class);

    private Map<Integer, Meal> meals;

    private AtomicInteger lastId;

    public InMemoryMealCrud() {
        this.meals = new ConcurrentHashMap<>();
        lastId = new AtomicInteger(0);
    }

    @Override
    public Meal create(Meal meal) {
        log.trace("start");
        Integer id = lastId.incrementAndGet();
        meal.setId(id);
        meals.put(id, meal);
        return meal;
    }

    @Override
    public List<Meal> readAll() {
        return new CopyOnWriteArrayList<>(meals.values());
    }

    @Override
    public Meal updateById(Meal meal) {
        log.trace("meal for update: {}", meal);
        if (meal == null) return null;
        Meal updatedMeal = meals.merge(meal.getId(), meal, (oldValue, newValue) -> {
            oldValue.setDateTime(newValue.getDateTime());
            oldValue.setDescription(newValue.getDescription());
            oldValue.setCalories(newValue.getCalories());
            return oldValue;
        });
        log.trace("updated meal: {}", updatedMeal);
        return updatedMeal;
    }

    @Override
    public void deleteById(int id) {
        meals.remove(id);
    }

    @Override
    public Meal getById(int id) {
        return meals.get(id);
    }
}
