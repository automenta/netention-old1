package automenta.netention.plugin.genifer;

import automenta.netention.Link;
import automenta.netention.Node;
import automenta.netention.Node.StringNode;
import automenta.netention.graph.NotifyingDirectedGraph;
import automenta.netention.graph.ValueEdge;
import automenta.netention.link.Next;
import automenta.netention.swing.RunGeniferGraph;
import genifer.Fact;
import genifer.Formula;
import genifer.Genifer;
import genifer.Rule;
import genifer.Sexp;
import org.armedbear.lisp.Cons;
import org.armedbear.lisp.LispObject;

public class GeniferGraph extends NotifyingDirectedGraph<Node, ValueEdge<Node, Link>> {

    private final Genifer gen;

    public GeniferGraph(Genifer gen, int maxLevels) {
        super();
        this.gen = gen;
        update(maxLevels);
    }

    protected void update(int maxLevels) {
        clear();
        for (Fact f : gen.getMemory().getFacts()) {
            updateNode(f, maxLevels - 1);
        }
        for (Rule rule : gen.getMemory().getRules()) {
            updateNode(rule, maxLevels - 1);
        }
    }

    protected void updateNode(Rule r, int i) {
        if (i == 0) {
            return;
        }
        Formula formula = r.formula;
        if (formula instanceof Sexp) {
            Sexp s = (Sexp) formula;
            Node parent = new RunGeniferGraph.RuleNode(r);
            add(parent);
            updateLispObject(parent, s.cons.car, i - 1);
            updateLispObject(parent, s.cons.cdr, i - 1);
        }
    }

    protected void updateNode(Fact f, int i) {
        if (i == 0) {
            return;
        }
        Formula formula = f.formula;
        if (formula instanceof Sexp) {
            Sexp s = (Sexp) formula;
            Node parent = new RunGeniferGraph.FactNode(f);
            add(parent);
            updateLispObject(parent, s.cons.car, i - 1);
            updateLispObject(parent, s.cons.cdr, i - 1);
        }
    }

    protected void updateLispObject(Node parent, LispObject l, int i) {
        if (i == 0) {
            return;
        }
        if (l == null) {
            return;
        }
        Node lNode = getNode(l);
        add(lNode);
        add(new ValueEdge<Node, Link>(new Next(), parent, lNode));
        if (l instanceof Cons) {
            Cons c = (Cons) l;
            updateLispObject(lNode, c.car, i - 1);
            updateLispObject(lNode, c.cdr, i - 1);
        }
    }

    private Node getNode(LispObject l) {
        if (l instanceof Cons) {
            return new StringNode("cons-" + l.hashCode());
        }
        return new StringNode(l.writeToString());
    }
}
