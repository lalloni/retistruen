/*
 * Author: Pablo Lalloni <plalloni@gmail.com>
 * Created: 27/05/2011 17:13:27
 */
package org.retistruen.jmx

import org.retistruen.Model
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FunSuite
import java.util.logging.Level._
import javax.management.StandardMBean
import javax.management.ObjectName
import akka.actor.ActorSystem

/**
 * @author Pablo Lalloni <plalloni@gmail.com>
 * @since 27/05/2011 17:13:27
 */
class JMXSuite extends FunSuite with ShouldMatchers {

  test("Build a complex model") {

    implicit val as = ActorSystem()

    val model = new Model("test") with JMX {

      val s1 = source[Double]("s1")

      s1 --> max --> rec(10)
      s1 --> mean --> rec(10)

      val max10 = s1 --> max(10)

      max10 --> rec(100)
      max10 --> rec(50)

      s1 --> mean(50) --> rec
      s1 --> mean(200) --> log(INFO)

      val s2 = source[Double]("s2")

      s2 --> max(10) --> rec(20)

    }

    1 to 1000 foreach { _ ⇒ model.s1 << math.random * 1000 }
    1 to 1000 foreach { _ ⇒ model.s2 << math.random * 100 }

    model.registerMBeans

    model.unregisterMBeans

    as.shutdown

  }

}
