/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.action;

import automenta.netention.Detail;
import automenta.netention.Self;

/**
 *
 * @author seh
 */
abstract public class DetailAction extends Action {
    
    private String label;
    private String desc;
    
    public DetailAction() {
        this("");
    }

    public DetailAction(String label) {
        this.label = label;
        desc = "";
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
    
    public String getLabel() {
        return label;
    }
    
    public String getDescription() {
        return desc;
    }
    
    abstract public double applies(Detail p);
    
    abstract public Runnable getRun(Self s, Detail d);
    
    

}
