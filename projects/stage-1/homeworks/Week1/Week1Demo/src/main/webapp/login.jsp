<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="ex" uri="WEB-INF/custom.tld"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Servlet Demo</title>
</head>
<body>
<h1>Hello JSP and Servlet!</h1>
<ex:Hello>
	This is the login page!
</ex:Hello>
<br/>
<form action="ServletDemo" method="post">
	Account: <input type="text" name="account" />${accountErrorString}<br>
	Password: <input type="password" name="psd" />${psdErrorString}<br>
	<input type="submit" value="Submit" /><br>${errorString}
</form>
</html>