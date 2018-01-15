package main

object types {
  case class Num(x : List[Int])
  case class Text(x : String)
  case class Pos(x : List[Int], y : Int)
}
