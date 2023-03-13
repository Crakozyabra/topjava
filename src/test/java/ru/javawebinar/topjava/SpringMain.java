package ru.javawebinar.topjava;

import org.springframework.beans.factory.config.PropertyOverrideConfigurer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.repository.jdbc.JdbcUserRepository;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.web.meal.MealRestController;
import ru.javawebinar.topjava.web.user.AdminRestController;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class SpringMain {
    public static void main(String[] args) {
        // java 7 automatic resource management (ARM)
        GenericXmlApplicationContext ctx = new GenericXmlApplicationContext();
        ConfigurableEnvironment env = ctx.getEnvironment();
        env.setActiveProfiles("postgres", "jdbc");
        ctx.load("spring/spring-db.xml");
        ctx.refresh();
        List.of(ctx.getBeanDefinitionNames()).forEach(System.out::println);
        UserRepository userRepository = ctx.getBean(UserRepository.class);
        //userRepository.getAll().forEach(System.out::println);
        //System.out.println(userRepository.getByEmail(UserTestData.admin.getEmail()));
        //System.out.println(userRepository.get(UserTestData.USER_ID));
        User user = UserTestData.getNew();
        Set<Role> roles = Set.of(Role.USER, Role.ADMIN);
        user.setRoles(roles);
        userRepository.save(user);
        ctx.close();

        /*
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml", "spring/inmemory.xml")) {
            System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));
            AdminRestController adminUserController = appCtx.getBean(AdminRestController.class);
            adminUserController.create(new User(null, "userName", "email@mail.ru", "password", Role.ADMIN));
            System.out.println();

            MealRestController mealController = appCtx.getBean(MealRestController.class);
            List<MealTo> filteredMealsWithExcess =
                    mealController.getBetween(
                            LocalDate.of(2020, Month.JANUARY, 30), LocalTime.of(7, 0),
                            LocalDate.of(2020, Month.JANUARY, 31), LocalTime.of(11, 0));
            filteredMealsWithExcess.forEach(System.out::println);
            System.out.println();
            System.out.println(mealController.getBetween(null, null, null, null));
            List.of(appCtx.getBeanDefinitionNames()).forEach(System.out::println);
        }
        */

    }
}
