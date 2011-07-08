package org.retistruen.instrument

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.Spec
import org.scalatest.WordSpec

class AbsoluteMaxSpec extends Spec with ShouldMatchers {

  describe("An AbsoluteMax") {

    describe("when created") {
      val max = new AbsoluteMax[Int]("x")
      it("should have no max") {
        max.lastValue should equal(None)
      }
    }

    describe("when given 4, 5, 2") {
      val s = new SourceEmitter[Int]("s")
      val max = new AbsoluteMax[Int]("x")
      val rec = new RecordingReceiver[Int]("r")
      s >> max >> rec
      s <<< (4, 5, 2)
      it("should have 5 as lastValue") {
        max.lastValue should equal(Some(5))
      }
      it("sould hace emitted 4, 5, 5") {
        rec.dataValues should equal(Seq(4, 5, 5))
      }
    }

  }

}
