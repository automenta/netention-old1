package automenta.netention.swing.widget;

import automenta.netention.Pattern;
import automenta.netention.Property;
import automenta.netention.Self;
import automenta.netention.swing.Icons;
import automenta.netention.swing.util.JHyperLink;
import automenta.netention.swing.util.JScaledLabel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

abstract public class PatternEditPanel extends JPanel {

    private JTextArea desc;
    private Map<String, JSlider> propStrengths = new HashMap();
    private final Self self;
    protected Pattern pattern;

    public class PropertyEditPanel extends JPanel implements ChangeListener {

        private final Property property;
        final int b = 4;
        private final JSlider strengthSlider;

        public PropertyEditPanel(Property pr) {
            super(new GridLayout(1, 2));
            this.property = pr;

            setBorder(new EmptyBorder(b, b, b, b));

            //JPanel left = new JPanel(new GridLayout(2, 1));

            Double v = pattern.properties.get(pr.getID());
            double value = v != null ? v : 0.0;
            JHyperLink lsl = new JHyperLink(pr.getName() + " (" + pr.getClass().getSimpleName() + ")", pr.getID(), 1f);
            lsl.setIcon(Icons.getIcon("property"));
            lsl.setHorizontalAlignment(SwingConstants.LEFT);
            add(lsl);

            strengthSlider = new JSlider(0, 100, (int) (value * 100.0));
            strengthSlider.addChangeListener(this);
            add(strengthSlider);

            //add(new JScrollPane(new JTextArea(pr.getName())));

//            JTextField vf = new JTextField(Double.toString(value));
//            add(vf);
            propStrengths.put(pr.getID(), strengthSlider);

            setOpaque(true);
            updateColor();
        }



        @Override
        public void stateChanged(ChangeEvent e) {
            updateColor();
        }

        protected void updateColor() {
            float strength = ((float)strengthSlider.getValue()) * 0.01f;
            float r = 0.5f + (strength/3.0f);
            float g = 0.5f + (strength/3.0f);
            float b = 0.5f + (strength/3.0f);
            Color c = new Color(r, g, b);
            setBackground(c);
        }
    }

    public PatternEditPanel(Self s, Pattern p) {
        super(new BorderLayout());
        this.self = s;
        setPattern(p);
    }

    public void setPattern(Pattern p) {
        this.pattern = p;
        removeAll();
        propStrengths.clear();

        JPanel header = new JPanel(new BorderLayout());
        {
            JLabel title = new JScaledLabel(p.getID(), 2.5f);
            title.setIcon(Icons.getPatternIcon(p));
            header.add(title, BorderLayout.CENTER);
            
        }
        add(header, BorderLayout.NORTH);

        JPanel j = new JPanel(new BorderLayout());
        add(j, BorderLayout.CENTER);
        desc = new JTextArea(p.getDescription(), 4, 40);
        desc.setToolTipText("Pattern description.");
        j.add(new JScrollPane(desc), BorderLayout.NORTH);

        JPanel propPanel = new JPanel();
        propPanel.setLayout(new BoxLayout(propPanel, BoxLayout.PAGE_AXIS));
        j.add(new JScrollPane(propPanel), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        {

            JButton deleteButton = new JButton("Delete");
            deleteButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (0 == JOptionPane.showConfirmDialog(PatternEditPanel.this, "Delete this pattern?  There may be details that presently depend upon it.", "Delete", JOptionPane.YES_NO_OPTION)) {
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override public void run() {
                                deleteThis();
                            }
                        });
                    }
                }
            });
            buttonPanel.add(deleteButton);

            JButton updateButton = new JButton("Update");
            updateButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    save();
                }
            });
            buttonPanel.add(updateButton);

        }
        j.add(buttonPanel, BorderLayout.SOUTH);


        for (String spr : self.getProperties()) {
            Property pr = self.getProperty(spr);
            PropertyEditPanel line = new PropertyEditPanel(pr);
            propPanel.add(line);
        }
        propPanel.add(Box.createVerticalBox());

        updateUI();
    }

    protected void save() {
        //save description
        pattern.setDescription(desc.getText());
        //save property strengths
        pattern.properties.clear();
        for (String p : propStrengths.keySet()) {
            double v = ((double) propStrengths.get(p).getValue()) / 100.0;
            if (v > 0.0) {
                pattern.properties.put(p, v);
            }
        }
        //TODO save property descriptions
        //TODO save property descriptions
    }

    abstract protected void deleteThis();


}
