<%-- 
    Document   : administracijaAerodroma
    Created on : Jun 13, 2021, 10:43:06 AM
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
        <title>Popis mojih aerodroma</title>
    </head>
    <body>
        <a href="${pageContext.servletContext.contextPath}">Početna</a>

        <h1>Zaprati/Otprati</h1>
        <form method="POST" id="obrazac" action="${pageContext.servletContext.contextPath}/mvc/aerodrom/dodaj">
            <table>
                <tr>
                    <td>Odabir korisnika: </td>
                    <td>
                        <select id="aerodrom" name="aerodrom" form="obrazac">
                            <c:forEach var="sa" items="${sviAerodromi}">
                                <option value="${sa.icao}" data-naziv="${sa.naziv}">${sa.icao} ${sa.naziv}</option>
                            </c:forEach>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>Odabrani aerodrom: </td>
                    <td><input id="odabraniAerodrom" readonly /></td>
                    <td><input type="submit" value="Dodaj praćenje aerodroma"/></td>
                </tr>
            </table>
        </form>

        <h1>Popis mojih aerodroma</h1>
        <table id="popisAerodromiPrati">
            <thead>
                <tr>
                    <th>ICAO</th>
                    <th>Naziv</th>
                    <th>Država</th>
                    <th>Geografska širina</th>
                    <th>Geografska dužina</th>
                    <th>PRIKAZ</th>
                    <th>PRAĆENJE</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="a" items="${mojiAerodromi}">
                    <tr>
                        <td>${a.icao}</td>
                        <td>${a.naziv}</td>
                        <td>${a.drzava}</td>
                        <td>${a.lokacija.latitude}</td>
                        <td>${a.lokacija.longitude}</td>
                        <td>
                            <form method="POST" action="${pageContext.servletContext.contextPath}/mvc/aerodrom/korisnici">
                                <input type="hidden" name="aerodrom" value="${a.icao}">
                                <input type="submit" value="Prikaži korisnike"/>
                            </form>
                        </td>
                        <td>
                            <form method="POST" action="${pageContext.servletContext.contextPath}/mvc/aerodrom/izbrisi">
                                <input type="hidden" name="aerodrom" value="${a.icao}">
                                <input type="submit" value="Obriši praćenje"/>                            
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </body>
</html>
