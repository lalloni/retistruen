/*
 * Author: Pablo Lalloni <plalloni@gmail.com>
 * Created: 30/05/2011 15:05:26
 */
package org.retistruen.instrument

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.Spec

/**
 * @author Pablo Lalloni <plalloni@gmail.com>
 *  @since 30/05/2011 15:05:26
 */
class SlidingMeanSpec extends Spec with ShouldMatchers {

  describe("A SlidingMean") {

    describe("when just created") {
      val mean = new SlidingMean[Double]("sliding-mean", 2)
      it("must have no cached value") {
        mean.last should equal(None)
      }
    }

    val data = Seq[Double](0, 11, 20, 30, 50, 100, 25, 1, -30, -5, 33, 0)

    describe("when given the sequence " + data.mkString("(", ",", ")")) {
      val source = new SourceEmitter[Double]("source")
      val mean = new SlidingMean[Double]("sliding-mean", 2)
      source >> mean
      var previous: Double = 0
      for (current ← data) {
        describe("after receiving " + current) {
          source << current
          val mean2 = (previous + current) / 2
          val result = mean.poll
          it("should have emited " + mean2) {
            result should equal(Some(mean2))
          }
          previous = current
        }
      }
    }

  }

}
