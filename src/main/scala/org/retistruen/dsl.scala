package org.retistruen

import edu.uci.ics.jung.graph.DirectedSparseMultigraph
import edu.uci.ics.jung.graph.DirectedSparseGraph
import edu.uci.ics.jung.graph.Graph
import edu.uci.ics.jung.graph.util.EdgeType._

class Model(val name: String) extends Named {

  private var comps: Set[Named] = Set.empty

  def components: Set[Named] = comps

  def component(name: String) = comps.find(_.name == name)

  override def toString = super.toString + "{" + graph.toString + "}"

  lazy val graph = {
    var edge = 0
    val graph = new DirectedSparseMultigraph[Named, Int]
    for (named ← components) {
      graph.addVertex(named)
      named match {
        case emitter: Emitter[_] ⇒
          for (receiver ← emitter.registered) {
            if (!graph.containsVertex(receiver)) graph.addVertex(receiver)
            edge = edge + 1
            graph.addEdge(edge, named, receiver, DIRECTED)
          }
        case other ⇒
          if (!graph.containsVertex(other)) graph.addVertex(other)
      }
    }
    graph
  }

  def source[T](name: String) =
    keep(new SourceEmitter[T](name))

  def rec[T] = { e: Emitter[T] ⇒
    keep(new RecordingReceiver[T](name(e, "rec")))
  }

  def rec[T](size: Int) = { e: Emitter[T] ⇒
    keep(new RecordingReceiver[T](name(e, "rec"), Some(size)))
  }

  def max[T: Ordering] = { e: Emitter[T] ⇒
    keep(new AbsoluteMax[T](name(e, "max")))
  }

  def max[T: Ordering](size: Int) = { e: Emitter[T] ⇒
    keep(new SlidingMax[T](name(e, "max" + size), size))
  }

  private def keep[N <: Named](named: N): N = {
    comps = comps + named
    named
  }

  private def name(previous: Emitter[_], name: String) = "%s.%s" format (previous.name, name)

  implicit def emitterConnector[T](emitter: Emitter[T]) = new Connector[T](emitter)

  class Connector[T](emitter: Emitter[T]) {
    def -->[Q <: Receiver[T]](builder: Emitter[T] ⇒ Q): Q = {
      val target = builder(emitter)
      emitter >> target
      target
    }
  }

}
