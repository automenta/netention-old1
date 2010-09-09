/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.spacegraph.shape.util;

import automenta.spacegraph.DefaultSurface;
import automenta.spacegraph.shape.Rect;

/**
 *
 * @author me
 */
public class RectLayout {
    private final DefaultSurface surface;

    public RectLayout(DefaultSurface surface) {
        this.surface = surface;
    }
    
    public RectLayout withRectInScale(Rect parent, Rect subRect, float dx, float dy, float sx, float sy) {
        subRect.setCenter(new InRect(parent, dx, dy));
        subRect.setScale(new ScaleRect(parent, sx, sy));
        surface.add(subRect);
        return this;
    }
}
