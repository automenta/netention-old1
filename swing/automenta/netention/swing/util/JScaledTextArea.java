/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.swing.util;

import javax.swing.JTextArea;

/**
 *
 * @author seh
 */
public class JScaledTextArea extends JTextArea {
    private final float originalFontSize;

    public JScaledTextArea() {
        super("");
        originalFontSize = (float)getFont().getSize();        
    }

    public JScaledTextArea(String t, float scale) {
        this();
        scaleFontSize(scale);
        setText(t);
    }
    
    public void scaleFontSize(float newScale) {
        setFont(getFont().deriveFont(originalFontSize * newScale));
    }

    
}
