package main
import java.net._

import akka.actor.Actor
import main.types._

import dispatch.{Defaults, Req, as, implyRequestHandlerTuple, url}
import dispatch.{Http => dispatchHttp}

import scala.concurrent.Future
import scala.io.StdIn._
import scala.util.Try
import java.nio.charset.StandardCharsets.UTF_8

import org.json4s._
import org.json4s.Formats
import org.json4s.jackson.JsonMethods._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{ Failure, Success }

case class dbpedia() extends Actor {
  implicit val formats = DefaultFormats

  override def receive = {
    case Text(x) => {
      /**val u = new URL("http://model.dbpedia-spotlight.org/en/annotate?text="+ (x) +"&confidence=0.7")
      val u = new URL("http://model.dbpedia-spotlight.org/en/annotate?text=salut%20ca%20va%20%3F&confidence=0.7")
      val conn = u.openConnection().asInstanceOf[HttpURLConnection]
      conn.setRequestProperty("accept", "application/json")

      conn.connect
      //println(Source.fromURL("http://model.dbpedia-spotlight.org/en/annotate?text="+URLEncoder.encode(x, "UTF-8")+"&confidence=0.7&content=application/json").mkString)
      println(Source.fromURL(u))**/
      val myRequest = url("http://model.dbpedia-spotlight.org/en/annotate?text="+ (x) +"&confidence=0.7")
      def myRequestAsJson = myRequest.addHeader("accept", "application/json")
      val response = dispatchHttp.default(myRequestAsJson OK dispatch.as.json4s.Json)
      response onComplete {
        case Success(json) => {
          val value = compact(render(json))
          println(value)
        }
        case Failure(error) => println(error)
      }
    }
    case _ => println("error")
  }
}
