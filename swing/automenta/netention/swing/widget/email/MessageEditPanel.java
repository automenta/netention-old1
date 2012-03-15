/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.swing.widget.email;

import automenta.netention.NMessage;
import automenta.netention.Self;
import automenta.netention.Session;
import automenta.netention.email.EMailChannel;
import automenta.netention.feed.TwitterChannel;
import automenta.netention.swing.util.JScaledTextArea;
import automenta.netention.swing.util.SwingWindow;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import net.atlanticbb.tantlinger.shef.HTMLEditorPane;
import org.jsoup.Jsoup;
import twitter4j.TwitterException;

/**
 *
 * @author seh
 */
public class MessageEditPanel extends JPanel {

    private final HTMLEditorPane editor;
    private final JTextArea subject;
    private final Self self;

    Session session;
    private TwitterChannel tc = null;
    
    public MessageEditPanel() {
        this(null, "", "");
    }
    
    public TwitterChannel getTwitter() {
        if (tc == null) {
            tc = new TwitterChannel(); 
            tc.setKey(Session.get("twitter.key"));
        }
        return tc;
    }

    public MessageEditPanel(Self self, String subj, String html) {
        super(new BorderLayout());

        this.self = self;

        final EMailChannel ec = new EMailChannel();
        ec.setFrom(Session.get("email.email"));
        ec.setUsername(Session.get("email.username"));
        ec.setPassword(Session.get("email.password"));
        ec.setSmtpServer(Session.get("email.smtpServer"));
        ec.setServer(Session.get("email.server"));
        
        int defaultSubjChars = 64;

        subject = new JScaledTextArea(subj, 2.0f);
        subject.setToolTipText("Subject");

        JPanel subjectWrapper = new JPanel(new BorderLayout());
        subjectWrapper.add(subject, BorderLayout.CENTER);
        int w = 8;
        subjectWrapper.setBorder(BorderFactory.createEmptyBorder(w, w, w, w));

        add(subjectWrapper, BorderLayout.NORTH);

        editor = new HTMLEditorPane();

        editor.setText(html);

        add(editor, BorderLayout.CENTER);


        final Map<String, JToggleButton> toggles = new HashMap();
        JPanel sendToPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
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

            JButton configButton = new JButton("Configure");            
            sendToPanel.add(configButton);
            
            JButton newButton = new JButton("Save to New Detail");
            sendToPanel.add(newButton);
            
            JButton tweetButton = new JButton("Tweet...");
            tweetButton.addActionListener(new ActionListener() {
                @Override public void actionPerformed(ActionEvent e) {
                    tweet();
                }                
            });
            sendToPanel.add(tweetButton);
            
            final JTextField emailArea = new JTextField(24);
            emailArea.setToolTipText("E-Mail Address");
            sendToPanel.add(emailArea);
            
            JButton emailButton = new JButton("E-Mail...");
            emailButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        ec.sendMessage(getMessage(emailArea.getText()));
                    } catch (Exception ex) {
                        Logger.getLogger(MessageEditPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            
//            emailButton.addActionListener(new ActionListener() {
//
//                @Override
//                public void actionPerformed(ActionEvent e) {
//                    SwingUtilities.invokeLater(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            for (String a : toggles.keySet()) {
//                                boolean b = toggles.get(a).isSelected();
//                                if (b) {
//                                    sendTo(a);
//                                    toggles.get(a).setEnabled(false);
//                                }
//                            }
//                        }
//                    });
//                }
//            });
            sendToPanel.add(emailButton);
        }
        add(sendToPanel, BorderLayout.SOUTH);
    }

    protected void tweet() {
        String s = Jsoup.parse(editor.getText()).text();
        if (s.length() > 140) {
            JOptionPane.showMessageDialog(null, "Message greater than 140 chars: " + s.length());
            return;
        }
        
        try {
            getTwitter().getTwitter().updateStatus(s);
            Logger.getLogger(TwitterChannel.class.getName()).info("Status updated:" + " "+ s);
        } catch (TwitterException ex) {
            Logger.getLogger(MessageEditPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public NMessage getMessage(String address) {
            NMessage m = new NMessage();
            m.setTo(address);
            m.setName(subject.getText());
            m.setContent("<html>" + editor.getText() + "</html>");
            m.setWhen(new Date());
            return m;
    }

    public static void main(String[] args) {
        Session.init();
        new SwingWindow(new MessageEditPanel(), 800, 600, true);
    }
}
