/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.swing.widget;

import automenta.netention.Self;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author seh
 */
abstract public class AbstractNewPanel extends JPanel {
    public final Self self;

    public AbstractNewPanel(Self self) {
        super(new BorderLayout());
        this.self = self;

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        add(bottom, BorderLayout.SOUTH);

        JButton createButton = new JButton("Create");
        createButton.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                create();
                closeThis();
            }
        });
        createButton.requestFocus();
        createButton.setDefaultCapable(true);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                closeThis();
            }
        });

        bottom.add(cancelButton);
        bottom.add(createButton);

        JPanel center = new JPanel(new GridBagLayout());
        add(center, BorderLayout.CENTER);

        init(center);

    }

    abstract protected void create();
    abstract protected void init(JPanel center);

    public void closeThis() {
        //TODO  impl this
        throw new UnsupportedOperationException("impl closeThis()");
    }


}
