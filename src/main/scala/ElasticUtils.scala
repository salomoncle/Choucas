package main

import akka.actor.Actor
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives.complete
import com.sksamuel.elastic4s.ElasticsearchClientUri
import com.sksamuel.elastic4s.http.HttpClient
import main.types.{Push, Text}
import com.sksamuel.elastic4s.http.ElasticDsl._

case class putDataES() extends Actor {

  override def receive = {
    case Push(path, json) => {
      import com.sksamuel.elastic4s.http.ElasticDsl._
      val client = HttpClient(ElasticsearchClientUri("localhost",9200))
      val response = client.execute{
        indexInto(path).doc(json)
      }.await
      println(response)
    }
    case _ => println("error")
  }

}
