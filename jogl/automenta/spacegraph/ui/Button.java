/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.spacegraph.ui;

import automenta.spacegraph.control.Pointer;
import automenta.spacegraph.control.Pressable;
import automenta.spacegraph.control.Touchable;
import automenta.spacegraph.shape.Rect;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author me
 */
public class Button extends Rect implements Pressable,  Touchable {

    private boolean touchable = true;
    private boolean pressed = false;
    private boolean touched = false;

    public interface ButtonAction {
        public void act(Pointer pointer, Button b);
    }
    
    List<ButtonAction> actions = new LinkedList();

    @Override
    public void onTouchChange(Pointer pointer, boolean touched) {
        this.touched = touched;
    }

    @Override
    public boolean isTouchable() {
        return touchable;
    }
    
    

    
    @Override
    public void onPressChange(Pointer pointer, boolean pressed) {
        this.pressed = pressed;
        if (!pressed) {
            onRelease();
            
            for (ButtonAction ba : actions) {
                ba.act(pointer, this);
            }

        } else {
            onPress();
        }
    }

    @Override
    public boolean isPressable() {
        return true;
    }
    
    public boolean isPressed() {
        return pressed;
    }

    public boolean isTouched() {
        return touched;
    }

    
    protected void onRelease() {
    }

    protected void onPress() {
    }

    public boolean addAction(ButtonAction a) {
        return actions.add(a);
    }

    public boolean removeAction(ButtonAction a) {
        return actions.remove(a);
    }
}
