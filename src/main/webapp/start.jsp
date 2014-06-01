<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>Bookmark Page</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/common.css">
</head>

<body>
    <c:set var="ID" value="${sessionScope.id}"/>

                    Id: ${ID}  <br>
                    Nickname: ${applicationScope.id2nick[ID]}
    <c:choose>
        <c:when test="${not empty sessionScope.registered}">
            <div>
                    Now you may find some game if there is also some waiting opponents.
            </div>
            <div>
                <form action="" method="POST">
                    <p><input type="submit" name="retry" value="Check..."></p>
                    <p><input type="hidden" name="action" value="retry"></p>
                </form>
            </div>
        </c:when>

        <c:otherwise>
            <div>
                Register to find a game.
            </div>
            <div>
                <form action="" method="POST">
                   <p>You nickname: <input type="text" name="nickname" value="${sessionScope.nickname}" autofocus></p>
                   <p><input type="submit" name="find" value="Register"></p>
                   <p><input type="hidden" name="action" value="find"></p>
               </form>
            </div>
        </c:otherwise>
    </c:choose>
</body>
</html>