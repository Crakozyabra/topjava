package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;

import java.util.List;

@Controller
public class MealRestController {
    private static final Logger log = LoggerFactory.getLogger(MealRestController.class);

    private MealService service;

    @Autowired
    public MealRestController(MealService service) {
        this.service = service;
    }

    public MealTo create(MealTo mealTo, int userId, int caloriesPerDay) {
        log.info("meal for save: {}", mealTo.toString());
        return service.create(mealTo, userId, caloriesPerDay);
    }

    public void delete(int id, int userId) {
        service.delete(id, userId);
    }

    public MealTo get(int id, int userId, int caloriesPerDay) {
        return service.get(id, userId, caloriesPerDay);
    }

    public List<MealTo> getAll(int userId, int caloriesPerDay) {
        List<MealTo> mealTos = service.getAll(userId, caloriesPerDay);
        return mealTos;
    }

    public void update(MealTo mealTo, int userId) {
        service.update(mealTo, userId);
    }
}