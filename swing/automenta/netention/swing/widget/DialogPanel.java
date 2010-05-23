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
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author seh
 */
public class DialogPanel extends JPanel {

    private final Step root;
    private final JPanel content;
    int marginTop = 8; /* per step */

    int marginBottom = 8;/* per step */

    int marginLeft = 16;/* per step */

    int marginRight = 16;/* per step */


    public DialogPanel(Step initialStep) {
        super(new BorderLayout());

        this.root = initialStep;

        JPanel topPanel = new JPanel();
        add(topPanel, BorderLayout.NORTH);

        content = new JPanel();
        BoxLayout bl = new BoxLayout(content, BoxLayout.PAGE_AXIS);
        //GridLayout bl = new GridLayout(1, 1);
        content.setLayout(bl);

        final JScrollPane contentScroll = new JScrollPane(content);
        contentScroll.addMouseMotionListener(new MouseMotionAdapter() {

            float lastX = -1;
            float lastY = -1;

            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);

                lastX = -1;
            }


            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);

                float x = e.getX();
                float y = e.getY();

                final float dx, dy;
                
                if (lastX == -1)  {
                    dx = 0;
                    dy = 0;
                }
                else {
                    dx = x - lastX;
                    dy = y - lastY;
                }

                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        contentScroll.getVerticalScrollBar().setValue(contentScroll.getVerticalScrollBar().getValue() - (int) dy);
                    }
                });

                lastX = x;
                lastY = y;
            }
        });
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
            n = new MessageStepPanel(this, (MessageStep) step);
        } else {
            n = new JLabel(step.toString());
        }


        if (n != null) {
            JPanel c = new JPanel(new BorderLayout());
            c.setBorder(new EmptyBorder(marginTop, marginLeft, marginBottom, marginRight));
            c.add(n, BorderLayout.CENTER);
            target.add(c);
        }

        List<Step> nextSteps = step.getNextSteps();
        if (nextSteps != null) {
            for (Step s : nextSteps) {
                renderContent(target, s);
            }
        }

        //content.add(Box.createVerticalBox());
    }

    //TODO use a more efficient method of changing instead of refreshing entire component
    public void onStepPanelChanged(AbstractStepPanel p) {
        refresh();
    }
}
