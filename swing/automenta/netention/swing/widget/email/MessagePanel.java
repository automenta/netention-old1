/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.swing.widget.email;

import automenta.netention.Channel;
import automenta.netention.NMessage;
import automenta.netention.swing.util.JScaledLabel;
import automenta.netention.swing.util.SwingWindow;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import net.atlanticbb.tantlinger.shef.HTMLEditorPane;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

/**
 * News mixer main panel
 * @author seh
 */
public class MessagePanel extends JPanel {

    private final MessageIndex messages;
    private final JSplitPane js;
    private MultiMessagePanel messagePanel;
    private final IndexPanel indexPanel;

    public class MessageIcon extends JPanel implements MouseListener {

        private final NMessage message;
        boolean selected = false;

        private MessageIcon(NMessage m) {
            super(new BorderLayout());

            this.message = m;

            setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

            JLabel l = new JScaledLabel(m.getSubject(), 1.25f, Font.BOLD);
            add(l, BorderLayout.CENTER);
            JLabel l2 = new JLabel(m.getTo());
            add(l2, BorderLayout.SOUTH);

            setToolTipText(getToolTip());

            addMouseListener(this);

            setSelected(MessagePanel.this.selected.contains(m));
        }

        public String getToolTip() {
            final int tt = 32;
            if (message.getContent().length() >= tt) {
                return message.getContent().substring(0, tt - 1);
            }
            return message.getContent();
        }

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            setSelected(!selected);
        }

