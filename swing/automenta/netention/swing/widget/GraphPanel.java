/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.swing.widget;

import automenta.netention.Detail;
import automenta.netention.Link;
import automenta.netention.Node;
import automenta.netention.Self;
import automenta.netention.graph.SimpleDynamicDirectedGraph;
import automenta.netention.linker.MetadataGrapher;
import automenta.netention.swing.GraphCanvas;
import automenta.spacegraph.SGPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author seh
 */
public class GraphPanel extends JPanel implements IndexView {
    private final Self self;

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
        
        SimpleDynamicDirectedGraph<Node,Link> target = new SimpleDynamicDirectedGraph(self.getGraph());
        MetadataGrapher.run(self, target, true, true, true, true);

        JPanel j = new SGPanel(new GraphCanvas(target, 3));
        add(j, BorderLayout.CENTER);

        j.setMinimumSize(new Dimension(1,1));
        setMinimumSize(new Dimension(1,1));
    }


}
