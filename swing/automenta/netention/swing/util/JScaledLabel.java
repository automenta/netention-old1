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

    float originalFontSize;
    
    public JScaledLabel(String s, float fontScale) {
        this(s, fontScale, 0);
    }
    
    public JScaledLabel(String s, float fontScale, int attrs) {
        super(s);
        originalFontSize = (float)getFont().getSize();
        
        setFont(getFont().deriveFont(attrs));
        scaleFontSize(fontScale);
        
    }
    
    public void scaleFontSize(float newScale) {
        setFont(getFont().deriveFont(originalFontSize * newScale));
    }
    


}
