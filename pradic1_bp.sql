--<PRVA BAZA PODATAKA>--

--<BROJ ZAPISA U TABLICI korisnici>--
SELECT COUNT(DISTINCT korisnik) as brojKorisnika FROM korisnici;

--<BROJ KORISNIKA S PRIDRUŽENIM OVLASTIMA U TABLICI ovlasti>--
SELECT COUNT(DISTINCT korisnik) as brojKorisnikaSOvlastima FROM ovlasti;

--<BROJ OVLASTI KOJE SU PRIDRUŽENE POJEDINOM KORISNIKU U TABLICI ovlasti>--
SELECT korisnik, COUNT(DISTINCT podrucje) as brojOvlastiKorisnika FROM ovlasti GROUP BY korisnik;

--<DRUGA BAZA PODATAKA>--

--<BROJ ZAPISA U TABLICI myairports>--
SELECT count(ident) broj_mojih_aerodroma FROM myairports;

--<BROJ ZAPISA U TABLICI airplanes>--
SELECT COUNT(id) as broj_letova FROM airplanes;

--<BROJ ZAPISA U TABLICI myairportslog>--
SELECT ident, count(DISTINCT flightDate) as broj_dana FROM myairportslog GROUP BY ident;

--<BROJ ZAPISA U TABLICI meteo>--
SELECT ident, COUNT(DISTINCT DATE_FORMAT(lastUpdate, '%Y-%m-%d')) as brojDana FROM meteo GROUP BY ident;
