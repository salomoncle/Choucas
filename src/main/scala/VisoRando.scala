package main

import akka.actor.{Actor, ActorRef}
import main.types.{Push, PushInES}

import sys.process._

case class visoRando() extends Actor{




  override def receive = {
    case PushInES(path, actor, x) => actor ! Push(path, Seq("casperjs", "--number=" + x.toString, "./src/main/js/caperjs.js")!!)
    case _ => println("error")
  }
}
