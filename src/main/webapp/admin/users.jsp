<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title></title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/common.css">
</head>

<body>
    <table>
        <tr>
            <td>Id</td>
            <td>Login</td>
            <td>State</td>
        </tr>
        <c:forEach var="user" items="${applicationScope.accounts}">
                <tr>
                    <td>${user.id}</td>
                    <td>${user.login}</td>
                    <td>${user.state}</td>
                </tr>
        </c:forEach>
    </table>
</body>

</html>