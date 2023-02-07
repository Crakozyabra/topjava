<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="ru">
    <head>
        <title>Title</title>
        <meta charset="utf-8">
    </head>
    <body>
        <h1>Meals</h1>
        <a href="${basePath}${applicationContext}/meals?action=create">Add Meal</a>
        <table border="1">
            <caption>Meal with calories</caption>
            <tr>

                <th>Date and time</th>
                <th>Description</th>
                <th>Calories</th>
                <th></th>
                <th></th>
            </tr>
            <c:forEach var="meal" items="${requestScope.mealTos}">

                <c:choose>
                    <c:when test="${meal.excess}">
                        <tr style="color: red">
                    </c:when>
                    <c:otherwise>
                        <tr style="color: green">
                    </c:otherwise>
                </c:choose>


                    <fmt:parseDate value="${meal.dateTime}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both" />
                    <td><fmt:formatDate pattern="dd-MM-yyyy HH:mm" value="${ parsedDateTime }" /></td>
                    <td>${meal.description}</td>
                    <td>${meal.calories}</td>
                    <td><a href="${basePath}${applicationContext}/meals?action=update&id=${meal.id}">Update</a></td>
                    <td><a href="${basePath}${applicationContext}/meals?action=delete&id=${meal.id}">Delete</a></td>
                </tr>

            </c:forEach>
        </table>
    </body>
</html>