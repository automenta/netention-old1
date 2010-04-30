/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.swing;

import automenta.netention.Link;
import automenta.netention.Node;
import automenta.netention.graph.SimpleDynamicDirectedGraph;
import automenta.netention.impl.MemorySelf;
import automenta.netention.linker.MetadataGrapher;
import automenta.netention.swing.util.SwingWindow;
import automenta.spacegraph.SGCanvas;
import automenta.spacegraph.SGPanel;
import com.syncleus.dann.graph.SimpleDirectedEdge;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javolution.context.ConcurrentContext;

/**
 *
 * @author seh
 */
public class RunGraphCanvasPanel<N, E extends SimpleDirectedEdge<N>>  extends SGCanvas {


    public static void main(String[] args) {

        ConcurrentContext.setConcurrency(Runtime.getRuntime().availableProcessors());

        MemorySelf self = new MemorySelf("me", "Me");
        new SeedSelfBuilder().build(self);

        //self.addPlugin(new Twitter());

        self.updateLinks(null);

        SimpleDynamicDirectedGraph<Node,Link> target = new SimpleDynamicDirectedGraph(self.getGraph());
        MetadataGrapher.run(self, target, true, true, true, true);

        JPanel j = new SGPanel(new GraphCanvas(target, 3));
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(j, BorderLayout.CENTER);
        panel.add(new JButton("X"), BorderLayout.SOUTH);
        
        SwingWindow sw = new SwingWindow(panel, 400, 400, true);

    }
}
