/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.swing.detail;

import automenta.netention.swing.detail.action.CraigslistRefreshAction;
import automenta.netention.swing.detail.action.SendAction;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author seh
 */
public class SwingDetailActions {
    private List<DetailAction> actions = new LinkedList();

    public SwingDetailActions() {
        actions.add(new SendAction());
        actions.add(new CraigslistRefreshAction(false));
        actions.add(new CraigslistRefreshAction(true));
    }
    
    public List<DetailAction> getActions() {
        return actions;
    }
}
