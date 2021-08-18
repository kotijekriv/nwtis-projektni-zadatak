<%-- 
    Document   : index
    Created on : Apr 28, 2021, 2:51:40 PM
    Author     : NWTiS_4
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Pero Radić - Projekt</title>
    </head>
    <body>
        <h1>Pero Radić - Projekt</h1>
        <ul>
            <li>
                <a href="${pageContext.servletContext.contextPath}/mvc/korisnik/registracijaKorisnika">Registracija korisnika</a>
            </li>
            <li>
                <a href="${pageContext.servletContext.contextPath}/mvc/korisnik/prijavaKorisnika">Prijava korisnika</a>
            </li>
            <c:if test="${!empty sessionScope.korisnik}">
                <li>
                    <a href="${pageContext.servletContext.contextPath}/mvc/korisnik/administracija">Administracija</a>
                </li>
                <li>
                    <a href="${pageContext.servletContext.contextPath}/mvc/korisnik/administracijaAerodroma">Administracija aerodroma</a>
                </li>
                <li>
                    <a href="${pageContext.servletContext.contextPath}/mvc/korisnik/komanda">Komanda</a>
                </li>
                <li>
                    <a href="${pageContext.servletContext.contextPath}/mvc/korisnik/odjava">ODJAVA</a>
                </li>
            </c:if>
        </ul>

    </body>
</html>
