<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ru">
    <head>
    <title>Title</title>
</head>
    <body>
        <h1>${not empty meal.id ? 'Update meal' : 'Add meal'}</h1>
        <form method="post" action="${requestScope.get("javax.servlet.forward.context_path")}/meals">
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
                        <input type="submit" value="${not empty meal.id ? 'Update' : 'Add'}"/>
                        <button><a  href="${basePath}${requestScope.get("javax.servlet.forward.context_path")}/meals">Cancel</a></button>
                    </td>
                </tr>
            </table>
        </form>
    </body>
</html>
