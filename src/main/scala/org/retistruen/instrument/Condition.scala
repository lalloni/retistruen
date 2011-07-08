package org.retistruen.instrument

import org.retistruen._

trait Condition[S] extends SimpleFunctor[S, Boolean] {

  val expression: Datum[S] ⇒ Datum[Boolean]

  def operate(datum: Datum[S]): Datum[Boolean] =
    expression.apply(datum)

}

class Condition1[T1](
  val name: String,
  val expression: (Datum[(T1)]) ⇒ Datum[Boolean])
    extends Condition[(T1)]

class Condition2[T1, T2](
  val name: String,
  val expression: (Datum[(T1, T2)]) ⇒ Datum[Boolean])
    extends Condition[(T1, T2)]

class Condition3[T1, T2, T3](
  val name: String,
  val expression: (Datum[(T1, T2, T3)]) ⇒ Datum[Boolean])
    extends Condition[(T1, T2, T3)]

class Condition4[T1, T2, T3, T4](
  val name: String,
  val expression: (Datum[(T1, T2, T3, T4)]) ⇒ Datum[Boolean])
    extends Condition[(T1, T2, T3, T4)]

class Condition5[T1, T2, T3, T4, T5](
  val name: String,
  val expression: (Datum[(T1, T2, T3, T4, T5)]) ⇒ Datum[Boolean])
    extends Condition[(T1, T2, T3, T4, T5)]

class Condition6[T1, T2, T3, T4, T5, T6](
  val name: String,
  val expression: (Datum[(T1, T2, T3, T4, T5, T6)]) ⇒ Datum[Boolean])
    extends Condition[(T1, T2, T3, T4, T5, T6)]
