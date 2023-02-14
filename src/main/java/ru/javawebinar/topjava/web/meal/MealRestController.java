package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.ValidationUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
public class MealRestController {
    private static final Logger log = LoggerFactory.getLogger(MealRestController.class);

    private MealService service;

    public MealRestController(MealService service) {
        this.service = service;
    }

    public MealTo create(Meal meal) {
        log.info("start meal for save: {}", meal);
        ValidationUtil.checkNew(meal);
        meal.setUserId(SecurityUtil.authUserId());
        return service.create(meal);
    }

    public void delete(int id) {
        log.info("start id: {}", id);
        service.delete(id, SecurityUtil.authUserId());
    }

    public Meal get(int id) {
        log.info("start id: {}", id);
        return service.get(id, SecurityUtil.authUserId(), SecurityUtil.authUserCaloriesPerDay());
    }

    public List<MealTo> getAll() {
        log.info("start");
        return service.getAll(SecurityUtil.authUserId(), SecurityUtil.authUserCaloriesPerDay());
    }

    public void update(Meal meal, int id) {
        log.info("start mealTo {}; id: {}", meal, id);
        ValidationUtil.assureIdConsistent(meal, id);
        service.update(meal, id, SecurityUtil.authUserId());
    }

    public List<MealTo> getAllFiltered(LocalDate dateFrom, LocalDate dateTo, LocalTime timeFrom, LocalTime timeTo) {
        log.info("start dateFrom: {}; dateTo {}; timeFrom {}; timeTo {}", dateFrom, dateTo, timeFrom, timeTo);
        return service.getAllFiltered(dateFrom, dateTo, timeFrom, timeTo, SecurityUtil.authUserId(), SecurityUtil.authUserCaloriesPerDay());
    }
}