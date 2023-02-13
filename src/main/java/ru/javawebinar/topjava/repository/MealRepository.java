package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;


public interface MealRepository {

    Meal save(Meal meal, int userId);

    Meal update(Meal meal, int id, int userId);

    boolean delete(int id, int userId);

    Meal get(int id, int userId);

    Collection<Meal> getAll(int userId);

    Collection<Meal> getAllFiltered(LocalDate dateFrom, LocalDate dateTo, LocalTime timeFrom, LocalTime timeTo, int userId);
}
