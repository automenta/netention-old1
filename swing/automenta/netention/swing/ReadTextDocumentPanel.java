/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.swing;

import automenta.netention.Detail;
import automenta.netention.NMessage;
import automenta.netention.Self;
import automenta.netention.feed.RSSChannel;
import automenta.netention.feed.TwitterChannel;
import automenta.netention.swing.util.SwingWindow;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.*;

/**
 * multi-line text input on left, with some buttons to choose what type of processing
 * 
 * after processing, display JList in right, showing what details would be created if proceeds
 * with cancel and ok buttons
 * @author SeH
 */
public class ReadTextDocumentPanel extends JPanel {
    private final JTextArea textArea;
    private final JPanel resultPanel;

    private final JPanel list = new JPanel();
    private final JComboBox processSelect;
    private List<? extends Detail> ld;
    private Self self = null;
    private final JSplitPane split;
    
    //in drop-down
    // read as list of urls (bookmark)
    // read combined content of urls
    // sentence-ize
    // create prototype details
    // twitter search
    // yahoo image search
    // google search
    // process chat dialog (like tumblr)
    // as-is (one text blob)

    public static abstract class TextProcess {
        public final String name;
        
        public TextProcess(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    
        
        abstract public List<? extends Detail> get(String input);
    }
    
    public ReadTextDocumentPanel() {
        super(new BorderLayout(8,8));

        split = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        add(split, BorderLayout.CENTER);
        
        JPanel inputArea = new JPanel(new BorderLayout());
        {
            textArea = new JTextArea();
            inputArea.add(new JScrollPane(textArea), BorderLayout.CENTER);
            
            JPanel j = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            {
                processSelect = new JComboBox();
                
                j.add(processSelect, BorderLayout.CENTER);
                
                JButton processButton = new JButton("Process");             
                processButton.addActionListener( new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        process();
                    }                     
                });
                j.add(processButton, BorderLayout.EAST);
            }
            inputArea.add(j, BorderLayout.SOUTH);
            
            
        }
        split.setTopComponent(inputArea);

        resultPanel = new JPanel(new BorderLayout());
        {
            
            list.setLayout(new BoxLayout(list, BoxLayout.PAGE_AXIS));
            resultPanel.add(new JScrollPane(list), BorderLayout.CENTER);
            
            JPanel j = new JPanel(new FlowLayout());
            {
                JButton clearButton = new JButton("Clear");
                clearButton.addActionListener(new ActionListener() {
                    @Override public void actionPerformed(ActionEvent e) {
                        ld = null;
                        resultPanel.setVisible(false);
                    }
                });
                j.add(clearButton);
                
                JButton okButton = new JButton("Add");                             
                okButton.addActionListener(new ActionListener() {
                    @Override public void actionPerformed(ActionEvent e) {
                        if (ld!=null) {
                            commit(ld);
                        }
                        resultPanel.setVisible(false);
                    }
                });
                j.add(okButton);
            }
            resultPanel.add(j, BorderLayout.SOUTH);
            
        }

        split.setBottomComponent(resultPanel);
                
        resultPanel.setVisible(false);
        
    }

    public Self getSelf() {
        return self;
    }

    public void setSelf(Self self) {
        this.self = self;
    }
          
    protected void commit(List<? extends Detail> ld) {
        if (self!=null) {
            for (Detail d : ld)
                self.addDetail(d);
        }
    }
    
    protected void process() {
        TextProcess tp = (TextProcess)processSelect.getSelectedItem();
        
        ld = tp.get(textArea.getText());
        
        list.removeAll();
        if (ld!=null) {
            for (Detail d : ld) {
                list.add(new JLabel("<html>" + d.toString(32) + "</html>"));
            }
            resultPanel.setVisible(true);
            split.setDividerLocation(0.5f);
        }
        
        updateUI();
        
        
    }
    
    public void add(TextProcess p) {
        processSelect.addItem(p);
    }
    
    public static void main(String[] args) {
        ReadTextDocumentPanel rd = new ReadTextDocumentPanel();

        rd.add(new Sentenceize("Sentence-ize"));
        rd.add(new Paragraphize("Paragraph-ize"));
        rd.add(new TwitterSearch("Twitter Search"));
        
        new SwingWindow(rd, 800, 600, true);
    }

    public static class Sentenceize extends TextProcess {

        public Sentenceize(String name) {
            super(name);
        }

        @Override
        public List<Detail> get(String input) {
            List<Detail> l = new LinkedList();
            
            String[] s = input.split("\n");
            for (String r : s) {
                r = r.trim();
                if (r.length() == 0)
                    continue;
                l.add(new Detail(r));
            }
            
            return l;
        }
    }

    public static class Paragraphize extends TextProcess {

        public Paragraphize(String name) {
            super(name);
        }

        @Override
        public List<Detail> get(String input) {
            List<Detail> l = new LinkedList();
            
            String[] s = input.split("\n\n");
            for (String r : s) {
                r = r.trim();
                if (r.length() == 0)
                    continue;
                l.add(new Detail(r));
            }
            
            return l;
        }
    }

    public static class TwitterSearch extends TextProcess {

        public final static Logger logger = Logger.getLogger(TwitterSearch.class.toString());
        
        public TwitterSearch(String name) {
            super(name);
        }

        @Override
        public List<Detail> get(String input) {
            try {
                return TwitterChannel.getTweets(input.trim());
            }
            catch (Exception e) {
                logger.severe(e.toString());
                return null;
            }            
        }
    }

    public static class RSSItemize extends TextProcess {

        public final static Logger logger = Logger.getLogger(TwitterSearch.class.toString());
        
        public RSSItemize(String name) {
            super(name);
        }

        @Override
        public List<? extends Detail> get(String input) {
            List<Detail> ld = new LinkedList();
            String[] urls = input.split("\n");
            for (String url : urls) {
                for (NMessage m : new RSSChannel(url).getMessages())
                    ld.add(m);
            }
            return ld;
        }
    }
    
}
