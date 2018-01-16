package main

import akka.actor.Actor
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives.complete
import com.sksamuel.elastic4s.ElasticsearchClientUri
import com.sksamuel.elastic4s.http.HttpClient
import main.types._
import com.sksamuel.elastic4s.http.ElasticDsl._

case class putDataES() extends Actor {
  val client = HttpClient(ElasticsearchClientUri("localhost",9200))

  override def receive = {
    case Push(path, json) => {
      import com.sksamuel.elastic4s.http.ElasticDsl._

      val response = client.execute{
        indexInto(path).doc(json)
      }.await
      println(response)
    }
    case Search(path, qr) =>
      val resp = client.execute {
      search(path).query(qr)
    }.await
      sender ! JsonUtil.toJson(resp)
    case SearchInField(path, qr, field) => val resp = client.execute {
      search(path).query(field+"="+qr)
    }.await
      sender ! JsonUtil.toJson(((resp)))
    case GetWithId(path, id) => val resp = client.execute {
      get(id).from(path)
    }.await
      sender ! JsonUtil.toJson(resp.source)
    case GetSources(path, field) => val resp = "[\"https://www.camptocamp.org\",\"https://www.visorando.com\"]"
      sender ! (resp)
    case _ => println("error")
  }

}
