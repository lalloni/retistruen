package org.retistruen.instrument.reduce

import org.scalatest._
import org.scalatest.matchers.ShouldMatchers

class AlgoSpec extends FunSuite with ShouldMatchers {

  test("percentRank") {

    algo.percentRank[Double](5)(3) should be (50.0)

    algo.percentRank[Double](100)(50) should be (49.5)

  }

  test("interpolatedPercentile") {
    
    algo.interpolatedPercentile(40)(Seq(15.0, 20.0, 35.0, 40.0, 50.0)) should be (27.5)

    algo.interpolatedPercentile(50)(Seq(1.0, 2.0, 3.0, 4.0, 5.0)) should be (3.0)

    algo.interpolatedPercentile(50)(Seq(1.0, 2.0, 4.0, 5.0)) should be (3.0)

    algo.interpolatedPercentile(50)(Seq(1.0, 3.0, 5.0)) should be (3.0)

    algo.interpolatedPercentile(50)(Seq(1.0, 3.0)) should be (2.0)

    algo.interpolatedPercentile(50)(Seq(1.0)) should be (1.0)

  }

}