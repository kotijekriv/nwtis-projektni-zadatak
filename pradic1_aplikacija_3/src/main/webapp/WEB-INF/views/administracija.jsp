<%-- 
    Document   : administracija
    Created on : Jun 13, 2021, 10:42:19 AM
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

        <h1>Administracija Korisnika</h1>
        <form method="POST" id="obrazac" action="${pageContext.servletContext.contextPath}/mvc/podrucje/aktiviraj">
            <table>
                <tr>
                    <td>Odabir korisnika: </td>
                    <td>
                        <select id="korisnik" name="korisnik" form="obrazac">
                            <c:forEach var="k" items="${korisnici}">
                                <option value="${k.korisnik}">${k.prezime} ${k.ime}</option>
                            </c:forEach>
                        </select>
                    </td>
                    <td/>
                    <td/>
                </tr>
                <tr>
                    <td>Odabir područja: </td>
                    <td>
                        <select id="podrucje" name="podrucje" form="obrazac">
                            <option value="administracija">Administracija</option>
                            <option value="administracijaAerodroma">Administracija aerodroma</option>
                            <option value="pregledKorisnik">Pregled korisnika</option>
                            <option value="pregledJMS">Pregled JMS</option>
                            <option value="pregledDnevnik">Pregled dnevnik</option>
                            <option value="pregledAktivnihKorisnika">Pregled aktivnih korisnika</option>
                            <option value="pregledAerodroma">Pregled Aerodroma</option>
                        </select>
                    </td>
                    <td><input type="submit" value="Aktiviraj odabrano područje"/></td>
                    <td>
                        <input type="submit" value="Deaktiviraj odabrano područje"
                               formaction="${pageContext.servletContext.contextPath}/mvc/podrucje/deaktiviraj" />
                    </td>
                </tr>
            </table>
        </form>

    </body>
</html>
