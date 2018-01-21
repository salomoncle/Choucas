package main

import main.types.settings
import org.json4s.{DefaultFormats, JsonAST, _}
import org.json4s.jackson.JsonMethods._
import org.json4s.JsonDSL._
import org.json4s.DefaultFormats

import scala.io.Source

case class settingsParser(filename: String) {
  implicit val formats = DefaultFormats


  //use to parse context.json file
  def run() : settings = {
    val data = parse(Source.fromFile(filename).mkString)
    val host = if ((data \ "host").extract[String] != null) (data \ "host").extract[String] else ""
    val port = if ((data \ "port").extract[String] != null) (data \ "port").extract[String].toInt else 8080
    val cluster_length = if ((data \ "cluster_length").extract[Int] != null) (data \ "cluster_length").extract[Int] else 5
    settings(host,port,cluster_length)
  }

}
