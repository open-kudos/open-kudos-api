<html>

<body>
	<div>
		<h1>Congratulations! You successfully logged in!</h1>
	</div>

	<div>
        <h1>User name is: ${name}</h1>
    </div>
    <div>
        <h1>User surname is: ${surname}</h1>
    </div>
    <div>
        <h1>User email is: ${email}</h1>
    </div>
    <div>
        <h1>User password is: ${password}</h1>
    </div>

    <form method="post">
        <input type="submit" value="Logout" action = "/login?logout" >
    </form>

</html>