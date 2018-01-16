package main


import java.net.URLEncoder

import scala.io.StdIn
import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.routing.SmallestMailboxPool
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.sksamuel.elastic4s.ElasticsearchClientUri
import com.sksamuel.elastic4s.http.HttpClient

import scala.concurrent.duration._
import akka.pattern.ask

import sys.process._
import scalaj.http.{Http => JHttp}
import scala.io.StdIn
import io.swagger.annotations
import main.types._

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

//    val s = URLEncoder.encode("salut ca va")
//    actorDbp ! Text(s.replace("+", "%20"))
    val actorES = system.actorOf(SmallestMailboxPool(5).props(Props(putDataES())), name="putDataEs")


    //val url = "https://api.camptocamp.org/outings?offset=0&pl=fr"
    //val json = JHttp(url).header("content-type", "application/json").asString.body
    //println(json.toString)
    //val campToCamp = JSON.parseFull(json.toString).get.asInstanceOf[Map[String, Any]]("documents")
    //println(campToCamp)
    //actorES ! Search()
    //actorDbp ! Text(URLEncoder.encode("Grand Tour du magnifique et mystérieux Lac Pavin et passant par le Puy de Montchal et le goufre du Creux de Soucy.").replace("+", "%20"))

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
            actorC2C ! PushC2C("choucas/randos", actorES)
            complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, s"Collecting data on CamptToCamp"))
        }
      } ~ path("getDataViso") {
        get {
          val nbRandos=(Seq("/home/eisti/Downloads/casperjs-1.1.4-1/bin/casperjs", "./src/main/js/getNbRandos.js") !!)
          val length = nbRandos.substring(0,3).toInt
          var list = List.range(0, length)
//          actorViso ! PushInES("viso/test1", actorES, 4)
          list.map(i=> actorViso ! PushInES("choucas/randos", actorES, i))
          complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "YOYO"))
        }
      } ~ path("elastic"){
          get{
//            actorC2C ! Push("choucas/test", "{\"title\":\"toto\"}", actorES)
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
