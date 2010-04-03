/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.swing.widget;

import automenta.netention.Detail;
import automenta.netention.Link;
import automenta.netention.Self;
import automenta.netention.swing.Icons;
import automenta.netention.swing.util.JHyperLink;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author seh
 */
public class DetailLinksPanel extends JPanel {

    private final Self self;
    private final Detail detail;

    public class LinkPanel extends JPanel {
        private final Link link;

        private LinkPanel(Link l) {
            super(new FlowLayout(FlowLayout.LEFT));
            this.link = l;

            String otherID = getOther();

            Detail other = self.getDetails().get(otherID);

            JHyperLink la = new JHyperLink(other.getName() + " (" + l.toString() + ")", "", 1.2f);
            la.setIcon(Icons.getDetailIcon(other));
            add(la);

            JLabel s = new JLabel(((int)(link.getStrength() * 100.0)) + "%");
            add(s);

        }

        public String getOther() {
            if (link.getSource().equals(detail.getID()))
                return link.getTarget();
            return link.getSource();
        }

    }

    public DetailLinksPanel(Self s, Detail d) {
        super(new BorderLayout());

        this.self = s;
        this.detail = d;

        refresh();
    }

    protected void refresh() {
        removeAll();

        self.updateLinks(new Runnable() {

            @Override
            public void run() {
                List<Link> edges = new LinkedList();
                Collection<Link> inEdges = self.getLinks().getInEdges(detail);
                Collection<Link> outEdges = self.getLinks().getOutEdges(detail);

                if (inEdges != null) {
                    edges.addAll(inEdges);
                }
                if (outEdges != null) {
                    edges.addAll(outEdges);
                }

                if (edges.size() == 0) {
                    add(new JLabel("No links."), BorderLayout.CENTER);
                    return;
                }

                JPanel linkPanel = new JPanel();
                linkPanel.setLayout(new BoxLayout(linkPanel, BoxLayout.PAGE_AXIS));
                add(new JScrollPane(linkPanel), BorderLayout.CENTER);

                for (Link l : edges) {
                    addLink(linkPanel, l);
                }

                linkPanel.add(Box.createVerticalBox());
                updateUI();
            }
        }, detail);

    }

    protected void addLink(JPanel p, Link l) {
        p.add(new LinkPanel(l));
    }
}
