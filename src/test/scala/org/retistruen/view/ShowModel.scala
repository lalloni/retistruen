package org.retistruen.view

import org.retistruen.Model

object MyModel extends Model("My Model") {

  val s1 = source[Double]("s1")

  s1 --> max --> rec(10)
  s1 --> max(50) --> rec(10)
  s1 --> mean(50) --> rec
  s1 --> mean(200) --> rec

  val max10 = s1 --> max(10)

  max10 --> rec(100)
  max10 --> rec(50)

  val s2 = source[Double]("s2")

  s2 --> max(10) --> rec(20)
  s2 --> rec(100)
  s2 --> mean

}

object ShowMyModel extends App {

  new ModelViewer(MyModel).show

}
