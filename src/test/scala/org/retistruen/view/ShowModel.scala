package org.retistruen.view

import org.retistruen.Model
import org.joda.time.Minutes._
import org.retistruen.jmx.JMX

object MyModel extends Model("mymodel") with JMX {

  val s1 = source[Double]("s1")

  s1 --> max --> rec
  s1 --> max(50) --> rec
  s1 --> mean(50) --> rec
  s1 --> mean(200) --> rec

  val min1 = s1 --> collect(minutes(1))

  min1 --> reduce(r.max) --> rec
  min1 --> reduce(r.min) --> rec
  min1 --> reduce(r.mean) --> rec
  min1 --> reduce(r.stddev) --> rec

  val s2 = source[Double]("s2")

  s2 --> rec
  s2 --> max(10) --> rec
  s2 --> mean --> rec

}

object ShowMyModel extends App {

  MyModel.registerMBeans

  new ModelViewer(MyModel).show

}
