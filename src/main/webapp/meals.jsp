<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
    <head>
        <title>Title</title>
    </head>
    <body>
        <table border="1">
            <caption>Meal with calories</caption>
            <tr>
                <th>Date and time</th>
                <th>Description</th>
                <th>Calories</th>
                <th>Excess</th>
            </tr>
            <c:forEach var="meal" items="${requestScope.meals}">

                <c:if test="${meal.excess eq true}">
                    <tr style="color: red">
                </c:if>

                <c:if test="${meal.excess eq false}">
                    <tr style="color: green">
                </c:if>

                    <fmt:parseDate value="${meal.dateTime}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both" />
                    <td><fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${ parsedDateTime }" /></td>
                    <td>${meal.description}</td>
                    <td>${meal.calories}</td>
                    <td>${meal.excess}</td>

                </tr>

            </c:forEach>
        </table>



    </body>
</html>