/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.dialog;

import automenta.netention.Node;
import java.util.LinkedList;
import java.util.List;

/**
 * a step or interaction
 * @param S state object that it can operate upon
 */
public interface Step extends Node {

    //public void setState(S s);


    public List<Step> getNextSteps();

    public void addStepWatcher(StepWatcher w);
    public void removeStepWatcher(StepWatcher w);

}
