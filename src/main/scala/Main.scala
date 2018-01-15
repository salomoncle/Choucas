package main


import java.net.URLEncoder

import scala.io.StdIn
import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.pattern.ask
import akka.routing.SmallestMailboxPool
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.sksamuel.elastic4s.ElasticsearchClientUri
import com.sksamuel.elastic4s.http.HttpClient
import dispatch.Future

import scala.concurrent.duration._
import scalaj.http.{Http => JHttp}
import scala.io.StdIn
import io.swagger.annotations
import main.types.{Num, Text}

import scala.concurrent.Await
import scala.util.parsing.json._

object HttpServer {

  def main(args: Array[String]) {

    implicit val system = ActorSystem("my-system")
    implicit val materializer = ActorMaterializer()
    // needed for the future flatMap/onComplete in the end
    implicit val executionContext = system.dispatcher
    val list = List.range(0, 5)//9971)
    val numbers = Num(list)
    val actorC2C = system.actorOf(SmallestMailboxPool(5).props(Props(campToCamp())), name = "getDataC2C")
    val actorViso = system.actorOf(SmallestMailboxPool(5).props(Props(visoRando())), name = "getDataViso")
    val actorDbp = system.actorOf(SmallestMailboxPool(5).props(Props(dbpedia())), name = "dbpedia")
    /*
    val s = URLEncoder.encode("salut ca va")
    actorDbp ! Text(s.replace("+", "%20"))
    */


    //val url = "https://api.camptocamp.org/outings?offset=0&pl=fr"
    //val json = JHttp(url).header("content-type", "application/json").asString.body
    //println(json.toString)
    //val campToCamp = JSON.parseFull(json.toString).get.asInstanceOf[Map[String, Any]]("documents")
    //println(campToCamp)

    val route =
      path("index") {
        get {
          parameter('name) {(name) =>complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, s"this is a get request : '$name'")) }
        } ~
          post{
            parameter('name) {(name) =>complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, s"this is a post request : '$name'")) }
          }
      } ~ path("getDataC2C"){
        get {
            actorC2C ! numbers
            complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, s"Collecting data on CamptToCamp"))
        }
      } ~ path("getDataViso") {
        get {
            var i=0;
            var stop = false
            while (!stop) {
              implicit val timeout = Timeout(50 seconds)
              val future = actorViso ? i // enabled by the “ask” import
              var result = Await.result(future, timeout.duration).asInstanceOf[String]
              //saveInES(result)
              if (result == "") stop = true
              i+=1
            }
            complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "It's OK"))

        }
      } ~ path("elastic"){
          get{
            import com.sksamuel.elastic4s.http.ElasticDsl._
            val client = HttpClient(ElasticsearchClientUri("localhost",9200))
            val response = client.execute{
              clusterState()
              //indexInto("choucas/test").doc("{\"title\"=\"toto\"}")
            }.await
            complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, s"ELASTIC"))

          }
        }
    
    val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)

    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }

}
