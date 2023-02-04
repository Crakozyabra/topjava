<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Title</title>
    </head>
    <body>
        <h1>Meals</h1>
        <a href="http://localhost:8080/topjava?page=create_meal">Add Meal</a>
        <table border="1">
            <caption>Meal with calories</caption>
            <tr>
                <th>#</th>
                <th>Date and time</th>
                <th>Description</th>
                <th>Calories</th>
                <th></th>
                <th></th>
            </tr>
            <c:forEach var="meal" items="${requestScope.mealTos}">

                <c:if test="${meal.excess eq true}">
                    <tr style="color: red">
                </c:if>

                <c:if test="${meal.excess eq false}">
                    <tr style="color: green">
                </c:if>

                    <td>${meal.id}</td>
                    <fmt:parseDate value="${meal.dateTime}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both" />
                    <td><fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${ parsedDateTime }" /></td>
                    <td>${meal.description}</td>
                    <td>${meal.calories}</td>
                    <td><a href="http://localhost:8080/topjava?page=update_meal&meal_id=${meal.id}">Update</a></td>
                    <td><a href="">Delete</a></td>
                </tr>

            </c:forEach>
        </table>
    </body>
</html>