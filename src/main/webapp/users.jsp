<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="ru">
    <head>
        <title>Users</title>
    </head>
    <body>
        <h3><a href="index.html">Home</a></h3>
        <hr>
        <h2>Users</h2>
        <form method="get" action="users">
            <input type="hidden" name="formName" value="selectUser"/>
            <label>
                <select size="3" name="authUserId">
                    <option disabled>Выберите пользователя</option>
                    <option value="1" ${authUserId eq 1  ? 'selected' : ''}>1</option>
                    <option value="2" ${authUserId eq 2  ? 'selected' : ''}>2</option>
                </select>
            </label>
            <p><input type="submit" value="Select"></p>
        </form>
    </body>
</html>