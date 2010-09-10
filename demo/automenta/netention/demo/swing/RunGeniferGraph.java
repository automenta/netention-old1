/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.demo.swing;

import automenta.netention.plugin.genifer.GeniferGraph;
import automenta.netention.Link;
import automenta.netention.Node;
import automenta.netention.graph.NotifyingDirectedGraph;
import automenta.netention.graph.SeHHyperassociativeMap;
import automenta.netention.graph.ValueEdge;
import automenta.netention.plugin.genifer.GeniferGraph.FactNode;
import automenta.netention.plugin.genifer.GeniferGraph.RuleNode;
import automenta.netention.demo.Demo;
import automenta.netention.demo.Demo;
import automenta.netention.swing.GraphSpace;
import automenta.netention.swing.util.SwingWindow;
import automenta.spacegraph.control.FractalControl;
import automenta.spacegraph.video.SGPanel;
import automenta.spacegraph.math.linalg.Vec4f;
import automenta.spacegraph.shape.Rect;
import automenta.spacegraph.ui.GridRect;
import automenta.spacegraph.ui.PointerLayer;
import com.syncleus.dann.graph.DirectedEdge;
import genifer.Genifer;
import genifer.GeniferLisp;
import genifer.SimpleMemory;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javolution.context.ConcurrentContext;

/**
 *
 * @author me
 */
public class RunGeniferGraph<N, E extends DirectedEdge<N>> implements Demo {

    public static void main(String[] args) {
        SwingWindow sw = new SwingWindow(new RunGeniferGraph().newPanel(), 800, 600, true);

    }
    int depth = 3; //no more than 4 or 5
    int numDimensions = 2;

    public NotifyingDirectedGraph getGeniferGraph(Genifer gen, int maxLevels) {
        return new GeniferGraph(gen, maxLevels);
    }

    public JPanel newPanel() {
        ConcurrentContext.setConcurrency(Runtime.getRuntime().availableProcessors());

        GeniferLisp genifer = new GeniferLisp(new SimpleMemory());
        genifer.execute("INIT-TEST-MEM", genifer.getMemory());

        NotifyingDirectedGraph<Node, ValueEdge<Node, Link>> target = getGeniferGraph(genifer, depth);


        System.out.println(target.getNodes().size() + " : " + target.getEdges().size());
        SeHHyperassociativeMap layout = new SeHHyperassociativeMap(target, numDimensions, 0.15, true);
        //final JungGraphDrawer layout = new JungGraphDrawer(target, new ISOMLayout(new JungGraph(target)), 6, 6);
        //final JungGraphDrawer layout = new JungGraphDrawer(target, new  SpringLayout(new JungGraph(target)), 12, 12);
        //final JungGraphDrawer layout = new JungGraphDrawer(target, new  SpringLayout2(new JungGraph(target)), 12, 12);
        //final JungGraphDrawer layout = new JungGraphDrawer(target, new  FRLayout2(new JungGraph(target)), 12, 12);
        
//        FRLayout fr = new FRLayout(new JungGraph(target));
//        fr.setAttractionMultiplier(30.0f);
//        fr.setRepulsionMultiplier(0.1f);
//        final JungGraphDrawer layout = new JungGraphDrawer(target, fr, 18, 12);
        
        final GraphSpace graphCanvas = new GraphSpace(target, layout) {

            @Override
            public Rect newNodeRect(Object n) {
                return super.newNodeRect(n);
            }

            @Override
            protected void updateRect(Object n, Rect r) {
                if (n instanceof FactNode) {
                    FactNode f = (FactNode) n;
                    float w = (float) f.getProbability() + 0.5f;
                    r.setBackgroundColor(new Vec4f(w / 2f, w, w / 4f, 0.75f));
                    r.scale(w, w);
                } else if (n instanceof RuleNode) {
                    RuleNode f = (RuleNode) n;
                    float w = (float) f.getW() / 100.0f + 0.5f;
                    r.setBackgroundColor(new Vec4f(w, w / 2f, w / 4f, 0.75f));
                    r.scale(w, w);

                } else {
                    super.updateRect(n, r);
                }
            }
 
        };

        SGPanel j = new SGPanel(graphCanvas);

        graphCanvas.add(new FractalControl(j));
        graphCanvas.add(new PointerLayer(graphCanvas));

        graphCanvas.add(new GridRect(6, 6));

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(j, BorderLayout.CENTER);

        return panel;
    }

    @Override
    public String getName() {
        return "Genifer Logic Graphs";
    }

    @Override
    public String getDescription() {
        return "..";
    }
}
