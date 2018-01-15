package main
import java.io.{ByteArrayOutputStream, PrintWriter}

import akka.actor.{Actor, ActorRef}
import main.types.{Num, Push, PushInES}

import sys.process._
import scala.io.Source
import scala.sys.process.Process
import scala.util.parsing.json._

case class visoRando() extends Actor{




  override def receive = {
    case PushInES(path, actor, x) => {

      actor ! Push(path, Seq("casperjs", "--number=" + x.toString, "./src/main/js/caperjs.js")!!)
    }
    case x if x.isInstanceOf[Int] => println(Seq("casperjs", "--number=" + x.toString, "./src/main/js/caperjs.js") !!)
    case _ => println("error")
  }
}
