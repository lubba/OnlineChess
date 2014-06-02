<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>Bookmark Page</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/common.css">
</head>

<body>
<c:set var="Game" value="${sessionScope.user.game}"/>

    <div>
        <form action="" method="POST">
            <p><input type="text" name="from" value="From"><br>
            <input type="text" name="to" value="To"></p>
            <p><input type="submit" name="go" value="Go">
            <input type="hidden" name="action" value="makeTurn">
        </form>
    </div>

</body>
</html>