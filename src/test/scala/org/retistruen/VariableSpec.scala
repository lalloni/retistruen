/*
 * Author: Pablo Lalloni <plalloni@gmail.com>
 * Created: 24/05/2011 13:48:43
 */
package org.retistruen

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.Spec
import org.joda.time.Instant

class VariableSpec extends Spec with ShouldMatchers {

  val i = new Instant

  describe("A Variable") {

    describe("when just created") {

      val variable = new Variable[Int]("x")

      it("should have no cached result") {
        variable.last should equal(None)
      }

    }

    describe("when received a datum") {

      val emitter = new SourceEmitter[Int]("emitter")
      val variable = new Variable[Int]("var")
      val rec = new RecordingReceiver[Int]("rec")

      emitter >> variable >> rec

      emitter.emit(Datum(1, i))

      it("should emit the same datum") {
        rec.data.head should equal(Datum(1, i))
      }

      it("should return the same datum as last emitted") {
        variable.last should equal(Some(Datum(1, i)))
      }

    }

  }

}
