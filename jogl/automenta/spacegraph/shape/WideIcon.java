/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.spacegraph.shape;

import automenta.spacegraph.gleem.linalg.Vec3f;
import javax.media.opengl.GL2;

/**
 *
 * @author seh
 */
public class WideIcon extends Rect {

    private final TextRect textRect;
    int MAX_LABEL_LENGTH = 32;
    
    public WideIcon(String label, Vec3f backgroundColor, Vec3f textColor) {
        super();

        setBackgroundColor(backgroundColor);

        if (label.length() > MAX_LABEL_LENGTH) {
            label = label.substring(0, MAX_LABEL_LENGTH);
        }

        textRect = new TextRect(label) {
            @Override public Vec3f getCenter() {
                return WideIcon.this.getCenter();
            }
            @Override public Vec3f getSize() {
                return WideIcon.this.getSize();
            }
            @Override public Vec3f getRotation() {
                return WideIcon.this.getRotation();
            }
        };

        setTextColor(textColor);

    }

    private void setTextColor(Vec3f textColor) {
        textRect.setTextColor(textColor);
    }

    @Override
    public void draw(GL2 gl) {
        super.draw(gl);

        textRect.draw(gl);
    }




}
