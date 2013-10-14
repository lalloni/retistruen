package org.retistruen.alarm

import org.joda.time.Minutes.minutes
import org.retistruen.{ Datum, Emitter, Model, Receiver }

import akka.actor.ActorSystem

class AlarmModel(override val actorSystem: ActorSystem) extends Model("alarm")(actorSystem) {

  val latency = source[Double]("latency")

  val medianLatency = latency --> collect(minutes(1)) --> reduce.median

  val window = medianLatency --> slide(60)

  val medianLatencyMean = window --> reduce.mean

  val medianLatencyStdDev = window --> reduce.stddev

  // val alarm = (medianLatency, medianLatencyMean, medianLatencyStdDev) --> { (a, b, c) ⇒ a > b + c }

}

object Alarm extends Receiver[Seq[Datum[Double]]] {

  val as = ActorSystem()

  val model = new AlarmModel(as)

  import model._

  val name = "Alarm"

  def receive(e: Emitter[Seq[Datum[Double]]], d: Datum[Seq[Datum[Double]]]) = {
    if (medianLatency > medianLatencyMean + medianLatencyStdDev)
      println("Alarm!")
  }

  def main(args: Array[String]): Unit = {
    window >> this
  }

}
