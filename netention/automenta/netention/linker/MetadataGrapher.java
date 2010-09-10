/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.linker;

import automenta.netention.Detail;
import automenta.netention.Link;
import automenta.netention.Mode;
import automenta.netention.Node;
import automenta.netention.Pattern;
import automenta.netention.PropertyValue;
import automenta.netention.Self;
import automenta.netention.graph.ValueEdge;
import automenta.netention.link.CreatedBy;
import automenta.netention.link.HasProperty;
import automenta.netention.link.PatternOf;
import automenta.netention.node.Creator;
import automenta.netention.node.PropertyNode;
import com.syncleus.dann.graph.MutableAdjacencyGraph;
import com.syncleus.dann.graph.MutableBidirectedGraph;
import java.util.Iterator;

/**
 *
 * @author seh
 */
public class MetadataGrapher {

    public static void run(Self s, MutableAdjacencyGraph<Node, ValueEdge<Node, Link>> target, boolean creators, boolean mode, boolean patterns, boolean properties) {
        Iterator<Node> i = s.iterateDetails();
        while (i.hasNext()) {
            Node n = i.next();
            if (n instanceof Detail) {
                Detail d = (Detail)n;

                target.add(d);

                if (creators) {
                    Creator c = new Creator(d);
                    target.add(c);
                    target.add(new ValueEdge(new CreatedBy(), d, c));
                }

                if (mode) {
                    final Node t = getNode(d.getMode());
                    target.add(t);
                    target.add(new ValueEdge(new PatternOf(), d, t));
                }

                if (patterns) {
                    for (String p : d.getPatterns()) {
                        final Pattern pa = s.getPatterns().get(p);
                        target.add(pa);
                        target.add(new ValueEdge(new PatternOf(), d, pa));
                    }
                }

                if (properties) {
                    for (PropertyValue p : d.getProperties()) {
                        final PropertyNode pn = new PropertyNode(p);
                        target.add(pn);
                        target.add(new ValueEdge(new HasProperty(), d, pn));
                    }
                }
            }
        }
    }

    public static Node getNode(Mode m) {
        if (m == Mode.Real) {
            return new Pattern("real");
        }
        else if (m == Mode.Imaginary) {
            return new Pattern("imaginary");
        }
        else /*if (m == Unknown)*/ {
            return new Pattern("thought");
        }
    }

}
