<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE HTML>
<html>
    <head>
        <title>Registration</title>
    </head>
    <body>
        <div class="container">
            <form:form method="post" modelAttribute="form" action="/user/registration">
                <div>

                    <label>Email</label>
                    <form:input path="email" type="text" id="email" />
                    <form:errors path="email" />

                </div>
                <div>

                    <label>Name</label>
                    <form:input path="name" type="text" id="name" />
                    <form:errors path="name" />

                </div>
                <div>

                    <label>Surname</label>
                    <form:input path="surname" type="text" id="surname" />
                    <form:errors path="surname" />

                </div>
                <div>

                    <label>Password</label>
                    <form:input path="password" type="password" id="password" />
                    <form:errors path="password" />

                </div>
                <div>

                    <label>Confirm Password</label>
                    <form:input path="confirmPassword" type="password" id="confirmPassword" />
                    <form:errors path="confirmPassword" />

                </div>
                <div>

                    <input type="submit" value="Register" >

                </div>

            </form:form>

        </div>
    </body>
</html>