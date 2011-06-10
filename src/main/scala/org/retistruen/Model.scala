package org.retistruen

import org.retistruen.building._

/** Represents a complete network of $Emitter and $Receiver.
 *
 *  This is the main entry point to the library, this class provides both a means
 *  to build the model and to reference its parts afterwards.
 *
 *  A tipical model building case would be:
 *
 *  {{{
 *  object MyModel extends Model("mymodel") {
 *   val sensor1 = source[Int]("source1")
 *   val sensor2 = source[Int]("source2")
 *   val recmax = source1 --> max(10) --> rec(10)
 *   val recmean = source2 --> mean(100) --> rec(10)
 *  }
 *  }}}
 *
 *  Then in some other code you inject data into the model:
 *
 *  {{{
 *  MyModel.sensor1 << 10
 *  MyModel.sensor2 << 50
 *  MyModel.sensor2 << 20
 *  // or
 *  MyModel.sensor2 <<< 20, 30, 40, 50, -10
 *  }}}
 *
 *  Anytime you could read latest readings on any referenced instrument:
 *
 *  {{{
 *  println(MyModel.recmax.last)
 *  println(MyModel.recmean.last)
 *  MyModel.recmean.data.foreach(println)
 *  }}}
 *
 *  @see [[org.retistruen.view.ModelViewer]]
 */
class Model(val name: String)
    extends Building
    with InstrumentBuilding
    with CollectorBuilding
    with ReducerBuilding
    with MiscBuilding {

  def components: Seq[Named] = structure

  def component(name: String) = structure.find(_.name == name)

}
