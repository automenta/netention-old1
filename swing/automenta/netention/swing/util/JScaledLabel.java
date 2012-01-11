/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.swing.util;

import javax.swing.JLabel;

/**
 *
 * @author seh
 */
public class JScaledLabel extends JLabel {

    public JScaledLabel(String s, float fontScale) {
        this(s, fontScale, 0);
    }
    
    public JScaledLabel(String s, float fontScale, int attrs) {
        super(s);
        setFont(getFont().deriveFont((float)(getFont().getSize() * fontScale)).deriveFont(attrs));
    }


}
