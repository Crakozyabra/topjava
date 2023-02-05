<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ru">
    <head>
    <title>Title</title>
</head>
    <body>
        <h1>Add meal</h1>
        <form method="post" action="/topjava/">
            <input type="hidden" name="formName" value="createMealForm"/>
            <table>
                <tr>
                    <td>DateTime:</td>
                    <td><input name="dateTime" type="datetime-local"/></td>
                </tr>

                <tr>
                    <td>Description:</td>
                    <td><input name="description" type="text"/></td>
                </tr>

                <tr>
                    <td>Calories:</td>
                    <td><input name="calories" type="number"/></td>
                </tr>

                <tr>
                    <td>
                        <input type="submit" value="Save"/>
                        <button><a  href="http://localhost:8080/topjava/">Cancel</a></button>
                    </td>
                </tr>
            </table>
        </form>
    </body>
</html>
