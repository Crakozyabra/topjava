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

import static org.junit.Assert.*;
import static ru.javawebinar.topjava.MealTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    @Autowired
    MealService service;

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
    public void createDuplicatePairOfUserIdAndDateTime() {
        service.create(getNew(), USER_ID);
        assertThrows(DuplicateKeyException.class, () -> service.create(getNew(), USER_ID));
    }

    @Test
    public void get() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND_ID, USER_ID));
        assertThrows(NotFoundException.class, () -> service.get(USER_MEAL_ID, NOT_FOUND_ID));
        assertThrows(NotFoundException.class, () -> service.get(ADMIN_MEAL_ID, USER_ID));
        assertThrows(NotFoundException.class, () -> service.get(USER_MEAL_ID, ADMIN_ID));
    }

    @Test
    public void delete() {
        service.delete(USER_MEAL_ID, USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(USER_MEAL_ID, USER_ID));
        assertThrows(NotFoundException.class, () -> service.delete(USER_MEAL_ID, ADMIN_ID));
        assertThrows(NotFoundException.class, () -> service.delete(ADMIN_MEAL_ID, USER_ID));
    }

    @Test
    public void getAll() {
        List<Meal> userMeals = service.getAll(USER_ID);
        List<Meal> adminMeals = service.getAll(ADMIN_ID);
        assertMatch(userMeals, USER_MEALS);
        assertMatch(adminMeals, ADMIN_MEALS);
    }

    @Test
    public void update() {
        Meal adminMeal = getUpdate();
        adminMeal.setId(ADMIN_MEAL_ID);
        Meal userMeal = getUpdate();
        userMeal.setId(USER_MEAL_ID);
        assertThrows(NotFoundException.class, () -> service.update(adminMeal, USER_ID));
        assertThrows(NotFoundException.class, () -> service.update(userMeal, ADMIN_ID));
    }


}