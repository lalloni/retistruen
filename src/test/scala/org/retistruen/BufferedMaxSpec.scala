/*
 * Author: Pablo Lalloni <plalloni@gmail.com>
 * Created: 24/05/2011 13:48:43
 */
package org.retistruen

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.Spec
import org.joda.time.Instant

class BufferedMaxSpec extends Spec with ShouldMatchers {

  val i = new Instant

  describe("A BufferedMax") {

    describe("when just created") {

      val emitter = new SourceEmitter[Int]("source")

      val max = new BufferedMax[Int]("max", 3)

      val recorder = new RecordingReceiver[Int]("rec")

      emitter --> max --> recorder

      describe("when received 3, 5, 1 datums") {

        emitter emit Datum(3, i)
        emitter emit Datum(5, i)
        emitter emit Datum(1, i)

        it("should emit 5") {
          recorder.recorded.last.value should equal(5)
        }

        it("should have emitted 3, 5, 5") {
          recorder.recorded.map(_.value) should equal(Seq(3, 5, 5))
        }

      }

    }

  }

}
