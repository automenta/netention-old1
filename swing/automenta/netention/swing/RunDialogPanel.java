/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.swing;

import automenta.netention.dialog.step.MultiMessageStep;
import automenta.netention.swing.util.SwingWindow;
import automenta.netention.swing.widget.DialogPanel;

/**
 *
 * @author seh
 */
public class RunDialogPanel {

    public static void main(String[] args) {
        //MessageStep b = new MessageStep("ABC");
        //MessageStep a = new MessageStep("ABC", "Next", b);

        MultiMessageStep c = new MultiMessageStep(new String[] {
            "abc",
            "xyz",
            "123",
            "456"
        }, null);

        new SwingWindow(new DialogPanel(c), 400, 800, true);
    }
}
