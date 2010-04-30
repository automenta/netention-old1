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
import automenta.netention.graph.SimpleDynamicDirectedGraph;
import automenta.netention.link.CreatedBy;
import automenta.netention.link.HasProperty;
import automenta.netention.link.PatternOf;
import automenta.netention.node.Creator;
import automenta.netention.node.PropertyNode;
import java.util.Iterator;

/**
 *
 * @author seh
 */
public class MetadataGrapher {

    public static void run(Self s, SimpleDynamicDirectedGraph<Node,Link> target, boolean creators, boolean mode, boolean patterns, boolean properties) {
        Iterator<Node> i = s.iterateDetails();
        while (i.hasNext()) {
            Node n = i.next();
            if (n instanceof Detail) {
                Detail d = (Detail)n;

                if (creators) {
                    target.addEdge(new CreatedBy(), d, new Creator(d));
                }

                if (mode) {
                    target.addEdge(new PatternOf(), d, getNode(d.getMode()));
                }

                if (patterns) {
                    for (String p : d.getPatterns()) {
                        target.addEdge(new PatternOf(), d, s.getPatterns().get(p));
                    }
                }

                if (properties) {
                    for (PropertyValue p : d.getProperties()) {
                        target.addEdge(new HasProperty(), d, new PropertyNode(p));
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
