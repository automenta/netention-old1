/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.swing.widget.step;

import automenta.netention.dialog.Step;
import automenta.netention.dialog.StepWatcher;
import automenta.netention.dialog.step.MessageStep;
import automenta.netention.dialog.step.MessageStep.Choice;
import automenta.netention.swing.widget.DialogPanel;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

/**
 *
 * @author seh
 */
public class MessageStepPanel extends AbstractStepPanel implements StepWatcher {
    private final MessageStep message;

    public MessageStepPanel(DialogPanel dialog, MessageStep messageStep) {
        super(dialog);
        
        this.message = messageStep;

        message.addStepWatcher(this);

        setMinimumSize(new Dimension(300, 200));
        setPreferredSize(new Dimension(300, 200));

        refresh();
    }

    @Override
    public void onStepChanged(Step step, boolean stateChanged, boolean nextStepsChanged) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                //refresh();
                getDialog().onStepPanelChanged(MessageStepPanel.this);
            }
        });
    }    

    protected void refresh() {
        getTop().removeAll();
        getTop().add(new JLabel("Message"));

        getContent().removeAll();
        getContent().add(new JLabel(message.getMessage()));

        getBottom().removeAll();
        
        int i = 0;
        List<Choice> choices = message.getChoices();
        if (choices!=null) {
            for (Choice c : message.getChoices()) {
                JButton b = new JButton(c.label);
                final int fi = i;
                b.addActionListener(new ActionListener() {
                    @Override public void actionPerformed(ActionEvent e) {
                        message.setChoice(fi);
                    }
                });

                getBottom().add(b);

                i++;

            }
        }


        updateUI();
    }
}
