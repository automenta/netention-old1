/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.swing;

import automenta.netention.swing.util.SwingWindow;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
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

    private final JList list = new JList();
    
    //in drop-down
    // read as list of urls (bookmark)
    // read combined content of urls
    // sentence-ize
    // create prototype details
    // twitter search
    // yahoo image search
    // google search

    public ReadTextDocumentPanel() {
        super(new BorderLayout(8,8));

        JPanel inputArea = new JPanel(new BorderLayout());
        {
            textArea = new JTextArea();
            inputArea.add(textArea, BorderLayout.CENTER);
            
            JPanel j = new JPanel(new FlowLayout());
            {
                JComboBox processSelect = new JComboBox();
                j.add(processSelect, BorderLayout.CENTER);
                JButton processButton = new JButton("Process");             
                j.add(processButton, BorderLayout.EAST);
            }
            inputArea.add(j, BorderLayout.SOUTH);
            
            
        }
        add(inputArea, BorderLayout.CENTER);

        resultPanel = new JPanel(new BorderLayout());
        {
            
            resultPanel.add(list, BorderLayout.CENTER);
            
            JPanel j = new JPanel(new FlowLayout());
            {
                JButton okButton = new JButton("OK");             
                j.add(okButton);
            }
            resultPanel.add(j, BorderLayout.SOUTH);
            
        }
        add(resultPanel, BorderLayout.EAST);
                
    }
          
    public static void main(String[] args) {
        new SwingWindow(new ReadTextDocumentPanel(), 800, 600, true);
    }
}
