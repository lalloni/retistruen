package org.retistruen.test

import org.scalatest.matchers.{ MatchResult, Matcher }

object CustomScalaTestMatchers {

  class ContainsMatcher[T, S <: Seq[T]](f: T ⇒ Boolean) extends Matcher[S] {
    def apply(o: S) = MatchResult(o.exists(f), "Did not contain", "Contained")
  }

  def anyVerifies[T, S <: Seq[T]](f: T ⇒ Boolean) = new ContainsMatcher[T, S](f)

}
