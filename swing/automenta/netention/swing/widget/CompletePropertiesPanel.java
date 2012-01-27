/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.swing.widget;

import automenta.netention.swing.detail.DetailEditPanel;
import automenta.netention.*;
import automenta.netention.demo.RunSelfBrowser;
import automenta.netention.impl.MemoryDetail;
import automenta.netention.impl.MemorySelf;
import automenta.netention.swing.util.SwingWindow;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 *
 * a wizard-like interface for entering the remaining property values for a detail
 * may display a list of steps to select from, but supports a serial question/answer process
 * recommended by salvador
 * @author seh
 */
public class CompletePropertiesPanel extends JPanel {
    private final ArrayList<Property> props;
    private final Map<Property, PropertyValue> values;
    
    int currentProp = 0;
    private final JPanel centerPanel;
    private final JPanel bottomPanel;
    private final Self self;
    private final Detail detail;
    private final JButton cancelButton;
    private final JButton finishButton;
    private final JButton prevButton;
    private final JButton nextButton;

    public CompletePropertiesPanel(Self s, Detail d, Runnable onFinished) {
        super(new BorderLayout());        
        
        this.self = s;
        this.detail = d;
        
        centerPanel = new JPanel(new BorderLayout());        
        add(centerPanel, BorderLayout.CENTER);
        
        bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        {
            
            bottomPanel.add(cancelButton = new JButton("Cancel"));            
            bottomPanel.add(prevButton = new JButton("Previous"));
            bottomPanel.add(nextButton = new JButton("Next"));
            bottomPanel.add(finishButton = new JButton("Finish"));
            
            prevButton.addActionListener(new ActionListener() {
                @Override public void actionPerformed(ActionEvent e) {
                    prev();
                }                
            });
            nextButton.addActionListener(new ActionListener() {
                @Override public void actionPerformed(ActionEvent e) {
                    next();
                }                
            });
        }
        add(bottomPanel, BorderLayout.SOUTH);
        
        props = new ArrayList(s.getAvailableProperties(d).keySet());
        values = new HashMap();
        
        refresh();
    }
    
    public PropertyValue getValue(Property p) {
        PropertyValue pv = values.get(p);
        if (pv == null) {
            pv = p.newDefaultValue(detail.getMode());
            values.put(p, pv);
        }
        return pv;
    }
    
    public void refresh() {
        centerPanel.removeAll();
        
        
        Property p = props.get(currentProp);
        
        PropertyValue pv = getValue(p);
        pv.setProperty(p.getID());
        
        JComponent j = DetailEditPanel.getLinePanel(self, detail, pv, true);
        centerPanel.add(j);
        
        prevButton.setEnabled(currentProp!=0);
        nextButton.setEnabled(currentProp!=(props.size()-1));
        
        updateUI();
                
    }

    public void next() {
        if (currentProp == (props.size()-1))
            return;
        currentProp++;
        refresh();
    }
    
    public void prev() {
        if (currentProp == 0)
            return;
        currentProp--;
        refresh();        
    }
    
    public static void main(String[] args) {
        MemorySelf s = RunSelfBrowser.newDefaultSelf();
        Detail d = new MemoryDetail("Description", Mode.Real, "Built");
        new SwingWindow(new CompletePropertiesPanel(s, d, null), 800, 600, true);
    }
    
}
