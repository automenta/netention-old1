/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.swing.widget.email;

import automenta.netention.NMessage;
import automenta.netention.swing.util.SwingWindow;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import net.atlanticbb.tantlinger.shef.HTMLEditorPane;

/**
 * News mixer main panel
 * @author seh
 */
public class MessagePanel extends JPanel {
    private final MessageIndex messages;
    private final JSplitPane js;

    public class MessageIcon extends JPanel implements MouseListener {
        private final NMessage message;

        boolean selected = false;
        
        private MessageIcon(NMessage m) {
            super(new BorderLayout());
            
            this.message = m;
            
            JLabel l = new JLabel(m.getSubject());
            add(l);
            
            setToolTipText(getToolTip());
            
            addMouseListener(this);
            
            setSelected(MessagePanel.this.selected.contains(m));
        }

        public String getToolTip() {
            final int tt = 32;
            if (message.getContent().length() >= tt) {
                return message.getContent().substring(0, tt-1);
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
                    }
                    else {
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
    
    public MessagePanel(MessageIndex mi) {
        super(new BorderLayout());
        
        this.messages = mi;
        
        js = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        add(js);
        
        js.setLeftComponent(new JScrollPane(new IndexPanel()));
        js.setRightComponent(new JPanel());
        
        js.setDividerLocation(0.5f);
        
        updateUI();
        
    }
    
    public static void main(String[] args) throws IOException {
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
        if (selected.size() == 0) {
            js.setRightComponent(new JPanel());
        }
        else if (selected.size() == 1) {
            NMessage only = selected.iterator().next();
            List<NMessage> x =new LinkedList();
            x.add(only);
            js.setRightComponent(new JScrollPane(new MultiMessagePanel(x)));
        }
        else {
            js.setRightComponent(new JScrollPane(new MultiMessagePanel(selected)));            
        }
    }
            
//    public static class SingleMessagePanel extends JPanel {
//
//        //http://shef.sourceforge.net/
//        public SingleMessagePanel(NMessage n) {
//            super(new BorderLayout());
//
//            
//            HTMLEditorPane editor = new HTMLEditorPane();
//            
//            StringBuffer html = new StringBuffer();
//            html.append(getMessageHTML(n));
//            
//            editor.setText(html.toString());
//
//            add(editor, BorderLayout.CENTER);
//        }
//        
//                
//    }

    public static class MultiMessagePanel extends JPanel {

        public MultiMessagePanel(Collection<NMessage> n) {
            super(new BorderLayout());
            

            HTMLEditorPane editor = new HTMLEditorPane();            
            
            StringBuffer html = new StringBuffer();
            for (NMessage m : n) {
                html.append(getMessageHTML(m));
                html.append("<br/>");
            }
            
            editor.setText(html.toString());

            add(editor, BorderLayout.CENTER);

        }
        public static String getMessageHTML(NMessage m) {
            String s = "";
            s += "<h1>" + m.getSubject() + "</h1>";
            s += "<br/>";
            s += m.getContent();
            return s;
        }
                
    }

}
