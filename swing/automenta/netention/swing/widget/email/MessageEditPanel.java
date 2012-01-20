/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.swing.widget.email;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import net.atlanticbb.tantlinger.shef.HTMLEditorPane;

/**
 *
 * @author seh
 */
public class MessageEditPanel extends JPanel {

    private final HTMLEditorPane editor;
    private final JTextField subject;

    public MessageEditPanel(String subj, String html) {
        super(new BorderLayout());

        int defaultSubjChars = 64;

        subject = new JTextField();
        subject.setText(subj);
        add(subject, BorderLayout.NORTH);

        editor = new HTMLEditorPane();

        editor.setText(html);

        add(editor, BorderLayout.CENTER);


        final Map<String, JToggleButton> toggles = new HashMap();
        JPanel sendToPanel = new JPanel(new BorderLayout());
        {
            JPanel p = new JPanel(new FlowLayout());
            {
//                    for (String a : messages.addresses) {
//                        JToggleButton b = new JToggleButton(a);
//                        toggles.put(a, b);
//                        b.addActionListener(new ActionListener() {
//                            @Override
//                            public void actionPerformed(ActionEvent e) {
//                            }                            
//                        });
//                        p.add(b);
//                    }
            }
            sendToPanel.add(p, BorderLayout.CENTER);

            JButton sendButton = new JButton("Send");
            sendButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    SwingUtilities.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            for (String a : toggles.keySet()) {
                                boolean b = toggles.get(a).isSelected();
                                if (b) {
                                    sendTo(a);
                                    toggles.get(a).setEnabled(false);
                                }
                            }
                        }
                    });
                }
            });
            sendToPanel.add(sendButton, BorderLayout.EAST);
        }
        add(sendToPanel, BorderLayout.SOUTH);
    }
        public void sendTo(String address) {
//            NMessage m = new NMessage();
//            m.setTo(address);
//            m.setSubject(subject.getText());
//            m.setContent("<html>" + editor.getText() + "</html>");
//            m.setWhen(new Date());
//            messages.send(m);
        }
}
