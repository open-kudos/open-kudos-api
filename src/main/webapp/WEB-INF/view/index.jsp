<!DOCTYPE HTML>
<html>
    <head>
        <title>Index</title>
    </head>
    <body>
        <h1>Hello to kudos app!</h1>
        <div class="container">
            <div>
                <h2>Login</h2>
                <form action="/index" method="post">
                    <div>
                        <input type="text" name="email" placeHolder="email"/>
                    </div>
                    <div>
                        <input type="password" name="password" placeholder="password"/>
                    </div>
                    <button id="loginButton" class="form-control">Login</button>
                </form>
            </div>
            <div>
                <a href="/user/registration">Please register here!<a/>
            </div>
        </div>
    </body>
</html>