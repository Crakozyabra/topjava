package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.mealCRUD.MealCRUD;
import ru.javawebinar.topjava.mealCRUD.MealCRUDInMemory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {

    private MealCRUD mealCRUD;

    private static final Logger log = getLogger(MealServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        log.trace("start");
        String page = req.getParameter("page");
        if ("create_meal".equals(page)) {
            log.trace("create_meal");
            req.getRequestDispatcher("/create_meal.jsp").forward(req,resp);
        } else if ("update_meal".equals(page)) {
            log.trace("update_meal");
            log.trace("meal id for update: {}", req.getParameter("meal_id"));

            int id = 1;
            try{
                id = Integer.parseInt(req.getParameter("meal_id"));
            } catch (Exception e) {
                e.printStackTrace();
                log.trace("exception parse id. id: {}", id);
            }

            Meal meal = mealCRUD.getById(id);

            req.setAttribute("meal",meal);
            req.getRequestDispatcher("/update_meal.jsp").forward(req,resp);
        } else if ("delete_meal".equals(page)) {
            log.trace("delete_meal");
            log.trace("meal id for delete: {}", req.getParameter("meal_id"));
            req.setAttribute("meal_id",req.getParameter("meal_id"));

            int id = 1;
            try{
                id = Integer.parseInt(req.getParameter("meal_id"));
            } catch (Exception e) {
                e.printStackTrace();
                log.trace("exception parse id. id: {}", id);
            }
            mealCRUD.deleteById(id);
            List<Meal> meals = (List<Meal>) getServletContext().getAttribute("meals");
            List<MealTo> mealTos = mealToMealTo(meals);
            req.setAttribute("mealTos", mealTos);
            req.getRequestDispatcher("/meals.jsp").forward(req, resp);
        } else {

            List<Meal> meals;
            try {
                meals = mealCRUD.readAll();
                List<MealTo> mealTos = mealToMealTo(meals);
                req.setAttribute("mealTos", mealTos);
            } catch (Exception e) {
                e.printStackTrace();
            }

            req.getRequestDispatcher("/meals.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        log.trace("start");
        String formName = req.getParameter("formName");
        int id = 1;
        String description = "";
        LocalDateTime localDateTime;
        int calories = 0;
        if ("updateMealForm".equals(formName)) {

            try{
                log.trace("meal id for update: {}", req.getParameter("mealId"));
                id = Integer.parseInt(req.getParameter("mealId"));
            } catch (Exception e) {
                e.printStackTrace();
                id = 1;
                log.trace("exception parse id. id: {}", id);
            }

            try {
                log.trace("dateTime: {}",req.getParameter("dateTime"));
                localDateTime = LocalDateTime.parse(req.getParameter("dateTime"));
            } catch (Exception e) {
                e.printStackTrace();
                localDateTime = LocalDateTime.now();
                log.trace("exception parse localDateTime. lacalDateTime: {}", localDateTime);
            }

            try {
                log.trace("description: {}", req.getParameter("description"));
                description = req.getParameter("description");
            } catch (Exception e) {
                e.printStackTrace();
                description = "";
                log.trace("exception parse description. description: {}", description);
            }

            try {
                calories = Integer.parseInt(req.getParameter("calories"));
            } catch (Exception e) {
                e.printStackTrace();
                log.trace("exception parse calories. calories: {}", 0);
            }

            mealCRUD.updateById(id,localDateTime,description,calories);
            List<Meal> meals = (List<Meal>) getServletContext().getAttribute("meals");
            List<MealTo> mealTos = mealToMealTo(meals);
            req.setAttribute("mealTos", mealTos);
            req.getRequestDispatcher("/meals.jsp").forward(req, resp);

        } else if ("createMealForm".equals(formName)) {

            try {
                log.trace("dateTime: {}",req.getParameter("dateTime"));
                localDateTime = LocalDateTime.parse(req.getParameter("dateTime"));
            } catch (Exception e) {
                e.printStackTrace();
                localDateTime = LocalDateTime.now();
                log.trace("exception parse localDateTime. localDateTime: {}", localDateTime);
            }

            try {
                log.trace("description: {}", req.getParameter("description"));
                description = req.getParameter("description");
            } catch (Exception e) {
                e.printStackTrace();
                description = "";
                log.trace("exception parse description. description: {}", description);
            }

            try {
                calories = Integer.parseInt(req.getParameter("calories"));
            } catch (Exception e) {
                e.printStackTrace();
                log.trace("exception parse calories. calories: {}", 0);
            }


            mealCRUD.create(localDateTime,description,calories);
            List<Meal> meals = (List<Meal>) getServletContext().getAttribute("meals");
            List<MealTo> mealTos = mealToMealTo(meals);
            req.setAttribute("mealTos", mealTos);
            req.getRequestDispatcher("/meals.jsp").forward(req, resp);
        }
    }


    @Override
    public void init() throws ServletException {

        CopyOnWriteArrayList<Meal> meals = new CopyOnWriteArrayList<Meal>() {{
            add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));
            add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000));
            add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500));
            add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
            add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000));
            add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500));
            add(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));
        }};

        for (int i = 1; i <= meals.size(); i++) {
            meals.get(i-1).setId(i);
        }

        try {
            getServletContext().setAttribute("meals", meals);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mealCRUD = new MealCRUDInMemory(getServletContext());

    }


    public List<MealTo> mealToMealTo(List<Meal> meals) {
        return MealsUtil.filteredByStreams(meals, LocalTime.MIN,LocalTime.MAX, 2000);
    }
}
