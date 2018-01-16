package main

import akka.actor.{Actor, ActorRef}
import com.sksamuel.elastic4s.ElasticsearchClientUri
import com.sksamuel.elastic4s.http.HttpClient
import main.types._
import com.sksamuel.elastic4s.http.ElasticDsl._
import org.json4s.{DefaultFormats, JsonAST, _}
import org.json4s.jackson.JsonMethods._
import java.net.URLEncoder

import akka.pattern.ask

import scala.concurrent.duration._
import akka.util.Timeout
import org.json4s.JsonDSL._

import scala.concurrent.Await



case class putDataES(dbp : ActorRef) extends Actor {
  implicit val formats = DefaultFormats
  val client = HttpClient(ElasticsearchClientUri("localhost",9200))

  override def receive = {
    case Push(path, json) => {
      import com.sksamuel.elastic4s.http.ElasticDsl._

      val response = client.execute {
        implicit val timeout = Timeout(25 seconds) // needed for `?` below
        val texte = if ((parse(json) \ "description").extract[String] != null) (parse(json) \ "description").extract[String] else ""
        val commune = if  ((parse(json) \ "commune").extract[String] != null) (parse(json) \ "commune").extract[String] else ""

        val tag_texte = if (texte != "") {
          val future_text = dbp ? Text(URLEncoder.encode(texte).replace("+", "%20"))
          val future_commune = dbp ? Text(URLEncoder.encode(commune).replace("+", "%20"))
          Await.result(future_text, timeout.duration).asInstanceOf[String] + Await.result(future_commune, timeout.duration).asInstanceOf[String]
        } else ""

        val res = if (tag_texte.length>2 && (tag_texte.substring(0,2) != "org") && ((parse(tag_texte) \ "Resources" \ "@surfaceForm") != JNothing )) {
          val parseTag =  "Tag" -> (parse(tag_texte) \ "Resources" \ "@surfaceForm")
          parse(json) merge render(parseTag)
        } else {
          parse(json) merge parse(compact(render("Tag" -> List(""))))
        }

        indexInto(path).doc(compact(render(res)))
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
