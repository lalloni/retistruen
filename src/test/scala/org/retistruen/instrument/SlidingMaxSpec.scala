/*
 * Author: Pablo Lalloni <plalloni@gmail.com>
 * Created: 24/05/2011 13:48:43
 */
package org.retistruen.instrument

import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers

class SlidingMaxSpec extends FunSpec with ShouldMatchers {

  def newFixture = {
    val emt = new SourceEmitter[Int]("emitter")
    val max = new SlidingMax[Int]("max3", Prune.bySize(3))
    val rec = new RecordingReceiver[Int]("rec")
    emt >> max >> rec
    (emt, max, rec)
  }

  describe("A SlidingMax") {

    describe("when received 3, 5, 1 datums") {
      val (emt, max, rec) = newFixture
      emt <<< (3, 5, 1)
      it("should emit 5") {
        rec.data.last.value should equal(5)
      }
      it("should have emitted 3, 5, 5") {
        rec.data.map(_.value) should equal(Seq(3, 5, 5))
      }
    }

    describe("when received 10, 5, 3, 1") {
      val (emt, max, rec) = newFixture
      emt <<< (10, 5, 3, 1)
      it("should have emitted 5 as last max") {
        rec.data.last.value should equal(5)
      }
      it("should have emitted 10, 10, 10, 5") {
        rec.data.map(_.value) should equal(Seq(10, 10, 10, 5))
      }
    }

  }

}
