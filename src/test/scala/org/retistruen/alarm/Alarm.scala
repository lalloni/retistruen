package org.retistruen.alarm

import org.retistruen._
import org.joda.time.Minutes._

object AlarmModel extends Model("alarm") {

  val latency = source[Double]("latency")

  val medianLatency = latency --> collect(minutes(1)) --> reduce.median

  val window = medianLatency --> slide(60)

  val medianLatencyMean = window --> reduce.mean

  val medianLatencyStdDev = window --> reduce.stddev

  // val alarm = (medianLatency, medianLatencyMean, medianLatencyStdDev) --> { (a, b, c) ⇒ a > b + c }

}

object Alarm extends Receiver[Seq[Datum[Double]]] {

  val name = "Alarm"

  def receive(e: Emitter[Seq[Datum[Double]]], d: Datum[Seq[Datum[Double]]]) = {
    import AlarmModel._
    if (medianLatency > medianLatencyMean + medianLatencyStdDev)
      println("Alarm!")
  }

  def main(args: Array[String]): Unit = {
    AlarmModel.window >> this
  }

}
