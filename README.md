# Choucas Config

Using *context.json* file.
Parameters : 
* host **[String]** : host name for Choucas API 
* port **[String]** : listening port for Choucas API
* cluster_length_c2c **[Int]** : length of the actor pool running camp to camp collecting data
* cluster_length_viso **[Int]** : length of the actor pool running viso rando scraping
* cluster_length_bdb **[Int]** : length of the actor pool running dbpeadia annotations
* cluter_length_elastic **[Int]** : length of the actor pool running elastic search requests
* elastic_confidence **[Double]** : number used in dbpedia spotlight http requests for annotate text in data. Refering to [DBPedia-Lucene-Parameters](https://github.com/dbpedia-spotlight/dbpedia-spotlight/wiki/Lucene---Web-Service-Parameters#Confidence_ConfidenceFilter)


Default *context.json* : 
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


# Running Choucas

### Run Elastic Search, LogsTash and Kibana services :
Run the following commands (using [docker-compose](https://docs.docker.com/compose/))
<pre><code>
cd App/
docker-compose up
</code></pre>

### Run Choucas API
Use the following command :
<pre><code>
./choucas
</pre></code>




# Choucas Services

### Elastic Search
[Elastic Search](https://www.elastic.co/) service is deployed on port 9200. 
Data Base can be requested refering to Elastic Search [API](https://www.elastic.co/guide/en/elasticsearch/reference/current/docs.html).

### Kibana 

[Kibana](https://www.elastic.co/products/kibana) service is deployed on port 5601.
Kibana app is usefull to apply tool on [Elastic Search](https://www.elastic.co/) entries

### Rest API


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