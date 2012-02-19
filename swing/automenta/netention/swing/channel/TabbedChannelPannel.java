/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.swing.channel;

import automenta.netention.NMessage;
import automenta.netention.Self;
import automenta.netention.demo.RunSelfBrowser;
import automenta.netention.impl.MemorySelf;
import automenta.netention.irc.IRCChannel;
import automenta.netention.irc.IRCServerConnection;
import automenta.netention.swing.detail.DetailEditPanel;
import automenta.netention.swing.util.JScaledTextArea;
import automenta.netention.swing.util.SwingWindow;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Date;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author seh
 */
public class TabbedChannelPannel extends JPanel {
    private final JTabbedPane tabs;
    private final JMenuBar menu;
    private final JButton newDetail;
    private Object[] selected;
    private final Self self;

    public TabbedChannelPannel(Self self) {
        super(new BorderLayout());
        
        this.self = self;
        
        add( tabs = new JTabbedPane(), BorderLayout.CENTER);

        add( menu = new JMenuBar(), BorderLayout.NORTH);

        JMenu connect = new JMenu("Connect");
        menu.add(connect);
            
        menu.add(newDetail = new JButton("Detail..."));
        newDetail.setEnabled(false);
        
        tabs.addChangeListener(new ChangeListener() {
            @Override public void stateChanged(ChangeEvent e) {
                newDetail.setEnabled(false);
            }            
        });
    }
    
    public class IRCPanel extends JPanel {
        private final String channel;
        private final IRCServerConnection c;
        private final IRCChannel chan;
        private final JScaledTextArea inputArea;
        private final JList output;
        private final DefaultListModel textModel;

        public IRCPanel(final IRCServerConnection c, final String channel) {
            super(new BorderLayout());
            this.c = c;
            this.channel = channel;
            
                             
            
            add(new JScrollPane(output = new JList( textModel = new DefaultListModel() )), BorderLayout.CENTER);
            
            add(inputArea = new JScaledTextArea("", 1.3f), BorderLayout.SOUTH);
            
            c.join(channel, chan = new IRCChannel() {

                @Override
                public void onMessage(String sender, String login, String hostname, String message) {
                    addMessage(sender, message);
                }
            });
            
            inputArea.addKeyListener(new KeyAdapter() {

                @Override
                public void keyReleased(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        final String m = inputArea.getText().trim();
                        inputArea.setText("");
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override public void run() {
                                if (m.length() > 0) {                                        
                                    send(m);
                                    addMessage(c.getNick(), m);
                                }
                            }                            
                        });
                    }
                }
                
            });
            
            output.setCellRenderer(new DefaultListCellRenderer() {

                @Override
                public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    JLabel l = (JLabel)super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    l.setFont(getFont().deriveFont(getFont().getSize2D() * 1.25f));
                    return l;
                }
                
            });
            
            //output.setSelectionMode(ListSelectionModel.);
            output.setFixedCellHeight(-1);
            
            output.addListSelectionListener(new ListSelectionListener() {
                @Override public void valueChanged(ListSelectionEvent e) {
                    Object[] vals = output.getSelectedValues();
                    if (vals != null) {
                        if (vals.length > 0) {
                            for (ActionListener al : newDetail.getActionListeners()) {
                                newDetail.removeActionListener(al);
                            }
                            newDetail.setEnabled(true);
                            newDetail.addActionListener(new ActionListener() {
                                @Override public void actionPerformed(ActionEvent e) {
                                    newDetail("Message", c.getServer() + "/" + channel, "" /* channel topic */);
                                }            
                            });
                            setSelected(vals);
                            return;
                        }
                    }
                    
                    newDetail.setEnabled(false);
                    
                        
                }

            });
        }
        
        public void addMessage(String sender, String message) {
            textModel.addElement(sender + ": " + message);            
        }
    
        public void send(String message) {
            chan.send(message);
        }
        
    }
    
    public void add(IRCServerConnection c, String chan) {
        IRCPanel p = new IRCPanel(c, chan);
        tabs.addTab(chan, p);        
        
        updateUI();
    }
    
    

    private void setSelected(Object[] vals) {
        this.selected = vals;
    }
    
    public void newDetail(String name, String from, String subject) {
     
        String message = "";
        int x = 0;
        for (Object o : selected)
            message += o.toString() + "<br/>";
        
        NMessage m = new NMessage(name, from, "", new Date(), subject, message );
        self.addDetail(m);
        
        new SwingWindow(new DetailEditPanel(self, m, true) {

            @Override
            protected void deleteThis() {
            }

            @Override
            protected void patternChanged() {
            }
            
        }, 400, 300);
    }
    
    public static void main(String[] args) throws Exception {
        
        Self s = RunSelfBrowser.newDefaultSelf();
        
        TabbedChannelPannel tcp = new TabbedChannelPannel(s);
                
        final IRCServerConnection c = new IRCServerConnection("xeehh_netz", "irc.freenode.net");
        
        tcp.add(c, "#b350f99700cd057aa9ab284c31b5eb2b");
        
        SwingWindow w = new SwingWindow(tcp, 600, 400, true) {

            @Override
            protected void onClosing() {
                c.disconnect();
            }
            
        };
        
    }
    
}
