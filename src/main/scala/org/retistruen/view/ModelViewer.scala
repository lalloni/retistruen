/*
 * Author: Pablo Lalloni <plalloni@gmail.com>
 * Created: 27/05/2011 19:35:14
 */
package org.retistruen.view

import edu.uci.ics.jung.algorithms.layout.ISOMLayout
import edu.uci.ics.jung.graph.util.EdgeType
import edu.uci.ics.jung.graph.{ DirectedSparseMultigraph, Graph }
import edu.uci.ics.jung.visualization.control.{ DefaultModalGraphMouse, ModalGraphMouse }
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller
import edu.uci.ics.jung.visualization.{ BasicVisualizationServer, VisualizationViewer }
import java.awt.{ Color, Dimension, Paint }
import javax.swing.JFrame
import org.apache.commons.collections15.Transformer
import org.retistruen.{ Emitter, Model, Named, Receiver, Source }

/** This is a simple visualizer of retistruen {org.retistruen.Model}.
 *
 *  To use it just pass your model to the constructor and call {ModelViewer.show} as in:
 *
 *  {{{
 *  val model = new Model("test model") { ... }
 *  new ModelViewer(model).show
 *  }}}
 *
 *  @author Pablo Lalloni <plalloni@gmail.com>
 */
class ModelViewer(model: Model) {

  private lazy val graph: Graph[Named, Int] = {
    var edge = 0
    val graph = new DirectedSparseMultigraph[Named, Int]
    for (named ← model.components) {
      graph.addVertex(named)
      named match {
        case emitter: Emitter[_] ⇒
          for (receiver ← emitter.registered) {
            if (!graph.containsVertex(receiver)) graph.addVertex(receiver)
            edge = edge + 1
            graph.addEdge(edge, named, receiver, EdgeType.DIRECTED)
          }
        case other ⇒
          if (!graph.containsVertex(other)) graph.addVertex(other)
      }
    }
    graph
  }

  def show = {
    val layout = new ISOMLayout(graph)
    layout.setSize(new Dimension(800, 600))

    val vv = new VisualizationViewer(layout)
    vv.setPreferredSize(new Dimension(800, 600))

    vv.getRenderContext.setVertexLabelTransformer(new ToStringLabeller);
    vv.getRenderContext.setVertexFillPaintTransformer(new Transformer[Named, Paint] {
      def transform(n: Named) = n match {
        case s: Source[_]   ⇒ Color.GREEN
        case e: Emitter[_]  ⇒ Color.ORANGE
        case r: Receiver[_] ⇒ Color.DARK_GRAY
      }
    })

    val gm = new DefaultModalGraphMouse
    gm.setMode(ModalGraphMouse.Mode.TRANSFORMING)
    vv.setGraphMouse(gm)
    vv.addKeyListener(gm.getModeKeyListener)

    val frame = new JFrame("[%s] Model" format model.name)
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
    frame.getContentPane.add(vv)
    frame.pack
    frame.setVisible(true)

  }

}