        public void setSelected(boolean b) {
            this.selected = b;

            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    if (selected) {
                        setBackground(Color.orange);
                        select(message);
                    } else {
                        setBackground(Color.lightGray);
                        unselect(message);
                    }
                }
            });

        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    }

    public class IndexPanel extends JPanel {

        public IndexPanel() {
            super();

            setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

            refresh();
        }

        public void refresh() {
            removeAll();

            for (NMessage m : messages.iterateAll()) {
                add(new MessageIcon(m));
            }
            add(Box.createVerticalGlue());
        }
    }
    Set<NMessage> selected = new HashSet();
    JPanel right = new JPanel(new BorderLayout());

    
    public class AddressesPanel extends JPanel {
        protected final JTextArea ja;

        public AddressesPanel(final JDialog jd) {
            super(new BorderLayout());
            
            ja = new JTextArea(dataToString());
            add(ja, BorderLayout.CENTER);
            
            JButton saveButton = new JButton("Save");
            saveButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    stringToData();     
                    jd.hide();
                }                
            });
            add(saveButton, BorderLayout.SOUTH);
        }
        
        public String dataToString() {
            String s = "";
            for (String a : messages.getAddresses()) {
                s += a + "\n";
            }
            return s;
        }
        
        public void stringToData() {                    
            String[] a = ja.getText().split("\n");
            
            messages.getAddresses().clear();
            for (String s : a) {
                messages.getAddresses().add(s);
            }
        }
        
    }
    public class FeedsPanel extends AddressesPanel {

        public FeedsPanel(JDialog jd) {
            super(jd);
        }

        @Override
        public String dataToString() {
            String s = "";
            for (String a : messages.getFeeds()) {
                s += a + "\n";
            }
            return s;
        }

        @Override
        public void stringToData() {
            String[] a = ja.getText().split("\n");
            
            messages.getFeeds().clear();
            for (String s : a) {
                messages.getFeeds().add(s);
            }
        }
        
        
    }
    
    public class UpdatePanel extends JPanel {

        public UpdatePanel(JDialog j) {
            super(new BorderLayout());

            final JTextArea jp = new JTextArea();
            add(jp, BorderLayout.CENTER);
            
            
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    for (Channel c : messages.getChannels()) {

                        jp.append(c.toString() + "...");
                        List<NMessage> n = c.getMessages();
                        messages.addAll( n );
                        jp.append("done. (" + n.size() + ")\n");
                    }

                    jp.append("FINISHED");
                    indexPanel.refresh();
                }
                
            });
            
            j.hide();
            
        }
        
    }
    
    public MessagePanel(MessageIndex mi) {
        super(new BorderLayout());

        this.messages = mi;

        JMenuBar menuBar = new JMenuBar();
        {
            JMenuItem update = new JMenuItem("Update");
            update.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JDialog jd = new JDialog();
                    jd.setSize(600, 400);
                    UpdatePanel ap = new UpdatePanel(jd);
                    jd.getContentPane().add(ap);
                    jd.setVisible(true);                    
                }                
            });
            menuBar.add(update);
        }
        {
            JMenuItem feeds = new JMenuItem("Feeds");
            feeds.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JDialog jd = new JDialog();
                    jd.setSize(600, 400);
                    FeedsPanel ap = new FeedsPanel(jd);
                    jd.getContentPane().add(new JScrollPane(ap));
                    jd.setVisible(true);                    
                }                
            });
            menuBar.add(feeds);
        }
        {
            JMenuItem addresses = new JMenuItem("Addresses");
            addresses.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JDialog jd = new JDialog();
                    jd.setSize(600, 400);
                    AddressesPanel ap = new AddressesPanel(jd);
                    jd.getContentPane().add(ap);
                    jd.setVisible(true);                    
                }                
            });
            menuBar.add(addresses);
        }
        {
            JMenuItem addresses = new JMenuItem("EMail");
            addresses.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    EMailParametersDialog ap = new EMailParametersDialog(messages.emailChannel, null);
                    ap.setSize(600, 400);
                    ap.setVisible(true);                    
                }                
            });
            menuBar.add(addresses);
        }
        add(menuBar, BorderLayout.NORTH);
        
        js = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        add(js, BorderLayout.CENTER);

        indexPanel = new IndexPanel();
        js.setLeftComponent(new JScrollPane(indexPanel));
        js.setRightComponent(right);

        js.setDividerLocation(0.3f);

        updateUI();

    }

    public static void main(String[] args) throws IOException {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }

        MessageIndex mi = MessageIndex.load(System.getProperty("user.home") + "/.netention.messages");

        //mi.addAll(new CraigslistChannel("pittsburgh", "eng").update());


        new SwingWindow(new MessagePanel(mi), 1024, 800, true);

    }

    public void select(NMessage m) {
        selected.add(m);
        refresh();
    }

    public void unselect(NMessage m) {
        selected.remove(m);
        refresh();
    }

    public void refresh() {
        messagePanel = null;

        right.removeAll();
        if (selected.size() == 0) {
        } else if (selected.size() == 1) {
            NMessage only = selected.iterator().next();
            List<NMessage> x = new LinkedList();
            x.add(only);
            messagePanel = new MultiMessagePanel(x);
            right.add(messagePanel, BorderLayout.CENTER);
        } else {
            messagePanel = new MultiMessagePanel(selected);
            right.add(messagePanel, BorderLayout.CENTER);
        }
        updateUI();

    }

    public class EditMessagePanel extends JPanel {
        private final HTMLEditorPane editor;
        private final JTextField subject;

        public EditMessagePanel(String html) {
            super(new BorderLayout());

            int defaultSubjChars = 64;
            
            subject = new JTextField();
            subject.setText(Jsoup.clean(html, Whitelist.simpleText()).substring(0, defaultSubjChars));
            add(subject, BorderLayout.NORTH);
            
            editor = new HTMLEditorPane();

            editor.setText(html);

            add(editor, BorderLayout.CENTER);

            
            final Map<String, JToggleButton> toggles = new HashMap();
            JPanel sendToPanel = new JPanel(new BorderLayout());
            {
                JPanel p = new JPanel(new FlowLayout());
                {
                    for (String a : messages.addresses) {
                        JToggleButton b = new JToggleButton(a);
                        toggles.put(a, b);
                        b.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                            }                            
                        });
                        p.add(b);
                    }
                    
                }
                sendToPanel.add(p, BorderLayout.CENTER);
                
                JButton sendButton = new JButton("Send");
                sendButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override public void run() {
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
            NMessage m = new NMessage();
            m.setTo(address);
            m.setSubject(subject.getText());
            m.setContent("<html>" + editor.getText() + "</html>");
            m.setWhen(new Date());
            messages.send(m);
        }
    }

    public class MultiMessagePanel extends JPanel {

        private final Collection<NMessage> messages;

        public MultiMessagePanel(Collection<NMessage> n) {
            super(new BorderLayout());

            this.messages = n;

            refresh();
        }

        public void refresh() {
            removeAll();


            JMenuBar jb = new JMenuBar();
            add(jb, BorderLayout.NORTH);


            final JEditorPane jp = new JEditorPane("text/html", getMessageHTML().toString());
            jp.setEditable(false);
            add(new JScrollPane(jp), BorderLayout.CENTER);

            JMenuItem editNow = new JMenuItem("Edit");
            editNow.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    SwingUtilities.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            String h = jp.getText();
                            new SwingWindow(new EditMessagePanel(h), 600, 400, false);
                        }
                    });
                }
            });
            jb.add(editNow);


            updateUI();
        }

        public StringBuffer getMessageHTML() {
            StringBuffer html = new StringBuffer("<html>");
            for (NMessage m : messages) {
                html.append(getMessageHTML(m));
                html.append("<br/>");
            }
            html.append("</html>");
            return html;
        }

        public String linkify(String u) {
            if (u.startsWith("http://")) {
                return "<a href=" + u + ">" + u + "</a>";
            }
            return u;
        }
        public String getMessageHTML(NMessage m) {
            String s = "";
            s += "<h1>" + m.getSubject() + "</h1>";
            if (m.getImage()!=null) {
                s += "<img src='" + m.getImage() + "'/>";
            }
            if (m.getId().length() > 0)
                s += "ID: " + linkify(m.getId()) + "<br/>";
            if (m.getTags().size() > 0)
                s += "Tags: " + m.getTags() + "<br/>";
            if (m.getFrom().length() > 0) 
                s += "From: " + m.getFrom() + "<br/>";
            if (m.getTo().length() > 0) 
                s += "To: " + linkify(m.getTo()) + "<br/>";
            if (m.getWhen()!=null) 
                s += "At: " + m.getWhen() + "<br/>";
            
            s += "<br/>";
            s += m.getContent();
            s += "<br/>";
            s += "<br/>";

            return s;
        }
    }
}
