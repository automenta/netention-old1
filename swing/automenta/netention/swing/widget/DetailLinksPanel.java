/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.swing.widget;

import automenta.netention.Detail;
import automenta.netention.Link;
import automenta.netention.Self;
import java.awt.BorderLayout;
import java.util.Collection;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author seh
 */
public class DetailLinksPanel extends JPanel {
    private final Self self;
    private final Detail detail;

    public DetailLinksPanel(Self s, Detail d) {
        super(new BorderLayout());

        this.self = s;
        this.detail = d;

        refresh();
    }


    protected void refresh() {
        removeAll();

        Collection<Link> inEdges = self.getLinks().getInEdges(detail);
        Collection<Link> outEdges = self.getLinks().getOutEdges(detail);

//        if ((inEdges.size() == 0) && (outEdges.size() == 0)) {
//            add(new JLabel("No links."), BorderLayout.CENTER);
//            return;
//        }
//
//        for (Link l : inEdges) {
//            addLink(l);
//        }
//        for (Link l : outEdges) {
//            addLink(l);
//        }

    }

    protected void addLink(Link l) {
        
    }

}
