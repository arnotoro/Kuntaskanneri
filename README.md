# Kuntaskanneri

## Kuvaus
**LUT Olio-ohjelmointi kurssin harjoitustyö**
Kuntaskanneri on Android-sovellus, joka hakee käyttäjän antaman kunnan perustiedot Tilastokeskuksen APIsta ja näyttää ne käyttäjälle. Sovellus hakee kunnan väestökehityksen, työpaikkaomavaraisuuden ja työllisyysasteen. Sovellus käyttää myös Fintrafficin ja OpenWeatherMapin APIa hakeakseen liikennekamerakuvia ja säätilan mikäli nämä ovat saatavilla. Sovellus tallentaa viimeisimmät haut, jotta käyttäjä voi helposti palata niihin.

## Ominaisuudet ja pisteytysehdotus

### Pakolliset ominaisuudet
- [x] Ohjelma on koodattu olioparadigman mukaisesti
- [x] Ohjelman koodi ja kommentit ovat englanniksi 
- [x] Ohjelma on koodattu Javalla Android-studiolla ja se toimii Android-laitteilla
- [x] Ohjelma sisältää vaaditut perustoiminnallisuudet
  - [x] Perustietojen haku kunnan nimen perusteella
    - [x] Väestökehitys (12dy)
    - [x] Työpaikkaomavaraisuus (125s)
    - [x] Työllisyysaste (115x)
  - [x] Tietojen esittäminen käyttäjälle
- [x] Dokumentaatio (README.md)
  - [x] Kuvaus
  - [ ] Luokkakaavio
  - [x] Työnjako
  - [x] Implementoidut ominaisuudet
  - [x] Pisteytysehdotus
- [x] Tilastokeskuksen API käytössä tiedonhaussa

### Lisäominaisuudet
- [x] RecycleView-komponentin käyttö listattaessa dataa
  - [x] Aiemmin haettujen kuntien tilasto
- [x] Datassa näytetään kuvia
  - [x] Kunnan asukasluvun mukaan vaihtuva siluetti
    - Pieni kunta (<20000 asukasta)
    - Keskisuuri kunta (20000-100000 asukasta)
    - Suuri kunta (>100000 asukasta)
- [x] Datalähteitä on useampi
  - [x] Tilastokeskuksen API
  - [x] Fintraffic
    - Liikennekamerakuvat
  - [x] OpenWeatherMap
    - Tämän hetkinen sää
- [x] Viimeksi haetut kunnat pikavalintoina ohjelman etusivulla
- [x] Fragmenttien käyttö
  - [x] Kunnan tiedot, sää ja liikennekamerat
- [x] Datan visualisointi graafisesti
  - [x] Asukasmäärän kehitys AnyChartilla


### Pisteytysehdotus
- **10 pistettä** pakollisista ominaisuuksista
- **21 pistettä** lisäominaisuuksista
  - 3 pistettä - RecycleView-komponentin käytöstä
    - Aiemmin haettujen kuntien tilasto
    - Liikennekamerakuvat
  - 2 pistettä - Kuvien käytöstä datassa
    - Kaupunkien siluetit
    - Liikennekamerakuvat
    - Sääikoni
  - 5 pistettä - Datalähteitä on useampi
    - 3 pistettä toisesta datalähteestä
    - 2 pistettä kolmannesta datalähteestä
  - 2 pistettä - Viimeksi haetut kunnat pikavalintoina etusivulla
  - 4 pistettä - Fragmenttien käytöstä kunta-, sää- ja liikennekameratietojen esityksessä
  - 5 pistettä - Datan visualisointi graafisesti
    - Asukasluvun muutos

#### Yhteenlaskeutut pisteet: 31

## Luokkakaavio
![Class diagram](./classdiagram.puml)  



## Käyttöohjeet
### Aloitusruutu
1. Syötä suomalaisen kunnan nimi hakukenttään ja paina "Hae tiedot" -nappia.
2. Sovellus hakee kunnan tiedot ja näyttää ne käyttäjälle.
3. Ylhäällä olevasta navigointipalkista pystyy vaihtamaan näkymää kunnan tietojen, sään ja liikennekameroiden välillä.
4. Puhelimen takaisin-napilla pääsee takaisin aloitusnäkymään, missä näkyy myös viimeksi haetut kunnat pikavalintoina.

