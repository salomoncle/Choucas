package main
import akka.actor.{Actor, ActorRef}
import main.types.Num

import scala.io.Source
import scala.sys.process.Process
import scala.util.parsing.json._

case class visoRando() extends Actor{

  override def receive = {
    case x if x.isInstanceOf[Int] => println( Process(Seq("casperjs", "/home/eisti/Info/ICC/Choucas/src/main/js/caperjs.js", "--number="+x.toString)) ! )
    case _ => println("error")
  }
}
