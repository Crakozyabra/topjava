package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
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
    private final AtomicInteger counter;

    public InMemoryMealRepository() {
        repository = new ConcurrentHashMap<>();
        counter = new AtomicInteger(0);
        MealsUtil.meals.forEach(meal -> this.save(meal, 1));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal == null) return null;
        log.info("start meal: {}; userId: {}", meal, userId);
        meal.setId(counter.incrementAndGet());
        repository.put(meal.getId(), meal);
        return meal;
    }

    @Override
    public Meal update(Meal meal, int id, int userId) {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        Meal updatedMeal = repository.computeIfPresent(id, (findedId, findedMeal) -> {
            if (findedMeal.getUserId().equals(userId)) {
                atomicBoolean.set(true);
                return meal;
            } else {
                atomicBoolean.set(false);
                return findedMeal;
            }
        });
        return atomicBoolean.get() ? updatedMeal : null;
    }

    @Override
    public boolean delete(int id, int userId) {
        log.info("start id: {}; userId: {}", id, userId);
        Meal meal = repository.get(id);
        log.info("meal for delete: {}", meal);
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
        Comparator<Meal> mealComparator = (meal1, meal2) -> {
            int mealCompare = -meal1.getDate().compareTo(meal2.getDate());
            return mealCompare == 0 ? -meal1.getId().compareTo(meal2.getId()) : mealCompare;
        };
        return repository.values().stream()
                .filter(meal -> meal.getUserId() == userId)
                .sorted(mealComparator)
                .collect(Collectors.toList());
    }

    @Override
    public List<Meal> getAllFiltered(LocalDate dateFrom, LocalDate dateTo, LocalTime timeFrom, LocalTime timeTo, int userId) {
        log.info("dateFrom: {}; dateTo: {}, timeFrom: {}, timeTo: {}, userId: {}", dateFrom, dateTo, timeFrom, timeTo, userId);
        return getAll(userId).stream()
                .filter(meal -> DateTimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalDate(), dateFrom, dateTo))
                .filter(meal -> {
                    if (meal.getDateTime().toLocalTime().equals(timeTo)) return false;
                    return DateTimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), timeFrom, timeTo);
                })
                .collect(Collectors.toList());
    }
}

