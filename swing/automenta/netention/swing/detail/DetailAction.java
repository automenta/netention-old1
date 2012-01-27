/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.swing.detail;

import automenta.netention.Detail;
import automenta.netention.Self;

/**
 *
 * @author seh
 */
public interface DetailAction {

    
    public boolean applies(Self s, Detail d);
    
    public String getLabel();
    public String getDescription();
    
    public Runnable getRun(Self s, Detail d);
    
}
