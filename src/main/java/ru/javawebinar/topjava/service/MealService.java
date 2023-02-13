package ru.javawebinar.topjava.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealService {

    private static final Logger log = LoggerFactory.getLogger(MealService.class);

    private MealRepository repository;

    @Autowired
    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public MealTo create(Meal meal, int userId, int caloriesPerDay) {
        log.info("mealTo: {}; userId {}; caloriesPerDay {}", meal, userId, caloriesPerDay);
        Meal createdMeal = repository.save(meal, userId);
        log.info("created meal: {}", createdMeal);
        return MealsUtil.createTo(createdMeal, false);
    }

    public void delete(int id, int userId) {
        log.info("start id: {}; userId: {}", id, userId);
        checkNotFoundWithId(repository.delete(id, userId), id);
    }

    public MealTo get(int id, int userId, int caloriesPerDay) {
        log.info("start id: {}; userId: {}; caloriesPerDay: {}", id, userId, caloriesPerDay);
        return MealsUtil.createTo(checkNotFoundWithId(repository.get(id, userId), id), false);
    }

    public List<MealTo> getAll(int userId, int caloriesPerDay) {
        log.info("start userId: {}; caloriesPerDay: {}", userId, caloriesPerDay);
        return MealsUtil.getTos(repository.getAll(userId), caloriesPerDay);
    }

    public List<MealTo> getAllFiltered(LocalDate dateFrom, LocalDate dateTo, LocalTime timeFrom, LocalTime timeTo, int userId, int caloriesPerDay) {
        log.info("dateFrom: {}; dateTo: {}, timeFrom: {}, timeTo: {}, userId: {}, caloriesPerDay: {}", dateFrom, dateTo, timeFrom, timeTo, userId, caloriesPerDay);
        return MealsUtil.getTos(repository.getAllFiltered(dateFrom, dateTo, timeFrom, timeTo, userId), caloriesPerDay);
    }

    public void update(Meal meal, int id, int userId) {
        log.info("start mealTo: {}; id: {}; userId: {}", meal, id, userId);
        checkNotFoundWithId(repository.update(meal, id, userId), id);
    }
}