package org.retistruen.survey

import scala.collection.mutable.Buffer

import org.joda.time.Duration
import org.joda.time.Seconds.seconds
import org.retistruen.{ Datum, Emitter, Receiver }
import org.retistruen.instrument.{ RecordingReceiver, SourceEmitter }
import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers

import akka.actor.{ ActorSystem, Props }

class FrequencySurveySpec extends FunSpec with ShouldMatchers {

  describe("A 2 seconds FrequencySurvey") {
    describe("when receiving 100 beats in less than 2 seconds") {
      it("should emit 100 in the next tick") {

        val source = new SourceEmitter[Int]("s")
        val receiver = new RecordingReceiver[Int]("r")

        source >> receiver

        val sys = ActorSystem()

        val freq = sys.actorOf(Props(new FrequencySurvey(source, seconds(2))))

        Thread.sleep(500)
        1.to(100).foreach(_ ⇒ freq ! new Beat)
        Thread.sleep(2000)

        receiver.dataValues.headOption should equal(Some(100))

        sys.shutdown

      }
    }
  }

  describe("A 1 second FrequencySurvey") {
    describe("when sent 50 beats every 100 ms") {
      it("should emit frequencies once a second and last should be 10") {
        val source = new SourceEmitter[Int]("s")

        val emitted: Buffer[Datum[Int]] = Buffer.empty
        var lastValue: Int = 0

        source >> new Receiver[Int] {
          val name = "r"
          def receive(e: Emitter[Int], d: Datum[Int]) = {
            lastValue = d.value
            emitted append d
            println(d)
          }
        }

        val sys = ActorSystem()

        val freq = sys.actorOf(Props(new FrequencySurvey(source, seconds(1))))

        1.to(100).foreach { _ ⇒
          Thread.sleep(100)
          freq ! new Beat
        }

        sys.shutdown

        lastValue should equal(10)

        val instants = emitted.map(_.created)
        val diffs = (instants.dropRight(1) zip instants.drop(1)) map (p ⇒ new Duration(p._1, p._2).getMillis.toInt)

        diffs.foreach(_ should (be < (1100) and be > (900)))
      }

    }

  }

}
