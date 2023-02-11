package ru.javawebinar.topjava.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.inmemory.InMemoryMealRepository;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealService {

    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepository.class);

    private MealRepository repository;

    @Autowired
    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public MealTo create(MealTo mealTo, int userId, int caloriesPerDay) {
        log.info("mealTo for create: {}", mealTo);
        Meal meal = new Meal(mealTo.getId(), mealTo.getDateTime(), mealTo.getDescription(), mealTo.getCalories(), userId);
        Meal createdMeal = repository.save(meal, userId);
        log.info("created meal: {}", createdMeal);
        return createdMeal == null ? null : MealsUtil.createTo(createdMeal, caloriesSumPerDay(createdMeal) > caloriesPerDay);
    }

    public void delete(int id, int userId) {
        checkNotFoundWithId(repository.delete(id, userId), id);
    }

    public MealTo get(int id, int userId, int caloriesPerDay) {
        Meal meal = repository.get(id, userId);
        return MealsUtil.createTo(checkNotFoundWithId(meal, id), caloriesSumPerDay(meal) > caloriesPerDay);
    }

    public List<MealTo> getAll(int userId, int caloriesPerDay) {
        log.info("start");
        repository.getAll(userId).forEach(System.out::println);
        return MealsUtil.getTos(repository.getAll(userId), caloriesPerDay);
    }

    public void update(MealTo mealTo, int userId) {
        log.info("mealTo for update: {}", mealTo);
        Meal mealForUpdate = new Meal(mealTo.getId(), mealTo.getDateTime(), mealTo.getDescription(), mealTo.getCalories(), userId);
        checkNotFoundWithId(repository.save(mealForUpdate, userId), mealForUpdate.getUserId());
    }

    private int caloriesSumPerDay(Meal meal) {
        return repository.getAll(meal.getUserId()).stream().filter(streamedMeal -> meal.getDate().equals(streamedMeal.getDate())).mapToInt(Meal::getCalories).sum();
    }

}