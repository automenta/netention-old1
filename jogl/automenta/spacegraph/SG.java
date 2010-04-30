package automenta.spacegraph;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.media.opengl.GLEventListener;

public abstract class SG implements GLEventListener, MouseMotionListener, MouseListener, KeyListener, MouseWheelListener {

    public interface SGListener {

        /** Indicates that the demo wants to be terminated. */
        public void shutdownDemo();

        /** Indicates that a repaint should be scheduled later. */
        public void repaint();
    }
    
    protected SGListener sgListener;
    private boolean doShutdown = true;

    public void setSGListener(SGListener listener) {
        this.sgListener = listener;
    }

    // Override this with any other cleanup actions
    public void shutdownDemo() {
        // Execute only once
        boolean shouldDoShutdown = doShutdown;
        doShutdown = false;
        if (shouldDoShutdown) {
            sgListener.shutdownDemo();
        }
    }
    public void mouseDragged(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }

    public void mouseWheelMoved(MouseWheelEvent e) {
    }

}
