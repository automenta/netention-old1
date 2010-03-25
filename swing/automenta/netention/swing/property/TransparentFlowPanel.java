/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.swing.property;

import java.awt.FlowLayout;
import javax.swing.JPanel;

/**
 *
 * @author seh
 */
public class TransparentFlowPanel extends JPanel {

    public TransparentFlowPanel() {
        super(new FlowLayout());
        setOpaque(false);
    }

}
