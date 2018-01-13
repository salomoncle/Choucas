package main
import akka.actor.{Actor, ActorRef}
import main.types.Num

import scala.io.Source
import scala.util.parsing.json._

case class campToCamp() extends Actor {

//  def getOutingsID(x:List[String]): List[String] ={
//    x.map(s=> JSON.parseFull(s).asInstanceOf[Map[String,String]]("document"))
//  }

  def getOutingsID(x: List[Int]): List[Int]={
    x.map(v => Source.fromURL("https://api.camptocamp.org/outings?offset=" + v + "&pl=fr").mkString)
      .flatMap(s=> JSON.parseFull(s).get.asInstanceOf[Map[String,String]]("documents").asInstanceOf[List[Map[String, Double]]]
      .map(s => s("document_id").asInstanceOf[Int]))
  }

  def getC2CJSON(l : List[Int]) : List[Map[String,Any]]={
    l.map(v => Source.fromURL("https://api.camptocamp.org/outings/" + v).mkString).map(s=> JSON.parseFull(s).get.asInstanceOf[Map[String,Any]])
  }

  override def receive = {
    case Num(x) => println(getC2CJSON(getOutingsID(x))(2)("locales").asInstanceOf[List[Map[String,Any]]](0))
    case _ => println("error")
  }
}
