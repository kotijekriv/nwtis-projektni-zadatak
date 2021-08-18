<%-- 
    Document   : komanda
    Created on : Jun 13, 2021, 10:43:17 AM
    Author     : NWTiS_4
--%>

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
        <title>Slanje komande</title>
    </head>
    
     <body>
        <a href="${pageContext.servletContext.contextPath}">Početna</a>
        <h1>Slobodno slanje komande</h1>
        <form method="POST" action="${pageContext.servletContext.contextPath}/mvc/komanda">
            <table>
                <tr>
                    <td>Upiši komandu: </td>
                    <td><input type="text" name="komanda"/></td>
                    <td><input type="submit" value="Slanje komande"/></td>
            </table>
        </form>
        <p>Vraćeni odgovor: ${odgovor}</p>
    </body>
    
</html>
