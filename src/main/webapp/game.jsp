<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>Bookmark Page</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/common.css">
</head>

<body>
<c:set var="ID" value="${sessionScope.id}"/>
<c:set var="Game" value="${applicationScope.id2game[ID]}"/>
<c:set var="Field" value="${Game.prettyField}"/>
<c:set var="Nick" value="${applicationScope.id2nick[ID]}"/>
<c:set var="Color" value="${applicationScope.colors[ID]}"/>
<c:set var="actorId" value="${Game['PAWN']}"/>
<c:set var="TurnInfo" value="${Game.lastTurnInfo}"/>

    <p>
    <div>
    |♜|♞|♝|♛|♚|♝|♞|♜|<br>
    |♟|♟|♟|♟|♟|♟|♟|♟|<br>
    |＿|＿|＿|＿|＿|＿|＿|＿|<br>
    |＿|＿|＿|＿|＿|＿|＿|＿|<br>
    |＿|＿|＿|＿|♙|＿|＿|＿|<br>
    |＿|＿|＿|＿|＿|＿|＿|＿|<br>
    |♙|♙|♙|♙|＿|♙|♙|♙|<br>
    |♖|♘|♗|♕|♔|♗|♘|♖|<br>

        ${Field}
    </div>
    </p>
    <div>
    Your turn!
        <div>
                    <form action="" method="POST">
                        <p><input type="text" name="from" value="From"><br>
                        <input type="text" name="to" value="To"></p>
                        <p><input type="submit" name="go" value="Go">
                        <input type="hidden" name="action" value="makeTurn">
                    </form>
                </div>
    </div>
 <div><!-- User info -->Nick: Petya <br>
 Color: ${applicationScope.colorsStrings[Color]} <br>
 Turn №2${Game.TurnCount}</div>
 <br>   <div>Your opponent: Vasya
    <c:if test="${not empty TurnInfo.error}">
        <div class="error">${TurnInfo.error}</div>
    </c:if>
    <div>${TurnInfo.log} <c:if test="${TurnInfo.isCheck}"> CHECK!</c:if></div>

    <div><form action="" method="post">
    <br>
    Game ID: 1
    <input type="submit" value="Save!"
    <input type="hidden" name="action" value="save"></form><div>
        ${Game.history}
    </div>
    <div><form action="" method="post">
    <input value="ID">
    <input type="submit" value="Load">
    <input type="hidden" name="action" value="load"></form>
</body>
</html>