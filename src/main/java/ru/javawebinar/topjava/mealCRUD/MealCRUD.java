package ru.javawebinar.topjava.mealCRUD;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

public interface MealCRUD {
    public void create(LocalDateTime localDateTime, String description, int calories);
    public List<Meal> readAll();
    public void updateById(Integer id, LocalDateTime localDateTime, String description, int calories);
    public void deleteById(Integer id);
    public Meal getById(Integer id);
}
