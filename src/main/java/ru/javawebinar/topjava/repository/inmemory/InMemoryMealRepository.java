package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepository.class);
    private final Map<Integer, Map<Integer, Meal>> repository; // Map<userId, Map<id, Meal>>
    private final AtomicInteger counter;

    public InMemoryMealRepository() {
        repository = new ConcurrentHashMap<>();
        counter = new AtomicInteger(0);
        MealsUtil.meals.stream().limit(14).forEach(meal -> save(meal, 1));
        MealsUtil.meals.stream().skip(14).limit(14).forEach(meal -> save(meal, 2));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        log.info("start meal: {}", meal);
        repository.compute(userId, (oldUserId, oldMapValue) -> {
            if (meal.isNew()) {
                meal.setId(counter.incrementAndGet());
            }
            if (oldMapValue == null) {
                log.info("such user is absent");
                return new ConcurrentHashMap<Integer, Meal>() {{
                    put(meal.getId(), meal);
                }};
            } else {
                log.info("such user is present");
                oldMapValue.compute(meal.getId(), (oldId, oldMeal) -> {
                    if (oldMeal == null) {
                      return meal;
                    } else {
                      oldMeal.setDateTime(meal.getDateTime());
                      oldMeal.setDescription(meal.getDescription());
                      oldMeal.setCalories(meal.getCalories());
                      return oldMeal;
                    }
                });
                return oldMapValue;
            }
        });
        return meal;
    }

    @Override
    public boolean delete(int id, int userId) {
        log.info("start id: {}; userId: {}", id, userId);
        AtomicBoolean wasDeleted = new AtomicBoolean(false);
        repository.compute(userId, (oldUserId, oldValue) -> {
            if (oldValue == null) {
                return null;
            } else {
                if (oldValue.remove(id) != null) {
                    wasDeleted.set(true);
                }
                return oldValue;
            }
        });
        return wasDeleted.get();
    }

    @Override
    public Meal get(int id, int userId) {
        log.info("start id: {}; userId: {}", id, userId);
        Map<Integer, Meal> mealMap = repository.get(userId);
        return mealMap == null ? null : mealMap.get(id);
    }

    @Override
    public List<Meal> getAll(int userId) {
        log.info("start userId: {}", userId);
        if (repository.get(userId) == null) {
            return null;
        } else {
            return repository.get(userId).values().stream()
                    .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                    .collect(Collectors.toList());
        }
    }
}

