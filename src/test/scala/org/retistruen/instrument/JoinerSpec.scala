package org.retistruen.instrument

import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers
import org.retistruen._
import Thread.sleep

class JoinerSpec extends Spec with ShouldMatchers {

  class Fixture {

    val source1 = new SourceEmitter[Int]("s1")
    val source2 = new SourceEmitter[Int]("s2")
    val source3 = new SourceEmitter[Int]("s3")
    val joiner = new Joiner[Int]("join")
    val rec = new RecordingReceiver[Seq[Datum[Int]]]("rec")

    source1 >> joiner
    source2 >> joiner
    source3 >> joiner

    joiner >> rec

    joiner start

    def apply(f: Fixture ⇒ Unit) = f(this)

  }

  describe("A Joiner of three emitters") {

    describe("when given one value") {
      it("should had not emitted anything") {
        val fix = new Fixture
        fix(_.source1 << 1)
        fix.joiner.lastValue should equal(None)
      }
    }

    describe("when given a second value from the same source") {
      it("should had not emitted anything") {
        val fix = new Fixture {
          source1 << 1
          source1 << 2
        }
        fix.joiner.lastValue should equal(None)
      }
    }

    describe("when given a value from a second source") {
      it("should had not emitted anything") {
        val fix = new Fixture {
          source1 << 1
          source1 << 2
          source2 << 1
        }
        fix.joiner.lastValue should equal(None)
      }
    }

    describe("when given a value from a third source") {
      it("should emit Seq(2, 1, 1)") {
        val fix = new Fixture {
          source1 << 1
          source1 << 2
          source2 << 1
          source3 << 1
        }
        sleep(10) // to wait for async emission
        fix.rec.dataValues.size should be === 1
        fix.rec.dataValues.last.map(_.value) should be === Seq(2, 1, 1)
        fix.joiner.lastValue.map(_.map(_.value)) should be === Some(Seq(2, 1, 1))
        fix(_.source2 <<< (3, 4))
        sleep(10) // to wait for async emission
        fix.rec.dataValues.size should be === 3
        fix.rec.dataValues.map(_.map(_.value)) should be === Seq(Seq(2, 1, 1), Seq(2, 3, 1), Seq(2, 4, 1))
        fix.joiner.lastValue.map(_.map(_.value)) should be === Some(Seq(2, 4, 1))
      }
    }

  }

}
