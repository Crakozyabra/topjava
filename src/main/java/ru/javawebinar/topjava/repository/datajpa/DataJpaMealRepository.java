package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class DataJpaMealRepository implements MealRepository {

    private final CrudMealRepository crudRepository;

    private final CrudUserRepository crudUserRepository;

    public DataJpaMealRepository(CrudMealRepository crudRepository, CrudUserRepository crudUserRepository) {
        this.crudRepository = crudRepository;
        this.crudUserRepository = crudUserRepository;
    }

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            Optional<User> optionalUser = crudUserRepository.findById(userId);
            User user = optionalUser.filter(u -> u.getId() == userId).orElse(null);
            if (user == null) {
                return null;
            }
            meal.setUser(user);
            return crudRepository.save(meal);
        }

        Meal mealWithUser = crudRepository.getWithUser(meal.getId(), userId);
        if (Objects.isNull(mealWithUser)) {
            return null;
        }
        meal.setUser(mealWithUser.getUser());
        return crudRepository.save(meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        return crudRepository.delete(id, userId) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        Optional<Meal> optionalMeal = crudRepository.findById(id);
        if (optionalMeal.isEmpty()) {
            return null;
        }
        Meal meal = optionalMeal.get();
        if (meal.getUser().getId() != userId) {
            return null;
        }
        return meal;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return crudRepository.getAll(userId);
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return crudRepository.getBetweenHalfOpen(startDateTime, endDateTime, userId);
    }

    @Override
    public Meal getWithUser(int id, int userId) {
        return crudRepository.getWithUser(id, userId);
    }
}
