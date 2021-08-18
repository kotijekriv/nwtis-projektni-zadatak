<%-- 
    Document   : registracijaKorisnika
    Created on : Apr 28, 2021, 2:54:12 PM
    Author     : NWTiS_4
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Registracija korisnika</title>
    </head>
    <body>
        <h1>Registracija korisnika</h1>
        
        <a href="${pageContext.servletContext.contextPath}">Početna</a><br>

        <form method="POST" action="${pageContext.servletContext.contextPath}/mvc/korisnikRegistracija">
            <div class="container">
                <p>Molimo popunite formu.</p>
                <hr>

                <label for="ime"><b>Ime</b></label>
                <input type="text" placeholder="Unesite ime" name="ime" id="ime" required><br>
                
                <label for="prezime"><b>Prezime</b></label>
                <input type="text" placeholder="Unesite prezime" name="prezime" id="prezime" required><br>

                <label for="korisnik"><b>Korisničko ime</b></label>
                <input type="text" placeholder="Unesite korisničko ime" name="korisnik" id="korisnik" required><br>

                <label for="lozinka"><b>Password</b></label>
                <input type="password" placeholder="Unesite lozinku" name="lozinka" id="lozinka" required><br>
                <hr>
                <button type="submit" class="registerbtn">Registracija</button>
            </div>
        </form>

    </body>
</html>
