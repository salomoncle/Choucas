package main

import akka.actor.Actor
import main.types._
import scalaj.http.{Http => JHttp}
import org.json4s._

case class dbpedia(confidence : Double) extends Actor {
  implicit val formats = DefaultFormats

  override def receive = {
    case Text(x) => {
      val result = JHttp("http://model.dbpedia-spotlight.org/en/annotate?text="+ (x) +confidence).header("accept", "application/json").asString
      val res = result.body
      sender ! res
    }
    case _ => println("error")
  }
}
