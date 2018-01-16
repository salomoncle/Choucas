package main
import akka.actor.{Actor, ActorRef}
import main.types._
import sys.process._


import scala.io.Source
import scala.util.parsing.json._
import org.json4s._
import org.json4s.Formats
import org.json4s.jackson.JsonMethods._

case class campToCamp() extends Actor {

//  def getOutingsID(x:List[String]): List[String] ={
//    x.map(s=> JSON.parseFull(s).asInstanceOf[Map[String,String]]("document"))
//  }

  val list = List.range(0, 1)//9971)
  val dataC2C = getOutingsID(list)

  def getOutingsID(x: List[Int]): List[Int]={
    x.map(v => Source.fromURL("https://api.camptocamp.org/outings?offset=" + v + "&pl=fr&act=hiking").mkString)
      .flatMap(s=> JSON.parseFull(s).get.asInstanceOf[Map[String,String]]("documents").asInstanceOf[List[Map[String, Double]]]
      .map(s => s("document_id").asInstanceOf[Int]))
  }

  def getC2CJSON(l : List[Int]) : List[String] = {//Map[String,Any]]={
    l.map(v => Source.fromURL("https://api.camptocamp.org/outings/" + v).mkString)//.map(s=> JSON.parseFull(s).get.asInstanceOf[Map[String,Any]])
  }

  override def receive = {
    case PushC2C(path, actor) => println("Starting puhing data in es...")
      dataC2C.map(id => Seq("/home/eisti/Downloads/casperjs-1.1.4-1/bin/casperjs", "--url=https://api.camptocamp.org/outings/" + id, "./src/main/js/parseCampToCamp.js") !!).foreach(json => actor ! Push(path, json)) //dataC2C.map(id => actor ! Push(Seq("casperjs", "--url=https://api.camptocamp.org/outings/" + id, "./src/main/js/parseCampToCamp.js"), path !!))
//    case Num(x) => dataC2C.map(id => (Seq("casperjs", "--url=https://api.camptocamp.org/outings/"+id, "./src/main/js/parseCampToCamp.js") !!))
    // println("bonjour " + getC2CJSON(dataC2C).map(url=> println(url)))//("locales").asInstanceOf[List[Map[String,Any]]])
      //println(Seq("casperjs", "--url=https://api.camptocamp.org/outings/959142", "./src/main/js/parseCampToCamp.js") !!)
    case _ => println("error")
  }
}
