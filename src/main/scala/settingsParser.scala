package main

import main.types.settings
import org.json4s.{DefaultFormats, JsonAST, _}
import org.json4s.jackson.JsonMethods._
import org.json4s.JsonDSL._
import org.json4s.DefaultFormats

import scala.io.Source

case class settingsParser(filename: String) {
  implicit val formats = DefaultFormats


  //used to parse context.json file
  def run() : settings = {
    val data = parse(Source.fromFile(filename).mkString)
    val host = if ((data \ "host").extract[String] != null) (data \ "host").extract[String] else ""
    val port = if ((data \ "port").extract[String] != null) (data \ "port").extract[String].toInt else 8080
    val cluster_length_c2c = if ((data \ "cluster_length_c2c").extract[Int] != 0) (data \ "cluster_length_c2c").extract[Int] else 5
    val cluster_length_viso = if ((data \ "cluster_length_viso").extract[Int] != 0) (data \ "cluster_length_viso").extract[Int] else 5
    val cluster_length_dbp = if ((data \ "cluster_length_dbp").extract[Int] != 0) (data \ "cluster_length_dbp").extract[Int] else 2
    val cluster_length_elastic = if ((data \ "cluster_length_elastic").extract[Int] != 0) (data \ "cluster_length_elastic").extract[Int] else 2
    val elastic_confidence = if ((data \ "elastic_confidence").extract[Double] != 0) (data \ "elastic_confidence").extract[Double] else 0.5
    settings(host,port,cluster_length_c2c,cluster_length_viso,cluster_length_dbp,cluster_length_elastic,elastic_confidence)
  }

}
