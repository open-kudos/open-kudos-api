<!DOCTYPE HTML>
    <html>
        <head>
            <title>Login</title>
        </head>
        <body>
            <div class="container">
                ${loginError}
                <div class="form-group form">
                    <form action='login' method="post">
                        <div>
                            <label>Email:</label>
                            <input type="text" id="email" name="email" placeHolder="email" class="form-control"/>
                        </div>
                        <div>
                            <label>Password:</label>
                            <input type="password" id="password" name="password" placeholder="password" class="form-control"/>
                        </div>
                        <button id="loginButton" class="form-control">Login</button>
                    </form>
                </div>
            </div>
        </body>
    </html>