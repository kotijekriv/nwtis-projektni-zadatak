<%-- 
    Document   : greska
    Created on : May 2, 2021, 5:22:01 PM
    Author     : NWTiS_4
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Greška</title>
    </head>
    <body>
        <a href="${pageContext.servletContext.contextPath}">Početna</a>
        <h1>Nešto je pošlo po zlu!</h1>
        <p>${opisGreske}</p>
    </body>
</html>
