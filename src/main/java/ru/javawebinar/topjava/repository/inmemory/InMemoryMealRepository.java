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
    private final Map<Integer, Meal> repository;
    private final Map<Integer, Map<Integer, Meal>> repositoryMap; // Map<userId, Map<id, Meal>>
    private final AtomicInteger counter;

    public InMemoryMealRepository() {
        repository = new ConcurrentHashMap<>();
        repositoryMap = new ConcurrentHashMap<>();
        counter = new AtomicInteger(0);
        MealsUtil.meals.forEach(this::save);
    }

    @Override
    public Meal save(Meal meal) {
        log.info("start meal: {}", meal);
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            return meal;
        }

        AtomicBoolean wasUpdated = new AtomicBoolean(false);
        Meal updatedMeal = repository.computeIfPresent(meal.getId(), (findedId, findedMeal) -> {
            if (findedMeal.getUserId().equals(meal.getUserId())) {
                wasUpdated.set(true);
                return meal;
            } else {
                wasUpdated.set(false);
                return findedMeal;
            }
        });
        return wasUpdated.get() ? updatedMeal : null;
    }

    @Override
    public boolean delete(int id, int userId) {
        log.info("start id: {}; userId: {}", id, userId);
        Meal meal = repository.get(id);
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        repository.computeIfPresent(id, (findedId, findedMeal) -> {
            if (findedMeal.getUserId().equals(userId)) {
                atomicBoolean.set(true);
                return null;
            } else {
                atomicBoolean.set(false);
                return findedMeal;
            }
        });
        return atomicBoolean.get();
    }

    @Override
    public Meal get(int id, int userId) {
        log.info("start id: {}; userId: {}", id, userId);
        Meal meal = repository.get(id);
        log.info(meal == null ? null : meal.toString());
        return meal.getUserId() == userId ? meal : null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        log.info("start userId: {}", userId);
        Comparator<Meal> comparatorDate = Comparator.comparing(Meal::getDate).reversed();
        Comparator<Meal> comparatorTime = Comparator.comparing(Meal::getTime).reversed();
        Comparator<Meal> comparatorResult = comparatorDate.thenComparing(comparatorTime);
        return repository.values().stream()
                .filter(meal -> meal.getUserId() == userId)
                .sorted(comparatorResult)
                .collect(Collectors.toList());
    }
}

