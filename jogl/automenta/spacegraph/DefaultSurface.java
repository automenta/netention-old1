/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.spacegraph;

import automenta.spacegraph.shape.Drawable;

/**
 *
 * @author seh
 */
public class DefaultSurface extends Surface {
    
    private final Space2D defaultSpace;
    
    public DefaultSurface() {
        super();
        defaultSpace = new Space2D();
        setSpace(defaultSpace);
    }

    public <D extends Drawable> D add(D d) {
        defaultSpace.add(d);
        return d;
    }

    public boolean remove(Drawable d) {
        return defaultSpace.remove(d);
    }



}
