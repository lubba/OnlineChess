<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title></title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/common.css">
</head>

<body>
    <c:choose>
        <c:when test="success">
            You've been successfully registered! Now you may go <a href="/index.jsp">further</a>
        </c:when>
        <c:otherwise>
            <div>
                <form action="" method="post">
                    <input name="login" value="Login" autofocus>
                    <c:if test="duplicateLogin"><div class="error">Login already in use. Choose another.</div></c:if>
                    <input type="password" name="password" value="Password">
                    <c:if test="shortPassword"><div class="error">Password must have at least 3 symbols.</div></c:if>
                    <input type="submit" value="Sing Up">
                </form>
            </div>
        </c:otherwise>
    </c:if>
</body>

</html>