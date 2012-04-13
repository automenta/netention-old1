/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.swing.widget;

import automenta.netention.Detail;
import automenta.netention.Pattern;
import automenta.netention.Self;
import automenta.netention.swing.util.Autocompletion;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import javax.swing.*;

/**
 *
 * @author me
 */
abstract public class PropertySearch extends JPanel implements ActionListener {

    //TODO improve layout
    
    private final Self self;
    private final JComboBox comboBox;
    private final Detail detail;
    private List<Pattern> lp;
    private final JPanel patternSelect;
    private final Map<Pattern, JToggleButton> patternAdd = new HashMap();

    //when selection changes
    //TODO display description and other property metadata when selection changes
    @Override
    public void actionPerformed(ActionEvent ae) {
        updatePatterns();
    }

    abstract public void onSelected(String property, List<String> patterns);
    abstract public void onCancel();

    public PropertySearch(Self s, Detail d) {
        super(new BorderLayout());

        this.self = s;
        this.detail = d;

        List<String> properties = new ArrayList(s.getProperties().size());
        for (String ps : s.getProperties()) {
            if (self.acceptsAnotherProperty(d, ps))
                properties.add(ps);
        }
        
        Collections.sort(properties, new Comparator<String>() {

            @Override
            public int compare(String t, String t1) {
                return t.compareToIgnoreCase(t1);
            }
        });
        Object[] propertyArray = new Object[properties.size()];
        properties.toArray(propertyArray);

        comboBox = new JComboBox(propertyArray);
        comboBox.setRenderer(new PComboBoxRenderer());
        Autocompletion.enable(comboBox);



        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                onCancel();
            }
        });

        JButton ok = new JButton("Add");
        ok.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                String p = comboBox.getSelectedItem().toString();
                
                List<String> rp = new LinkedList();
                for (Pattern pp : patternAdd.keySet()) {
                    JToggleButton jt = patternAdd.get(pp);
                    if (jt.isSelected())
                        rp.add(pp.getID());
                }
                onSelected(p, rp);
            }
        });

        comboBox.addActionListener(this);

        patternSelect = new JPanel();
        
        JPanel top = new JPanel(new FlowLayout());
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        add(top, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
        
        top.add(comboBox);
        top.add(patternSelect);
        bottom.add(ok);
        bottom.add(cancel);


        updatePatterns();

    }

    protected void updatePatterns() {
        String p = comboBox.getSelectedItem().toString();
        
        lp = self.getPatternsOfProperty(p);
        for (Pattern alreadyAdded : detail.getPatterns(self)) {
            lp.remove(alreadyAdded);
        }
        
        patternSelect.setLayout(new BoxLayout(patternSelect, BoxLayout.PAGE_AXIS));
        patternSelect.removeAll();
        patternAdd.clear();
        int c = 0;
        for (Pattern pp : lp) {
            JToggleButton j = new JToggleButton(pp.getName());
            j.setSelected(c == 0);
            j.setEnabled(!((lp.size() == 1) && (c == 0)));
            patternAdd.put(pp, j);
            patternSelect.add(j);
            c++;
        }
        
        updateUI();
        
        
    }
    
    class PComboBoxRenderer extends JLabel
            implements ListCellRenderer {

        public PComboBoxRenderer() {
            setOpaque(true);
//            setHorizontalAlignment(CENTER);
//            setVerticalAlignment(CENTER);
        }

        /*
         * This method finds the image and text corresponding to the selected
         * value and returns the label, set up to display the text and image.
         */
        public Component getListCellRendererComponent(
                JList list,
                Object value,
                int index,
                boolean isSelected,
                boolean cellHasFocus) {
            //Get the selected index. (The index param isn't
            //always valid, so just use the value.)
            //int selectedIndex = ((Integer) value).intValue();

            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }

            String p = value.toString();
            setText(p + " (" + self.getProperty(p).getName() + ")");

//        //Set the icon and text.  If icon was null, say so.
//        ImageIcon icon = images[selectedIndex];
//        String pet = petStrings[selectedIndex];
//        setIcon(icon);
//        if (icon != null) {
//            setText(pet);
//            setFont(list.getFont());
//        } else {
//            setUhOhText(pet + " (no image available)",
//                        list.getFont());
//        }

            return this;
        }
    }
}
