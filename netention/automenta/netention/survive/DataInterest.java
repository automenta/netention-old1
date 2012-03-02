/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.survive;

/**
 * Contains current parameters decided by user defining their relative "interest" in a particular data source.
 * Can be extended with more parameters later.
 * @author seh
 */
@Deprecated public class DataInterest {
    private double importance;
    private double scale;

    public DataInterest(double importance, double scale) {
        super();
        setImportance(importance);
        setScale(scale);
    }
    
    public double getImportance() {
        return importance;
    }

    public void setImportance(double importance) {
        this.importance = importance;
    }

    public void setScale(double newScale) {
        this.scale = newScale;
    }

    public double getScale() {
        return scale;
    }
    
    
    
}
