/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.spacegraph.ui;

import automenta.spacegraph.shape.TextRect;
import javax.media.opengl.GL2;

/**
 *
 * @author me
 */
public class TextButton extends Button {

    private String text;
    private TextRect tr;

    public TextButton(String label) {
        super();
        setText(label);
    }

    public void setText(String newText) {
        this.text = newText;
        tr = new TextRect(text);
    }

    @Override
    protected void drawFront(GL2 gl) {
        super.drawFront(gl);
        tr.draw(gl);
        
    }
    
    
    
}
