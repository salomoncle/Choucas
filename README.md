**Choucas API**
----

* `GET`/api/getDataC2
  Récupération des données sur camptocamp.org et ajout de ces données dans Elastic Search
  ----
*  `GET`/api/getDataViso
  Récupération des données sur visorando.com et ajout de ces données dans Elastic Search
  ----
*  `GET`/api/search
  Récupération de la recherche effectué par elastic search
*  **URL Params**
    * path : chemin des données dans Elastic Search ("Choucas/randos" par défault)
    * query : Mots recherchés
    * field : Champs dans lequel effectué la recherche
  ---
* `GET`/api/searchAll
    Récupération de la recherche effectué par elastic search
*  **URL Params**
    * path : chemin des données dans Elastic Search ("Choucas/randos" par défault)
    * query : Mots recherchés
  --- 
* `GET`/api/get
    Récupération d'une randonné à partir de l'id
*  **URL Params**
    * path : chemin des données dans Elastic Search ("Choucas/randos" par défault)
    * id : id de la randonné
  --- 
* `GET`/api/getSources
    Récupération des sources de randonnés