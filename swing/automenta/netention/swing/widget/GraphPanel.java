/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.swing.widget;

import automenta.netention.Detail;
import automenta.netention.Link;
import automenta.netention.Node;
import automenta.netention.Self;
import automenta.netention.graph.ValueEdge;
import automenta.netention.linker.MetadataGrapher;
import automenta.netention.swing.GraphCanvas;
import automenta.spacegraph.SGPanel;
import automenta.spacegraph.gleem.linalg.Vec3f;
import com.syncleus.dann.graph.MutableBidirectedGraph;
import com.syncleus.dann.graph.MutableDirectedAdjacencyGraph;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JPanel;

/**
 *
 * @author seh
 */
public class GraphPanel extends JPanel implements IndexView {
    private final Self self;
        int minWidth = 150;

    public GraphPanel(Self self) {
        super(new BorderLayout());

        this.self = self;
    }

    @Override
    public void selectObject(Detail d) {
    }

    @Override
    public void refresh() {
        removeAll();
        
        MutableBidirectedGraph<Node,ValueEdge<Node, Link>> target = new MutableDirectedAdjacencyGraph<Node, ValueEdge<Node, Link>>(self.getGraph());
        MetadataGrapher.run(self, target, true, true, true, true);

        GraphCanvas gc = new GraphCanvas(target, 3);
        gc.setBackground(new Vec3f(0.2f, 0.2f, 0.2f));
        

        JPanel j = new SGPanel(gc);
        add(j, BorderLayout.CENTER);

        j.setMinimumSize(new Dimension(minWidth,minWidth));
        setMinimumSize(new Dimension(minWidth,minWidth));

        updateUI();
    }


}
