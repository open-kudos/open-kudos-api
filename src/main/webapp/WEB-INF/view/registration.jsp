<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE HTML>
<html>
    <head>
        <title>Registration</title>
    </head>
    <body>
        <div class="container">
            ${error}
            <div class="form-group form">
                <form action="registration" method="post">
                    <div>
                        <label>Email:</label>
                        <input type="text" id="email" name="email" value="${form.email}" class="form-control"/>
                    </div>
                    <form:errors path="*"/>
                    <div>
                        <label>Password:</label>
                        <input type="password" id="password" name="password" class="form-control"/>
                        <form:errors path="*"/>
                    </div>
                    <div>
                        <label>Password:</label>
                        <input type="password" id="password" name="confirmPassword" class="form-control"/>
                         <form:errors path="*" />
                    </div>
                    <button id="loginButton" class="form-control">Register</button>

                    <spring:bind path="name">
                      <c:if test="${status.error}">
                        <img src="<c:url value="/resources/images/warning.png"/>"
                           width="31" height="32" class="error_tooltip" title="${status.errorMessage}" />
                      </c:if>
                    </spring:bind>

                </form>
            </div>
        </div>
    </body>
</html>