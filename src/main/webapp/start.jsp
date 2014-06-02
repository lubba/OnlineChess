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

    <a href="/chessonline/users">Registered users</a>

</body>
</html>