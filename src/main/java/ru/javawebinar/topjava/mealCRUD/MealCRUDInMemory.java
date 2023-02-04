package ru.javawebinar.topjava.mealCRUD;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;

import javax.servlet.ServletContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

public class MealCRUDInMemory implements MealCRUD{

    private final ServletContext servletContext;

    public MealCRUDInMemory(ServletContext sc) {
        servletContext = sc;
    }

    private static final Logger log = getLogger(MealCRUDInMemory.class);

    @Override
    public void create(LocalDateTime localDateTime, String description, int calories) {
        log.trace("start");
        List<Meal> meals = (List<Meal>) servletContext.getAttribute("meals");
        log.trace("existing meals: {}", meals.stream().map(Meal::toString).collect(Collectors.joining(",")));
        int newId = getNextId(meals);
        log.trace("newId: {}", newId);
        Meal meal = new Meal(localDateTime,description,calories);
        meal.setId(newId);
        log.trace("newMeal: {}", meal.toString());
        meals.add(meal);
        log.trace("after add meal: {}", meals.stream().map(Meal::toString).collect(Collectors.joining(",")));
    }

    @Override
    public List<Meal> readAll() {
        return (List<Meal>) servletContext.getAttribute("meals");
    }

    @Override
    public void updateById(Integer id, LocalDateTime localDateTime, String description, int calories) {
        List<Meal> meals = (List<Meal>) servletContext.getAttribute("meals");
        Meal takedMeal = meals.stream().filter(meal -> meal.getId().equals(id)).findFirst().orElse(null);
        Meal updatedMeal = new Meal(localDateTime,description,calories);
        updatedMeal.setId(id);
        meals.remove(takedMeal);
        meals.add(updatedMeal);
    }

    @Override
    public void deleteById(Integer id) {
        List<Meal> meals = (List<Meal>) servletContext.getAttribute("meals");
        Meal takedMeal = meals.stream().filter(meal -> meal.getId().equals(id)).findFirst().orElse(null);
        meals.remove(takedMeal);
    }

    private int getNextId(List<Meal> meals) {
        return meals.stream().mapToInt(Meal::getId).max().orElse(0) + 1;
    }


}
