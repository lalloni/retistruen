package org.retistruen.instrument

import java.lang.Thread.sleep

import org.joda.time.Seconds.seconds
import org.junit.runner.RunWith
import org.retistruen.instrument.reduce.Max
import org.retistruen.{ Receiver, Emitter }
import org.retistruen.Datum
import org.scalatest.junit.JUnitRunner
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.Spec

import grizzled.slf4j.Logging

@RunWith(classOf[JUnitRunner])
class ReduceSpec extends Spec with ShouldMatchers {

  describe("A Reducer") {

    describe("when received empty data") {

      it("should not try to emit value") {

        val source = new SourceEmitter[Int]("src")
        val collector = new PeriodCollector[Int]("col", seconds(1))
        val reducer = new Reducer[Int, Int]("red", new Max)
        val recorder = new Receiver[Int] with Logging {
          val name = "rec"
          var received = false
          def receive(emitter: Emitter[Int], datum: Datum[Int]) = {
            error("Received %s from %s" format (datum, emitter))
            received = true
          }
        }

        source >> collector
        collector >> reducer
        reducer >> recorder

        collector.start
        sleep(2000)
        collector.stop
        
        recorder.received should equal(false)
        

      }

    }

  }

}