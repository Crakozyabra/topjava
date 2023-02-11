package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.DateTimeUtil;
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
import java.util.stream.Collectors;

public class MealServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    private MealRestController mealRestController;

    @Override
    public void init() {
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml");) {
            mealRestController = appCtx.getBean(MealRestController.class);
            mealRestController.getAll(SecurityUtil.authUserId(), SecurityUtil.authUserCaloriesPerDay()).forEach(System.out::println);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");

        if (request.getParameter("formName") != null && "filterEntry".equals(request.getParameter("formName"))) {
            LocalDate dateFrom = LocalDate.parse(request.getParameter("dateFrom"));
            LocalDate dateTo = LocalDate.parse(request.getParameter("dateTo"));
            LocalTime timeFrom = LocalTime.parse(request.getParameter("timeFrom"));
            LocalTime timeTo = LocalTime.parse(request.getParameter("timeTo"));
            log.info("{} {} {} {}", dateFrom, dateTo, timeFrom, timeTo);
            List<MealTo> mealTos = mealRestController.
                    getAll(SecurityUtil.authUserId(), SecurityUtil.authUserCaloriesPerDay()).stream().
                    filter(mealTo -> DateTimeUtil.isBetweenDate(mealTo.getDateTime().toLocalDate(), dateFrom, dateTo)).
                    filter(mealTo -> DateTimeUtil.isBetweenTime(mealTo.getDateTime().toLocalTime(), timeFrom, timeTo)).
                    collect(Collectors.toList());
            request.setAttribute("meals", mealTos);
            log.info(mealTos.stream().map(MealTo::toString).collect(Collectors.joining(",")));
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
        } else if (request.getParameter("formName") != null && "createUpdate".equals(request.getParameter("formName"))) {

            MealTo mealTo = new MealTo(
                    id.isEmpty() ? null : Integer.parseInt(id),
                    LocalDateTime.parse(request.getParameter("dateTime")),
                    request.getParameter("description"),
                    Integer.parseInt(request.getParameter("calories")),
                    false,
                    SecurityUtil.authUserId()
            );

            log.info(mealTo.getId() == null ? "Create {}" : "Update {}", mealTo);
            if (mealTo.getId() == null) {
                mealRestController.create(mealTo, SecurityUtil.authUserId(), SecurityUtil.authUserCaloriesPerDay());
            } else {
                mealRestController.update(mealTo, SecurityUtil.authUserId());
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
                mealRestController.delete(id, SecurityUtil.authUserId());
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
                        mealRestController.get(getId(request), SecurityUtil.authUserId(), SecurityUtil.authUserCaloriesPerDay());
                request.setAttribute("meal", mealTo);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "all":
            default:
                log.info("getAll");
                request.setAttribute("meals",
                        mealRestController.getAll(SecurityUtil.authUserId(), SecurityUtil.authUserCaloriesPerDay()));
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
