/*
 * Author: Pablo Lalloni <plalloni@gmail.com>
 * Created: 27/05/2011 17:13:27
 */
package org.retistruen.building

import org.retistruen.Model
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FunSuite

/**
 * @author Pablo Lalloni <plalloni@gmail.com>
 *  @since 27/05/2011 17:13:27
 */
class BuildingSuite extends FunSuite with ShouldMatchers {

  test("Build a complex model") {

    val model = new Model("test") {

      val s1 = source[Double]("s1")

      s1 --> max --> rec(10)
      s1 --> mean --> rec(10)

      val max10 = s1 --> max(10)

      max10 --> rec(100)
      max10 --> rec(50)

      s1 --> mean(50) --> rec
      s1 --> mean(200) --> rec

      val s2 = source[Double]("s2")

      s2 --> max(10) --> rec(20)

    }

    1 to 1000 foreach { _ ⇒ model.s1 << math.random }
    1 to 2000 foreach { _ ⇒ model.s2 << math.random }

    model.s1 << 10
    model.s2 << 5

  }

}
