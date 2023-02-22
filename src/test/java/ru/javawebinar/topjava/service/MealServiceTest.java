package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.List;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    @Autowired
    private MealService service;

    @Test
    public void create() {
        Meal created = service.create(getNew(), USER_ID);
        Integer newId = created.getId();
        Meal newMeal = getNew();
        newMeal.setId(newId);
        assertMatch(newMeal, created);
        assertMatch(service.get(newId, USER_ID), newMeal);
    }

    @Test
    public void createDuplicateDateTimePerUser() {
        Meal newMeal = getNew();
        newMeal.setDateTime(EXISTING_USERS_MEAL_DATE_TIME);
        assertThrows(DuplicateKeyException.class, () -> service.create(newMeal, USER_ID));
    }

    @Test
    public void get() {
        Meal meal = service.get(USER_MEAL_ID, USER_ID);
        assertMatch(meal, userMeals[13]);
    }

    @Test
    public void getNotFoundEntry() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND_ID, USER_ID));
        assertThrows(NotFoundException.class, () -> service.get(USER_MEAL_ID, NOT_FOUND_ID));
    }

    @Test
    public void getOtherUserEntry() {
        assertThrows(NotFoundException.class, () -> service.get(ADMIN_MEAL_ID, USER_ID));
        assertThrows(NotFoundException.class, () -> service.get(USER_MEAL_ID, ADMIN_ID));
    }

    @Test
    public void delete() {
        service.delete(USER_MEAL_ID, USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(USER_MEAL_ID, USER_ID));
    }

    @Test
    public void deleteNotFoundEntry() {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND_ID, USER_ID));
    }

    @Test
    public void deleteMealOfOtherUser() {
        assertThrows(NotFoundException.class, () -> service.delete(USER_MEAL_ID, ADMIN_ID));
        assertThrows(NotFoundException.class, () -> service.delete(ADMIN_MEAL_ID, USER_ID));
    }

    @Test
    public void getAllUserMeal() {
        List<Meal> userMeals = service.getAll(USER_ID);
        assertMatch(userMeals, userMeals);
    }

    @Test
    public void getAllAdminMeal() {
        List<Meal> adminMeals = service.getAll(ADMIN_ID);
        assertMatch(adminMeals, adminMeals);
    }

    @Test
    public void update() {
        Meal userMeal = getUpdate();
        userMeal.setId(USER_MEAL_ID);
        service.update(userMeal, USER_ID);
        Meal updatedUserMeal = service.get(USER_MEAL_ID, USER_ID);
        assertMatch(updatedUserMeal, userMeal);
    }

    @Test
    public void updateMealOfOtherUser() {
        Meal adminMeal = getUpdate();
        adminMeal.setId(ADMIN_MEAL_ID);
        assertThrows(NotFoundException.class, () -> service.update(adminMeal, USER_ID));
        Meal userMeal = getUpdate();
        userMeal.setId(USER_MEAL_ID);
        assertThrows(NotFoundException.class, () -> service.update(userMeal, ADMIN_ID));
    }

    @Test
    public void getBetweenHalfOpen() {
        List<Meal> meals = service.getBetweenInclusive(FLOOR_MEAL_DATE_FILTER, CEIL_MEAL_DATE_FILTER, USER_ID);
        assertMatch(meals, filteredMeals);
    }
}