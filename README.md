**Choucas Config**
----
Using context.json file.
Parameters : 
* host : [String] host name for Choucas API 
* port : [String] listening port for Choucas API
* cluster_length_c2c : [Int] length of the actor pool running camp to camp collecting data
* cluster_length_viso : [Int] length of the actor pool running viso rando scraping
* cluster_length_bdb : [Int] length of the actor pool running dbpeadia annotations
* cluter_length_elastic : [Int] length of the actor pool running elastic search requests
* elastic_confidence : [Double] number used in dbpedia spotlight http requests for annotate text in data. Refering to [DBPedia-Lucene-Parameters](https://github.com/dbpedia-spotlight/dbpedia-spotlight/wiki/Lucene---Web-Service-Parameters#Confidence_ConfidenceFilter)


Default context.json : 
<pre><code>
{
     "host: "localhost",
     "port": "8081",
     "cluster_length_c2c": 5,
     "cluster_length_viso": 5,
     "cluster_length_dbp": 2,
     "cluster_length_elastic": 2,
     "elastic_confidence" : 0.3
}
</code></pre>


**Running with Docker**
----
Run the following command (using [docker-compose](https://docs.docker.com/compose/))
<pre><code>
docker-compose up
</code></pre>




**Choucas API**
----

* `GET`/api/getDataC2
  * Récupération des données de [Camp To Camp](https://www.camptocamp.org/) et ajout de ces données dans Elastic Search
  ----
*  `GET`/api/getDataViso
   * Récupération des données de [Viso Rando](https://www.visorando.com/) et ajout de ces données dans Elastic Search
  ----
*  `GET`/api/search
   * Récupération de la recherche effectué par [Elastic Search](https://www.elastic.co/)
   
*  **URL Params**
    * path : chemin des données dans [Elastic Search](https://www.elastic.co/)("Choucas/randos" par défault)
    * query : Mots recherchés
    * field : Champ dans lequel effectué la recherche
  ---
* `GET`/api/searchAll
    * Récupération de la recherche effectué par [Elastic Search](https://www.elastic.co/)
  **URL Params**
    * path : chemin des données dans [Elastic Search](https://www.elastic.co/) ("Choucas/randos" par défault)
    * query : Mots recherchés
      
  --- 
* `GET`/api/get
   * Récupération d'une randonné à partir de l'id
*  **URL Params**
    * path : chemin des données dans [Elastic Search](https://www.elastic.co/) ("Choucas/randos" par défault)
    * id : id de la randonné
  --- 
* `GET`/api/getSources
    * Récupération des sources de randonnés 