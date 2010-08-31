/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.spacegraph.control;

import automenta.spacegraph.math.linalg.Vec2f;

/**
 *
 * @author me
 */
public interface Pressable {
    public void onPressChange(Pointer pointer, Vec2f world, boolean pressed);
}
