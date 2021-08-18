<%-- 
    Document   : popisKorisnikaPrati
    Created on : Jun 13, 2021, 12:21:37 PM
    Author     : NWTiS_4
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="https://cdn.datatables.net/1.10.20/css/jquery.dataTables.css" type="text/css">
        <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/smoothness/jquery-ui.css" type="text/css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
        <script src="//code.jquery.com/jquery-1.12.4.js"></script>
        <script src="//code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
        <script src="https://cdn.datatables.net/1.10.20/js/jquery.dataTables.js"></script>
        <script src="${pageContext.servletContext.contextPath}/resources/pradic1.js"></script>        
        <title>Više o aerodromu</title>
    </head>
    <body>
        <a href="${pageContext.servletContext.contextPath}">Početna</a>
        <h1>Popis korisnika koji prate odabrani aerodrom</h1><br>

        <h2>Korisnici koji prate aerodrom</h2>
        <table id="popisKorisnika">
            <thead>
                <tr>
                    <th>Korisničko ime</th>
                    <th>Ime</th>
                    <th>Prezime</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="korisnik" items="${korisnici}">
                    <tr>
                        <td>${korisnik.korisnik}</td>
                        <td>${korisnik.ime}</td>
                        <td>${korisnik.prezime}</td>                    
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </body>
</html>