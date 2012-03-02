/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.swing.util;

import javax.swing.JSlider;

/**
 *
 * @author seh
 */
public class JFloatSlider extends JSlider {
    public static final int MAXRESOLUTION = 100;
    private final double maxValue;
    private final double minValue;
    private double dvalue;

    public JFloatSlider(double value, double minValue, double maxValue, int orientation) {
        super();
        setName("X");
        this.maxValue = maxValue;
        this.minValue = minValue;
        setDoubleValue((double) value);
    }

    public int doubleToInt(double v) {
        if (v > maxValue) {
            v = maxValue;
        }
        if (v < minValue) {
            v = minValue;
        }
        return (int) ((v - minValue) / (maxValue - minValue) * ((double) MAXRESOLUTION));
    }

    public void setDoubleValue(double v) {
        this.dvalue = v;
        setValue(doubleToInt(dvalue));
    }

    public double value() {
        int v = getValue();
        return (((double) v) / ((double) MAXRESOLUTION)) * (maxValue - minValue) + minValue;
    }

    @Override
    public String getToolTipText() {
        return Double.toString(value());
    }
    
    
}
