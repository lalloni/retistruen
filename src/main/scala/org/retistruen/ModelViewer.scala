/*
 * Author: Pablo Lalloni <plalloni@gmail.com>
 * Created: 27/05/2011 19:35:14
 */
package org.retistruen

import edu.uci.ics.jung.algorithms.layout.CircleLayout
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position
import edu.uci.ics.jung.visualization.BasicVisualizationServer
import java.awt.{ Stroke, BasicStroke, Color, Paint, Dimension }
import javax.swing.JFrame
import org.apache.commons.collections15.Transformer
import edu.uci.ics.jung.algorithms.layout.ISOMLayout
import edu.uci.ics.jung.algorithms.layout.DAGLayout
import edu.uci.ics.jung.algorithms.layout.TreeLayout
import edu.uci.ics.jung.algorithms.layout.KKLayout
import edu.uci.ics.jung.algorithms.layout.FRLayout
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse
import edu.uci.ics.jung.visualization.control.ModalGraphMouse
import edu.uci.ics.jung.visualization.VisualizationViewer
import edu.uci.ics.jung.graph.util.Context
import edu.uci.ics.jung.graph.Graph

/**
 * @author Pablo Lalloni <plalloni@gmail.com>
 * @since 27/05/2011 19:35:15
 */
class ModelViewer(model: Model) {

  def view = {
    val layout = new ISOMLayout(model.graph)
    layout.setSize(new Dimension(800, 600))

    val vv = new VisualizationViewer(layout)
    vv.setPreferredSize(new Dimension(800, 600))

    vv.getRenderContext.setVertexLabelTransformer(new ToStringLabeller);
    vv.getRenderContext.setVertexFillPaintTransformer(new Transformer[Named, Paint] {
      def transform(n: Named) = n match {
        case s: SourceEmitter[_] ⇒ Color.GREEN
        case e: Emitter[_]       ⇒ Color.ORANGE
        case r: Receiver[_]      ⇒ Color.DARK_GRAY
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
