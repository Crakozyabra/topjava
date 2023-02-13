package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

public class MealServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    private ConfigurableApplicationContext applicationContext;

    private MealRestController mealRestController;

    @Override
    public void init() {
        applicationContext = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        mealRestController = applicationContext.getBean(MealRestController.class);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");

        if (request.getParameter("formName") != null && "createUpdate".equals(request.getParameter("formName"))) {

            Meal meal = new Meal(
                    id.isEmpty() ? null : Integer.parseInt(id),
                    LocalDateTime.parse(request.getParameter("dateTime")),
                    request.getParameter("description"),
                    Integer.parseInt(request.getParameter("calories")),
                    SecurityUtil.authUserId()
            );

            log.info(meal.getId() == null ? "Create {}" : "Update {}", meal);
            if (meal.getId() == null) {
                mealRestController.create(meal);
            } else {
                mealRestController.update(meal, Integer.parseInt(id));
            }
            response.sendRedirect("meals");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                log.info("Delete id={}", id);
                mealRestController.delete(id);
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final MealTo mealTo = "create".equals(action) ?
                        new MealTo(
                                null,
                                LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES),
                                "",
                                1000,
                                false,
                                SecurityUtil.authUserId()
                        )
                        :
                        mealRestController.get(getId(request));
                request.setAttribute("meal", mealTo);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "filter":
                LocalDate dateFrom;
                LocalDate dateTo;
                LocalTime timeFrom;
                LocalTime timeTo;

                try {
                    dateFrom = LocalDate.parse(request.getParameter("dateFrom"));
                } catch (Exception e) {
                    dateFrom = LocalDate.MIN;
                }

                try {
                    dateTo = LocalDate.parse(request.getParameter("dateTo"));
                } catch (Exception e) {
                    dateTo = LocalDate.MAX;
                }

                try {
                    timeFrom = LocalTime.parse(request.getParameter("timeFrom"));
                } catch (Exception e) {
                    timeFrom = LocalTime.MIN;
                }

                try {
                    timeTo = LocalTime.parse(request.getParameter("timeTo"));
                } catch (Exception e) {
                    timeTo = LocalTime.MAX;
                }

                LocalDate effectivelyFinalDateFrom = dateFrom;
                LocalDate effectivelyFinalDateTo = dateTo;
                LocalTime effectivelyFinalTimeFrom = timeFrom;
                LocalTime effectivelyFinalTimeTo = timeTo;

                log.info("{} {} {} {}", dateFrom, dateTo, timeFrom, timeTo);
                List<MealTo> mealTos = mealRestController.getAllFiltered(dateFrom, dateTo, timeFrom, timeTo);
                request.setAttribute("meals", mealTos);
                request.setAttribute("dateFrom", effectivelyFinalDateFrom);
                request.setAttribute("dateTo", effectivelyFinalDateTo);
                request.setAttribute("timeFrom", effectivelyFinalTimeFrom);
                request.setAttribute("timeTo", effectivelyFinalTimeTo);

                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
            case "all":
            default:
                log.info("getAll");
                request.setAttribute("meals",
                        mealRestController.getAll());
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }

    @Override
    public void destroy() {
        applicationContext.close();
    }
}
