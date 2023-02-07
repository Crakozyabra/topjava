package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.mealcrud.InMemoryMealCrud;
import ru.javawebinar.topjava.mealcrud.MealCrud;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {

    private static final Logger log = getLogger(MealServlet.class);

    private final static int CALORIES_PER_DAY = 2000;

    private MealCrud mealCrud;

    @Override
    public void init() throws ServletException {
        mealCrud = new InMemoryMealCrud();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        log.trace("start");
        String action = req.getParameter("action") == null?"":req.getParameter("action");
        int id = 0;
        switch (action) {
            case "create":
                log.trace("create_meal");
                LocalDateTime localDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.now());
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                log.trace(localDateTime.format(dateTimeFormatter));
                req.setAttribute("actionName", "add");
                req.setAttribute("headerName", "Add Meal");
                req.setAttribute("formName", "createMealForm");
                req.setAttribute("dateTime", localDateTime.format(dateTimeFormatter));
                req.setAttribute("basePath", req.getScheme() + "://" + req.getHeader("Host"));
                req.setAttribute("applicationContext", req.getContextPath());
                req.getRequestDispatcher("create_update_meal.jsp").forward(req, resp);
            break;
            case "update":
                log.trace("update_meal");
                id = Integer.parseInt(req.getParameter("id"));
                Meal meal = mealCrud.getById(id);
                req.setAttribute("actionName", "update");
                req.setAttribute("headerName", "Update Meal");
                req.setAttribute("formName", "updateMealForm");
                req.setAttribute("meal", meal);
                req.setAttribute("basePath", req.getScheme() + "://" + req.getHeader("Host"));
                req.setAttribute("applicationContext", req.getContextPath());
                req.getRequestDispatcher("create_update_meal.jsp").forward(req, resp);
            break;
            case "delete":
                log.trace("delete_meal");
                id = Integer.parseInt(req.getParameter("id"));
                mealCrud.deleteById(id);
                resp.sendRedirect(req.getScheme() + "://" + req.getHeader("Host") + req.getContextPath() + "/meals");
            break;
            default:
                List<MealTo> mealTos = getMealToFromMeal(mealCrud.readAll());
                req.setAttribute("mealTos", mealTos);
                req.setAttribute("basePath", req.getScheme() + "://" + req.getHeader("Host"));
                req.setAttribute("applicationContext", req.getContextPath());
                req.getRequestDispatcher("meals.jsp").forward(req, resp);
            break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        log.trace("start");
        String formName = req.getParameter("formName");
        int id = 0;
        String description = "";
        LocalDateTime localDateTime = null;
        int calories = 0;
        if ("updateMealForm".equals(formName)) {
            log.trace("updateMealForm");
            id = Integer.parseInt(req.getParameter("mealId"));
            localDateTime = LocalDateTime.parse(req.getParameter("dateTime"));
            description = req.getParameter("description");
            calories = Integer.parseInt(req.getParameter("calories"));
            log.trace("parsed values: {}, {}, {}, {}",id,localDateTime,description,calories);
            mealCrud.updateById(id, localDateTime, description, calories);
            resp.sendRedirect(req.getScheme() + "://" + req.getHeader("Host") + req.getContextPath() + "/meals");
        } else if ("createMealForm".equals(formName)) {
            log.trace("createMealForm");
            localDateTime = LocalDateTime.parse(req.getParameter("dateTime"));
            description = req.getParameter("description");
            calories = Integer.parseInt(req.getParameter("calories"));
            mealCrud.create(localDateTime, description, calories);
            resp.sendRedirect(req.getScheme() + "://" + req.getHeader("Host") + req.getContextPath() + "/meals");
        }
    }

    public List<MealTo> getMealToFromMeal(List<Meal> meals) {
        return MealsUtil.filteredByStreams(meals, LocalTime.MIN, LocalTime.MAX, CALORIES_PER_DAY);
    }
}
