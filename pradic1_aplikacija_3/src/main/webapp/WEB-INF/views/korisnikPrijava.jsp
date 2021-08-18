<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Prijava korisnika</title>
    </head>
    <body>
        <h1>Prijava korisnika</h1>
        
        <a href="${pageContext.servletContext.contextPath}">Početna</a><br><br>
        
        <form method="POST" action="${pageContext.servletContext.contextPath}/mvc/prijavaKorisnika">
            <table>
                <tr>
                    <td>Korisničko ime</td>
                    <td><input type="text" name="korisnik"/></td>
                </tr>
                <tr>
                    <td>Lozinka</td>
                    <td><input type="password" name="lozinka"/></td>
                </tr>
                <tr>
                    <td></td>
                    <td><input type="submit" name="potvrdi" value="Prijava"/></td>
                </tr>
            </table>
        </form>
    </body>
</html>
