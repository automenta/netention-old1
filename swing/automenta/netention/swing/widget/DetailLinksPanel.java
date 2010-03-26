/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.swing.widget;

import automenta.netention.Detail;
import automenta.netention.Self;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author seh
 */
public class DetailLinksPanel extends JPanel {

    public DetailLinksPanel(Self s, Detail d) {
        super(new BorderLayout());
        add(new JLabel("No links."), BorderLayout.CENTER);
    }


}
