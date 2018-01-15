package main

import akka.actor.Actor
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives.complete
import com.sksamuel.elastic4s.ElasticsearchClientUri
import com.sksamuel.elastic4s.http.HttpClient
import main.types.{Push, Search, Text}
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
    case Search() => val resp = client.execute {
      search("viso") query "Mont-dore"
    }.await
      println(resp)
    case _ => println("error")
  }

}
