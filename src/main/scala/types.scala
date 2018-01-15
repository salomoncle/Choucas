package main

import akka.actor.ActorRef

object types {
  case class Num(x : List[Int])
  case class Text(x : String)
  case class Pos(x : List[Int], y : Int)
  case class Push(path : String, json : String)
  case class PushInES(path : String, actor: ActorRef, i:Int)
  case class PushC2C(path : String, actor : ActorRef)
  case class Search()

  case class JsonFormat(
                             commune: String,
                             deniveleN: String,
                             deniveleP: String,
                             depart: String,
                             difficulte: String,
                             distance: String,
                             duree: String,
                             pointBas: String,
                             pointHaut: String,
                             regions: List[String],
                             retourPointBas: String,
                             titre: String,
                             description: String,
                             url: String,
                             source : String
                           )
}
