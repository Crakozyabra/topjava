package ru.javawebinar.topjava.crud;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealCrud {

    Meal create(Meal meal);

    List<Meal> readAll();

    Meal updateById(Meal meal);

    void deleteById(int id);

    Meal getById(int id);
}
