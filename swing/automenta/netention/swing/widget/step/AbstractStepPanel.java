/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.swing.widget.step;

import automenta.netention.swing.widget.DialogPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

/**
 *
 * @author seh
 */
abstract public class AbstractStepPanel extends JPanel {

    private final JPanel content;
    private final JPanel top;
    private final JPanel bottom;
    private final DialogPanel dialog;

    public AbstractStepPanel(DialogPanel dialog) {
        super(new BorderLayout());

        this.dialog = dialog;

        setOpaque(true);
        setBackground(Color.ORANGE);
        setBorder(new LineBorder(Color.BLACK, 8));

        top = new JPanel();
        top.setOpaque(false);
        add(top, BorderLayout.NORTH);
        
        content = new JPanel();
        content.setOpaque(false);
        add(content, BorderLayout.CENTER);

        bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setOpaque(false);
        add(bottom, BorderLayout.SOUTH);

    }

    public JPanel getContent() {
        return content;
    }

    public JPanel getTop() {
        return top;
    }

    public JPanel getBottom() {
        return bottom;
    }

    public DialogPanel getDialog() {
        return dialog;
    }



    
}
