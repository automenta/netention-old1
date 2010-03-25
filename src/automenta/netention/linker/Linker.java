package automenta.netention.linker;

import automenta.netention.Detail;
import automenta.netention.Link;
import edu.uci.ics.jung.graph.DirectedGraph;
import java.util.Collection;


/** a weaver is a process that semantically links stories in real-time */
public interface Linker {

    public DirectedGraph<Detail,Link> run(Collection<Detail> details);

}
