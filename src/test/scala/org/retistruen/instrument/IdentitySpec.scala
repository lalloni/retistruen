/*
 * Author: Pablo Lalloni <plalloni@gmail.com>
 * Created: 24/05/2011 13:48:43
 */
package org.retistruen.instrument

import org.joda.time.Instant
import org.retistruen.Datum
import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers

class IdentitySpec extends FunSpec with ShouldMatchers {

  val i = new Instant

  describe("An Identity functor") {

    describe("when just created") {

      val identity = new Identity[Int]("x")

      it("should have no cached result") {
        identity.last should equal(None)
      }

    }

    describe("when received a datum") {

      val emitter = new SourceEmitter[Int]("emitter")
      val identity = new Identity[Int]("var")
      val rec = new RecordingReceiver[Int]("rec")

      emitter >> identity >> rec

      emitter << Datum(1, i)

      it("should emit the same datum") {
        rec.data.head.value should equal(1)
      }

      it("should return the same datum as last emitted") {
        identity.last.map(_.value) should equal(Some(1))
      }

    }

  }

}
