package ru.javawebinar.topjava;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.web.SecurityUtil;
import ru.javawebinar.topjava.web.meal.MealRestController;

import java.util.Arrays;

public class SpringMain {
    public static void main(String[] args) {
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames())); // печатается список бинов
            MealRestController mealRestController = (MealRestController) appCtx.getBean("mealRestController");
            mealRestController.getAll(SecurityUtil.authUserId(), SecurityUtil.authUserCaloriesPerDay()).forEach(System.out::println); // печатается список еды
        }
    }
}
