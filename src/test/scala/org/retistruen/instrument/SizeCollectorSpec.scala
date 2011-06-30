package org.retistruen.instrument

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.Spec
import org.retistruen._

class SizeCollectorSpec extends Spec with ShouldMatchers {
  val data = (1 to 20) map (_ ⇒ (math.random * 100).toInt)
  describe("A SizeCollector of 3") {
    describe("when given " + data) {
      val so = new SourceEmitter[Int]("s")
      val coll = new SizeCollector[Int]("c", 3)
      val rec = new RecordingReceiver[Seq[Datum[Int]]]("r")
      so >> coll
      coll >> rec
      so <<< (data: _*)
      val groups = data.grouped(3).filter(_.size == 3).toSeq
      it("should have emitted " + groups.map(_.mkString("(", ", ", ")")).mkString("(", ", ", ")")) {
        rec.dataValues.map(_.map(_.value)) should equal(groups)
      }
      it("should have " + groups.last.mkString("(", ", ", ")") + " last values") {
        coll.lastValue.map(_.map(_.value)) should equal(Some(groups.last))
      }
    }
  }

}