/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.action;

import automenta.netention.Detail;
import automenta.netention.Pattern;
import automenta.netention.Self;

/**
 *
 * @author seh
 */
abstract public class Action {
    
    private Self self;

    public void onActionEnabled(Self s) {
        this.self = s;
    }
    
    public void onActionDisabled(Self s) {
        this.self = null;
    }
    
    public Self getSelf() {
        return self;
    }
    
    
}
