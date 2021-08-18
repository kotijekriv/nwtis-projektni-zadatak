#Korisnici
curl -X GET -H "Accept: application/json" -H "korisnik: pero" -H "lozinka: 123456" http://localhost:8084/pradic1_aplikacija_2/rest/korisnici/
curl -X POST -H "Content-Type: application/json" -H "Accept: application/json" -H "korisnik:pero" -H "lozinka: 123456" -d '{"ime":"Steve","korisnik":"stiv","lozinka":"123456","prezime":"Jobs"}' http://localhost:8084/pradic1_aplikacija_2/rest/korisnici/
curl -X GET -H "Accept: application/json" -H "korisnik: pero" -H "lozinka: 123456" http://localhost:8084/pradic1_aplikacija_2/rest/korisnici/stiv
#Aerodromi
curl -X GET -H "Accept: application/json" -H "korisnik: pero" -H "lozinka: 123456" http://localhost:8084/pradic1_aplikacija_2/rest/aerodromi/
curl -X GET -H "Accept: application/json" -H "korisnik: pero" -H "lozinka: 123456" http://localhost:8084/pradic1_aplikacija_2/rest/aerodromi?naziv=%Za%
curl -X GET -H "Accept: application/json" -H "korisnik: pero" -H "lozinka: 123456" http://localhost:8084/pradic1_aplikacija_2/rest/aerodromi?drzava=GB
curl -X GET -H "Accept: application/json" -H "korisnik: pero" -H "lozinka: 123456" http://localhost:8084/pradic1_aplikacija_2/rest/aerodromi/CYUL
curl -X GET -H "Accept: application/json" -H "korisnik: pero" -H "lozinka: 123456" http://localhost:8084/pradic1_aplikacija_2/rest/aerodromi/CYUL/letovi
curl -X GET -H "Accept: application/json" -H "korisnik: pero" -H "lozinka: 123456" http://localhost:8084/pradic1_aplikacija_2/rest/aerodromi/CYUL/letovi/2021-01-01
curl -X GET -H "Accept: application/json" -H "korisnik: pero" -H "lozinka: 123456" http://localhost:8084/pradic1_aplikacija_2/rest/aerodromi/CYUL/meteoDan/2021-06-12
curl -X GET -H "Accept: application/json" -H "korisnik: pero" -H "lozinka: 123456" http://localhost:8084/pradic1_aplikacija_2/rest/aerodromi/CYUL/meteoVrijeme/1623448800
#Moji aerodromi
curl -X GET -H "Accept: application/json" -H "korisnik: pero" -H "lozinka: 123456" http://localhost:8084/pradic1_aplikacija_2/rest/mojiAerodromi/LOWW/prate
curl -X GET -H "Accept: application/json" -H "korisnik: pero" -H "lozinka: 123456" http://localhost:8084/pradic1_aplikacija_2/rest/mojiAerodromi/pero/prati
curl -X POST -H "Content-Type: application/json" -H "Accept: application/json" -H "korisnik: pero" -H "lozinka: 123456" -d '{"icao":"AYPY", "naziv":"Port Moresby Jacksons International Airport", "drzava":"PG", "lokacija":{"latitude":"147.22000122070312", "longitude":"-9.4433803"}}' http://localhost:8084/pradic1_aplikacija_2/rest/mojiAerodromi/pero/prati
curl -X DELETE -H "Accept: application/json" -H "korisnik: pero" -H "lozinka: 123456" http://localhost:8084/pradic1_aplikacija_2/rest/mojiAerodromi/pero/prati/AYPY
#Dnevnik
curl -X POST -H "Content-Type: application/json" -H "Accept: application/json" -H "korisnik: pero" -H "lozinka: 123456" -d '{"vrijeme":"1622320657429", "komanda":"AUTHEN pero 123456", "odgovor":"OK 7 1622320657429 15", "korisnik":"pero"}' http://localhost:8084/pradic1_aplikacija_2/rest/dnevnik/
curl -X GET -H "Accept: application/json" -H "korisnik: pero" -H "lozinka: 123456" http://localhost:8084/pradic1_aplikacija_2/rest/dnevnik/pero/broj
curl -X GET -H "Accept: application/json" -H "korisnik: pero" -H "lozinka: 123456" "http://localhost:8084/pradic1_aplikacija_2/rest/dnevnik/pero?vrijemeOd=1622239200000&vrijemeDo=1622320657429&pomak=0&stranica=10"
curl -X GET -H "Accept: application/json" -H "korisnik: pero" -H "lozinka: 123456" "http://localhost:8084/pradic1_aplikacija_2/rest/dnevnik/pero?pomak=10&stranica=10"
curl -X GET -H "Accept: application/json" -H "korisnik: pero" -H "lozinka: 123456" "http://localhost:8084/pradic1_aplikacija_2/rest/dnevnik/pero/broj?vrijemeOd=1622239200000&vrijemeDo=1622320657429"
