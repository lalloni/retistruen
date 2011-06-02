/*
 * Author: Pablo Lalloni <plalloni@gmail.com>
 * Created: 30/05/2011 15:05:26
 */
package org.retistruen.instrument

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.Spec

/**
 * @author Pablo Lalloni <plalloni@gmail.com>
 * @since 30/05/2011 15:05:26
 */
class AbsoluteMeanSpec extends Spec with ShouldMatchers {

  describe("An AbsoluteMean") {

    describe("when just created") {
      val mean = new AbsoluteMean[Double]("abs-mean")
      it("must have no cached value") {
        mean.last should equal(None)
      }
    }

    val data = Seq[Double](0, 10, 20, 30, 50, 100, 25, 1, -30)
    val aritmean = data.sum / data.size

    describe("when given " + data.mkString("(", ",", ")")) {
      val source = new SourceEmitter[Double]("source")
      val mean = new AbsoluteMean[Double]("abs-mean")
      source >> mean
      source <<< (data: _*)
      it("must return the arithmetic mean of the values received which is " + aritmean) {
        mean.last.map(_.value) should equal(Some(aritmean))
      }
    }

  }

}
