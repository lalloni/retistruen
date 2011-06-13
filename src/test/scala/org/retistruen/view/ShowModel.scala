package org.retistruen.view

import org.joda.time.Seconds._
import org.joda.time.Minutes._
import org.retistruen.jmx.JMX
import org.retistruen._
import org.retistruen.instrument.reduce.Max

object MyModel extends Model("mymodel") with JMX {

  val s1 = source[Double]("s1")

  s1 --> mean
  s1 --> max --> rec
  s1 --> max(50) --> rec
  s1 --> mean(50) --> rec
  s1 --> mean(200) --> rec

  val min1 = s1 --> collect(seconds(15))

  min1 --> reduce.max --> rec
  min1 --> reduce.min --> rec
  min1 --> reduce.mean --> rec
  min1 --> reduce.stddev --> rec
  min1 --> reduce("parity", { seq: Seq[Double] ⇒ seq.map(d ⇒ 1 - (d % 2)).sum }) // cálculo de "paridad" del bloque

  s1 --> collect(minutes(5)) --> reduce.mean

  val s2 = source[Double]("s2")

  s2 --> rec
  s2 --> max(10) --> rec
  s2 --> mean --> rec

  val s3 = osource[Double]("s3")

  s3 --> collect(seconds(10)) --> reduce.mode --> rec(1000)

  val s3c = s3 --> collect(seconds(15))

  s3c --> reduce.open
  s3c --> reduce.close
  s3c --> reduce.max
  s3c --> reduce.min
  s3c --> reduce.median
  s3c --> reduce.mean
  s3c --> reduce.mode
  s3c --> reduce.percentile(90)
  s3c --> reduce.percentile(10)
  s3c --> reduce.range

}

object ShowMyModel extends App {

  MyModel.registerMBeans

  new ModelViewer(MyModel).show

  MyModel.s1 << 0
  MyModel.s2 << 0

  (1 to 1000000) foreach { v ⇒
    MyModel.s1 << math.random * (math.random * v)
    Thread.sleep((math.random * 5).toInt)
    MyModel.s3 << (math.random * 100).toInt
  }

}
