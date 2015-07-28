<!DOCTYPE HTML>
    <html>
        <head>
            <title>Registration completed!</title>
        </head>
        <body>
            <label>Registration successful, here is your data: </label>

            <div class="container">

                        <form:form method="post" modelAttribute="form" action = "registration">

                            <div>

                                <label>Your email is: ${email}</label>

                            </div>
                            <div>

                                <label>Your name is: ${name}</label>

                            </div>
                            <div>

                                <label>Your surname is: ${surname}</label>

                            </div>
                            <div>

                                <label>Your password is: ${password}</label>

                            </div>
                            <div>

                                <input type="submit" value="Go to login page" >

                            </div>

                        </form:form>

                    </div>

        </body>
    </html>