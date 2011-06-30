package org.retistruen.instrument

import java.lang.Thread.sleep
import org.joda.time.DateTimeFieldType._
import org.joda.time.{ Partial, Seconds }
import org.retistruen._
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.Spec

class PeriodCollectorSpec extends Spec with ShouldMatchers {

  val data = (1 to 20) map (_ ⇒ (math.random * 100).toInt)

  describe("A PeriodCollector of 1 second") {

    describe("when given " + data) {
      val so = new SourceEmitter[Int]("s")
      val coll = new PeriodCollector[Int]("c", Seconds.ONE)
      val rec = new RecordingReceiver[Seq[Datum[Int]]]("r")
      so >> coll
      coll >> rec
      coll.start
      sleep(500)
      for (d ← data) {
        so << Datum(d)
        sleep(500)
      }
      coll.stop
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
