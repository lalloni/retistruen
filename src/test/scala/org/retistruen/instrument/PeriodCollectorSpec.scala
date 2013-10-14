package org.retistruen.instrument

import java.lang.Thread.sleep

import org.joda.time.Seconds
import org.retistruen.Datum
import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers

import akka.actor.ActorSystem

class PeriodCollectorSpec extends FunSpec with ShouldMatchers {

  val data = (1 to 20) map (_ ⇒ (math.random * 100).toInt)

  describe("A PeriodCollector of 1 second") {

    describe("when given " + data) {
      implicit val system = ActorSystem()
      val so = new SourceEmitter[Int]("s")
      val coll = new PeriodCollector[Int]("c", Seconds.ONE)
      val rec = new RecordingReceiver[Seq[Datum[Int]]]("r")
      so >> coll
      coll >> rec
      coll.start
      sleep(500)
      for (g ← data.grouped(2)) {
        g foreach (so << Datum(_))
        sleep(1000)
      }
      coll.stop
      system.shutdown
      val groups = data.grouped(2).filter(_.size == 2).toSeq
      it("should have emitted " + groups.map(_.mkString("(", ", ", ")")).mkString("(", ", ", ")")) {
        rec.dataValues.map(_.map(_.value)) should equal(groups)
      }
      it("should have " + groups.last.mkString("(", ", ", ")") + " last values") {
        coll.lastValue.map(_.map(_.value)) should equal(Some(groups.last))
      }
    }

  }

}
