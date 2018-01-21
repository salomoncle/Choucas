package main

import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.routing.SmallestMailboxPool
import akka.stream.ActorMaterializer
import akka.util.Timeout
import scala.concurrent.duration._
import akka.pattern.ask
import sys.process._
import scala.io.StdIn
import main.types._
import scala.concurrent.Await

object HttpServer {
  def main(args: Array[String]) {

    implicit val system = ActorSystem("my-system")
    implicit val materializer = ActorMaterializer()
    implicit val executionContext = system.dispatcher

    //parse settings file
    val settings = settingsParser("./src/context.json").run()

    val list = List.range(0, 5)//9971)
    val numbers = Num(list)
    val actorC2C = system.actorOf(SmallestMailboxPool(settings.cluster_length).props(Props(campToCamp())), name = "getDataC2C")
    val actorViso = system.actorOf(SmallestMailboxPool(settings.cluster_length).props(Props(visoRando())), name = "getDataViso")
    val actorDbp = system.actorOf(SmallestMailboxPool(settings.cluster_length).props(Props(dbpedia())), name = "dbpedia")
    val actorES = system.actorOf(SmallestMailboxPool(settings.cluster_length).props(Props(putDataES(actorDbp))), name="putDataEs")

    val route =
      path("getDataC2C"){
        get {
            actorC2C ! PushC2C("choucas/randos", actorES)
            complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, s"Collecting data on CamptToCamp.org"))
        }
      } ~ path("getDataViso") {
        get {
          val nbRandos=(Seq("casperjs", "./src/main/js/getNbRandos.js") !!)
          val length = nbRandos.substring(0,3).toInt
          var list = List.range(0, length)
          list.map(i=> actorViso ! PushInES("choucas/randos", actorES, i))
          complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "Collecting Data on visorando.com"))
        }
      } ~ path("sandbox"){
        get{
            actorES ! SearchInField("choucas/randos","sommet","description")
            actorES ! Search("choucas/randos","1h")
            complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, s"ELASTIC"))
          }
        } ~ path("api" / "search"){
        get {
          parameters('path, 'query, 'field){(path, query, field)=>
            implicit val timeout = Timeout(12 seconds)
            val future = actorES ? SearchInField(path, query, field)
            val result = Await.result(future, timeout.duration).asInstanceOf[String]
            println(result)

            complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, result))
          }
        }
      }~ path("api" / "searchAll"){
        get {
          parameters('path, 'query){(path, query)=>
            implicit val timeout = Timeout(12 seconds)
            val future = actorES ? Search(path, query)
            val result = Await.result(future, timeout.duration).asInstanceOf[String]
            println(result)

            complete(HttpEntity(ContentTypes.`text/html(UTF-8)`,result.toString))
          }
        }
      }~ path("api" / "get"){
        get {
          parameters('path, 'id){(path, id)=>
            implicit val timeout = Timeout(12 seconds)
            val future = actorES ? GetWithId(path, id)
            val result = Await.result(future, timeout.duration).asInstanceOf[String]
            println(result)

            complete(HttpEntity(ContentTypes.`text/html(UTF-8)`,result.toString))
          }
        }
      }~ path("api" / "getSources"){
        get {
          parameters('path, 'field){(path, field)=>
            implicit val timeout = Timeout(80 seconds)
            val future = actorES ? GetSources
            val result = Await.result(future, timeout.duration).asInstanceOf[String]
            println(result)

            complete(HttpEntity(ContentTypes.`text/html(UTF-8)`,result.toString))
          }
        }
      }
    
    val bindingFuture = Http().bindAndHandle(route, settings.host, settings.port)

    println(s"Server online at http://" + settings.host + ":" + settings.port + "/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }
}
