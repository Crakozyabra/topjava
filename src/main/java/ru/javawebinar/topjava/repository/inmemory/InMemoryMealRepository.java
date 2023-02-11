package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepository.class);
    private final Map<Integer, Meal> repository;
     private final AtomicInteger counter;

    public InMemoryMealRepository() {
        repository = new ConcurrentHashMap<>();
        counter = new AtomicInteger(0);
        MealsUtil.meals.forEach(meal -> this.save(meal, 1));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal == null) return null;
        log.info("meal for save: {}", meal);
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            //meal.setUserId(userId);
            repository.put(meal.getId(), meal);
            return meal;
        }

        Meal updatedMeal = repository.computeIfPresent(meal.getId(), (id, oldMeal) -> {
            if (oldMeal.getUserId().equals(meal.getUserId())) {
                return meal;
            } else return oldMeal;
        });

        return meal.equals(updatedMeal) ? meal : null;
    }

    @Override
    public boolean delete(int id, int userId) {
        Meal meal = repository.get(id);
        log.info("meal for delete: {}", meal);
        if (meal != null) {
            if (meal.getUserId() == userId) {
                repository.remove(id);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public Meal get(int id, int userId) {
        Meal meal = repository.get(id);
        log.info(meal == null ? null : meal.toString());
        return meal.getUserId() == userId ? meal : null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        log.info("start");
        return repository.values().stream().
                filter(meal -> meal.getUserId() == userId).
                sorted((meal1, meal2) -> -meal1.getDate().compareTo(meal2.getDate())).
                collect(Collectors.toList());
    }
}

