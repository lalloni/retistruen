package org.retistruen.view

import org.retistruen.Model
import org.joda.time.Minutes._
import org.retistruen.jmx.JMX
import org.joda.time.Seconds

object MyModel extends Model("mymodel") with JMX {

  val s1 = source[Double]("s1")

  s1 --> mean
  s1 --> max --> rec
  s1 --> max(50) --> rec
  s1 --> mean(50) --> rec
  s1 --> mean(200) --> rec

  val min1 = s1 --> collect(Seconds.seconds(15))

  min1 --> reduce(r.max) --> rec
  min1 --> reduce(r.min) --> rec
  min1 --> reduce(r.mean) --> rec
  min1 --> reduce(r.stddev) --> rec

  s1 --> collect(minutes(5)) --> reduce(r.mean)

  val s2 = source[Double]("s2")

  s2 --> rec
  s2 --> max(10) --> rec
  s2 --> mean --> rec

}

object ShowMyModel extends App {

  MyModel.registerMBeans

  new ModelViewer(MyModel).show

  MyModel.s1 << 0
  MyModel.s2 << 0

  (1 to 1000000) foreach { v ⇒
    MyModel.s1 << math.random * (math.random * v)
    Thread.sleep((math.random * 5).toInt)
  }

}
