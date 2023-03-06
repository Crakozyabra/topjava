package ru.javawebinar.topjava.service.datajpa;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserServiceTest;

import static ru.javawebinar.topjava.UserTestData.USER_MATCHER;

@ActiveProfiles(profiles = Profiles.DATAJPA)
public class DataJpaUserServiceTest extends UserServiceTest {
    @Test
    public void getWithMeals() {
        User user = service.getWithMeals(UserTestData.USER_ID);
        Assertions.assertThat(user.getMeals()).containsOnlyOnceElementsOf(MealTestData.meals);
        USER_MATCHER.assertMatch(user, UserTestData.user);
    }

    @Test
    public void getWithMealsEmpty() {
        User user = service.getWithMeals(UserTestData.GUEST_ID);
        Assert.assertTrue(user.getMeals().isEmpty());
    }
}
