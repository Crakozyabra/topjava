<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ru">
    <head>
    <title>Title</title>
</head>
    <body>
        <h1>${headerName}</h1>
        <form method="post" action="${applicationContext}/meals">
            <input type="hidden" name="formName" value="${formName}"/>
            <input type="hidden" name="mealId" value="${meal.id}"/>
            <table>
                <tr>
                    <td>DateTime:</td>
                    <td>
                        <input name="dateTime" type="datetime-local" value="${dateTime}${meal.dateTime}"/>
                    </td>
                </tr>

                <tr>
                    <td>Description:</td>
                    <td><input name="description" type="text" value="${meal.description}"/></td>
                </tr>

                <tr>
                    <td>Calories:</td>
                    <td><input name="calories" type="number" value="${meal.calories}"/></td>
                </tr>

                <tr>
                    <td>
                        <input type="submit" value="${actionName}"/>
                        <button><a  href="${basePath}${applicationContext}/meals">Cancel</a></button>
                    </td>
                </tr>
            </table>
        </form>
    </body>
</html>
