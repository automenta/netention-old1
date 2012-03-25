/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package afxdeadcode.test;

import afxdeadcode.NGramClassifier;
import automenta.netention.swing.util.SwingWindow;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;

/**
 *
 * @author SeH
 */
public class ClassifierTool {
    public static void main(String[] args) {
        NGramClassifier happy = NGramClassifier.load("happy");
        NGramClassifier sad = NGramClassifier.load("sad");
        
        String[] s = new String[] {
            "i am happy :) now what do you want",
            "i am happy :)",
            "i'm ok",
            "i'm ok i'm ok",
            "i am sad :( but i am happy",
            "i am sad :("
        };
        
        for (String x : s) {
            double a = happy.getDistance(x);
            double b = sad.getDistance(x);
            System.out.println(x + " " + (a / b));
        }
    }
}
//public class ClassifierTool extends JPanel {
//    private final NGramClassifier c;
//    private final JTabbedPane tabs;
//
//    public ClassifierTool(final NGramClassifier c) {
//        super(new BorderLayout());
//        this.c = c;
//        
//        tabs = new JTabbedPane();
//        for (final String p : c.categories()) {
//            JPanel x = new JPanel(new BorderLayout());
//            final JTextArea text = new JTextArea(5, 80);
//            text.setText(c.getCorpus(p));
//            final JTextArea out = new JTextArea(5, 80);
//            out.setText(c.getCategory(p).toString());
//            
//            JButton update = new JButton("Update");
//            update.addActionListener(new ActionListener() {
//                @Override public void actionPerformed(ActionEvent e) {
//                    c.set(p, text.getText().trim());
//                    out.setText(c.getCategory(p).toString());
//                }
//            });
//            
//            out.setEditable(false);
//            
//            x.add(new JScrollPane(text), BorderLayout.CENTER);
//            x.add(update, BorderLayout.EAST);
//            x.add(new JScrollPane(out), BorderLayout.SOUTH);
//            tabs.add(p, x);
//        }
//        add(tabs, BorderLayout.CENTER);
//        
//        updateUI();
//    }
//    
//    public static class ClassifierTestTool extends JPanel {
//
//        JTextArea input, output;
//        private final NGramClassifier c;
//        
//        public ClassifierTestTool(NGramClassifier c) {
//            super(new BorderLayout());
//            
//            this.c = c;
//            
//            input = new JTextArea(5, 40);
//            add(new JScrollPane(input), BorderLayout.NORTH);
//            
//            input.addKeyListener(new KeyAdapter() {
//
//                @Override public void keyReleased(KeyEvent e) {
//                    update();
//                }
//                
//            });
//            output = new JTextArea(5, 40);
//            add(new JScrollPane(output), BorderLayout.CENTER);
//            
//        }
//        
//        protected synchronized void update() {
//            final String t = input.getText().trim();
//            output.setText(                    
//                    "Happy:"  + c.getDistance(t, "happy") + " Sad: " + c.getDistance(t, "sad") + "\n");
//                    //"Rich/Poor:"  + c.analyzeC(input.getText().trim(), Arrays.asList( new String[] {"rich", "poor" } ) ).toString()
//            
//            
//        }
//        
//        
//    }
//    
//    public static void main(String[] args) throws Exception {
//        String path = "data";
//        
//        NGramClassifier cc = NGramClassifier.load(path, true);        
//        
//        cc.addCategory("happy");
//        cc.addCategory("sad");
//        cc.addCategory("rich");
//        cc.addCategory("poor");
//        
//        ClassifierTool c = new ClassifierTool(cc);        
//        new SwingWindow(c, 800, 600, true);
//        
//        new SwingWindow(new ClassifierTestTool(cc), 400, 300, true);
//        
//    }
//    
//}

