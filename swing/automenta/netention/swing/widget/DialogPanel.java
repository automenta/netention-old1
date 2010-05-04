/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.swing.widget;

import automenta.netention.dialog.Step;
import automenta.netention.dialog.step.MessageStep;
import automenta.netention.swing.widget.step.AbstractStepPanel;
import automenta.netention.swing.widget.step.MessageStepPanel;
import java.awt.BorderLayout;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author seh
 */
public class DialogPanel extends JPanel {
    private final Step root;
    private final JPanel content;

    public DialogPanel(Step initialStep) {
        super(new BorderLayout());

        this.root = initialStep;

        JPanel topPanel = new JPanel();
        add(topPanel, BorderLayout.NORTH);

        content = new JPanel();
        BoxLayout bl = new BoxLayout(content, BoxLayout.PAGE_AXIS);
        content.setLayout(bl);
        
        JScrollPane contentScroll = new JScrollPane(content);
        add(contentScroll, BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel();
        add(bottomPanel, BorderLayout.SOUTH);

        refresh();

    }

    protected void refresh() {
        content.removeAll();

        renderContent(content, root);

        updateUI();
    }

    protected void renderContent(JPanel target, Step step) {
        JComponent n = null;

        if (step instanceof MessageStep) {
            n = new MessageStepPanel(this, (MessageStep)step);
        }
        else {
            n = new JLabel(step.toString());
        }

        int marginTop = 8;
        int marginBottom = 8;
        int marginLeft = 8;
        int marginRight = 8;

        if (n != null) {
            JPanel c = new JPanel(new BorderLayout());
            c.setBorder(new EmptyBorder(marginTop, marginLeft, marginBottom, marginRight));
            c.add(n, BorderLayout.CENTER);;
            target.add(c);            
        }

        List<Step> nextSteps = step.getNextSteps();
        if (nextSteps!=null) {
            for (Step s : nextSteps) {
                renderContent(target, s);
            }
        }

        content.add(Box.createVerticalBox());
    }

    //TODO use a more efficient method of changing instead of refreshing entire component
    public void onStepPanelChanged(AbstractStepPanel p) {
        refresh();
    }
}
