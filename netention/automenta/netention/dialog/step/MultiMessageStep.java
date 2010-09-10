/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.dialog.step;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author seh
 */
public class MultiMessageStep extends MessageStep {


    public MultiMessageStep(String[] m, List<Choice> finalChoices) {
        super(m[0]);
        
        int numMessages = m.length;
        
        MessageStep[] msgs = new MessageStep[numMessages];
        msgs[0] = this;
        for (int i = 1; i < numMessages; i++) {
            msgs[i] = new MessageStep(m[i]);
            
            List<Choice> l = new LinkedList();
            l.add(new Choice("Next", msgs[i]));
            msgs[i-1].setChoices(l);
        }

    }

    public List<Choice> getChoices() {
        if (chosen == -1)
            return choices;
        else
            return null;
    }


}
