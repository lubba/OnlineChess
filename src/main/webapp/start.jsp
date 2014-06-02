<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>Bookmark Page</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/common.css">
</head>

<body>
    <c:set var="ID" value="${sessionScope.id}"/>
    Hello, <b>${sessionScope.user.login}</b>!

    <form action="/chessonline/start" method="POST">
        <input type="submit" value="Find!">
        <input type="hidden" name="action" value="find">
    </form>
    <form action="/chessonline/start" method="POST">
            <input type="submit" value="Log out">
            <input type="hidden" name="action" value="logout">
    </form>
    <c:if test="searching==1">
        <p>
            <table>
                    <tr>
                        <td>Id</td>
                        <td>Login</td>
                        <td>State</td>
                    </tr>
                    <c:forEach var="user" items="${applicationScope.accounts}">
                        <c:if test="${user.state.name==SEARCHING}">
                            <tr>
                                <td>${user.id}</td>
                                <td>${user.login}</td>
                                <td>${user.state}</td>
                            </tr>
                        </c:if>
                    </c:forEach>
            </table>
        </p>
    </c:if>
    <a href="/chessonline/users">Registered users</a>
</body>
</html>