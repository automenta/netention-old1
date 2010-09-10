/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.swing;

import automenta.netention.plugin.genifer.GeniferGraph;
import automenta.netention.Link;
import automenta.netention.Node;
import automenta.netention.Node.StringNode;
import automenta.netention.graph.NotifyingDirectedGraph;
import automenta.netention.graph.ValueEdge;
import automenta.netention.plugin.jung.JungGraph;
import automenta.netention.swing.RunDemos.Demo;
import automenta.netention.swing.util.SwingWindow;
import automenta.spacegraph.DefaultSurface;
import automenta.spacegraph.control.FractalControl;
import automenta.spacegraph.dimensional.JungGraphDrawer;
import automenta.spacegraph.impl.SGPanel;
import automenta.spacegraph.math.linalg.Vec4f;
import automenta.spacegraph.shape.Rect;
import automenta.spacegraph.ui.GridRect;
import automenta.spacegraph.ui.PointerLayer;
import com.syncleus.dann.graph.DirectedEdge;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import genifer.Fact;
import genifer.Genifer;
import genifer.GeniferLisp;
import genifer.Rule;
import genifer.SimpleMemory;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javolution.context.ConcurrentContext;

/**
 *
 * @author me
 */
public class RunGeniferGraph<N, E extends DirectedEdge<N>> extends DefaultSurface implements Demo {

    public static void main(String[] args) {
        SwingWindow sw = new SwingWindow(new RunGeniferGraph().newPanel(), 800, 600, true);

    }
    int depth = 4; //no more than 4 or 5
    int numDimensions = 2;

    public static class FactNode extends StringNode {

        public final Fact fact;

        public FactNode(Fact f) {
            super(f.toString());
            this.fact = f;
        }

        public double getProbability() {
            return fact.truth.getProbability();
        }
    }

    public static class RuleNode extends StringNode {

        public final Rule rule;

        public RuleNode(Rule r) {
            super(r.toString());
            this.rule = r;
        }

        public double getW() {
            return rule.getW();
        }
    }

    public NotifyingDirectedGraph getGeniferGraph(Genifer gen, int maxLevels) {
        return new GeniferGraph(gen, maxLevels);
    }

    public JPanel newPanel() {
        ConcurrentContext.setConcurrency(Runtime.getRuntime().availableProcessors());

        GeniferLisp genifer = new GeniferLisp(new SimpleMemory());
        genifer.execute("INIT-TEST-MEM", genifer.getMemory());

        NotifyingDirectedGraph<Node, ValueEdge<Node, Link>> target = getGeniferGraph(genifer, depth);


        System.out.println(target.getNodes().size() + " : " + target.getEdges().size());
        //layout = new SeHHyperassociativeMap(target, numDimensions, 0.15, true);
        //final JungGraphDrawer layout = new JungGraphDrawer(target, new ISOMLayout(new JungGraph(target)), 6, 6);
        //final JungGraphDrawer layout = new JungGraphDrawer(target, new  SpringLayout(new JungGraph(target)), 12, 12);
        //final JungGraphDrawer layout = new JungGraphDrawer(target, new  SpringLayout2(new JungGraph(target)), 12, 12);
        //final JungGraphDrawer layout = new JungGraphDrawer(target, new  FRLayout2(new JungGraph(target)), 12, 12);
        
        FRLayout fr = new FRLayout(new JungGraph(target));
        fr.setAttractionMultiplier(30.0f);
        fr.setRepulsionMultiplier(0.1f);
        final JungGraphDrawer layout = new JungGraphDrawer(target, fr, 18, 12);
        
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

        new FractalControl(j);
        new PointerLayer(this);

        graphCanvas.add(new GridRect(6, 6));

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(j, BorderLayout.CENTER);

//        JButton pb = new JButton("+");
//        pb.addActionListener(new ActionListener() {
//
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                double n = layout.getEquilibriumDistance() * 1.1;
//                layout.resetLearning();
//                layout.setEquilibriumDistance(n);
//            }
//        });
//        JButton mb = new JButton("-");
//        mb.addActionListener(new ActionListener() {
//
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                double n = layout.getEquilibriumDistance() * 0.9;
//                layout.resetLearning();
//                layout.setEquilibriumDistance(n);
//            }
//        });

        //JPanel px = new JPanel(new FlowLayout());
//        px.add(mb);
//        px.add(pb);

        //panel.add(px, BorderLayout.SOUTH);

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
