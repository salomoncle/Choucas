package main
import java.io.{ByteArrayOutputStream, PrintWriter}

import akka.actor.{Actor, ActorRef}
import main.types.Num

import sys.process._
import scala.io.Source
import scala.sys.process.Process
import scala.util.parsing.json._

case class visoRando() extends Actor{




  override def receive = {
    case x if x.isInstanceOf[Int] => sender ! (Seq("/home/eisti/Downloads/casperjs-1.1.4-1/bin/casperjs", "--number=" + x.toString, "./src/main/js/caperjs.js") !!)
    case _ => println("error")
  }
}
