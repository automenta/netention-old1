/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.spacegraph.shape;

import com.sun.opengl.util.awt.TextRenderer;
import java.awt.Font;
import java.awt.geom.Rectangle2D;
import javax.media.opengl.GL2;

/**
 *
 * @author seh
 */
public class TextRect extends Rect {
    private TextRenderer textRenderer;
    private float textScaleFactor;
    private String text;

    public TextRect(String initialText) {
        super();
        this.text = initialText;
    }

    @Override
    public void draw(GL2 gl) {
        //super.draw(gl);

        if (textRenderer == null) {
            textRenderer = new TextRenderer(new Font("SansSerif", Font.PLAIN, 72));
            gl.glEnable(GL2.GL_DEPTH_TEST);
            textRenderer.setSmoothing(true);
            textRenderer.setUseVertexArrays(true);

            // Compute the scale factor of the largest string which will make
            // them all fit on the faces of the cube
            Rectangle2D bounds = textRenderer.getBounds("Bottom");
            float w = (float) bounds.getWidth();
            float h = (float) bounds.getHeight();
            textScaleFactor = 1.0f / (w * 1.1f);
        }

        gl.glPushMatrix();
        transform(gl);

        // Now draw the overlaid text. In this setting, we don't want the
        // text on the backward-facing faces to be visible, so we enable
        // back-face culling; and since we're drawing the text over other
        // geometry, to avoid z-fighting we disable the depth test. We
        // could plausibly also use glPolygonOffset but this is simpler.
        // Note that because the TextRenderer pushes the enable state
        // internally we don't have to reset the depth test or cull face
        // bits after we're done.
        textRenderer.begin3DRendering();
        gl.glEnable(GL2.GL_DEPTH_TEST);
        //gl.glEnable(GL2.GL_CULL_FACE);

        // Note that the defaults for glCullFace and glFrontFace are
        // GL_BACK and GL_CCW, which match the TextRenderer's definition
        // of front-facing text.
        Rectangle2D bounds = textRenderer.getBounds(text);
        float w = (float) bounds.getWidth();
        float h = (float) bounds.getHeight();
        textRenderer.draw3D(text,
            w / -2.0f * textScaleFactor,
            h / -2.0f * textScaleFactor,
            0.1f,
            textScaleFactor);
        textRenderer.end3DRendering();

        gl.glPopMatrix();
        

    }
}
