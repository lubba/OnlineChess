<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Bookmark Page</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/common.css">
</head>

<body>
    <c:set var="user" value="${sessionScope.user}"/>

    <div>
        ${user.game.prettyJspField}
    </div>

    <c:if test="${empty user.game.lastTurnInfo}">
        Game started!
    </c:if>

    <div>
        ${user.game.lastTurnInfo.log}
    </div>

    <c:choose>
        <c:when test="${user.id == user.game.actor.id}">
            Your turn.
            <c:if test="${not empty error}">
                <div class="error">
                    ${error}
                </div>
            </c:if>
            <div>
                <form action="" method="POST">
                    <p><input type="text" name="from" value="" autofocus>From<br>
                    <input type="text" name="to" value="">To</p>
                    <p><input type="submit" name="go" value="Go">
                    <input type="hidden" name="action" value="go">
                </form>
            </div>
        </c:when>
        <c:otherwise>
            <div>
                Opponents turn.
                <% response.setIntHeader("Refresh",10); %>
            </div>
        </c:otherwise>
    </c:choose>


    <p>
        Turn <i>#${user.game.turnCount}</i><br>
        White: <b>${user.game.players[0].login}</b><br>
        Black: <b>${user.game.players[1].login}</b>
    </p>

</body>
</html>