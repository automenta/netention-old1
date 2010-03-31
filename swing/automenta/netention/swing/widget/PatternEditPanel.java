package automenta.netention.swing.widget;

import automenta.netention.Pattern;
import automenta.netention.Property;
import automenta.netention.Self;
import automenta.netention.swing.util.JScaledLabel;
import automenta.netention.swing.util.SwingWindow;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

public class PatternEditPanel extends JPanel {

    private JTextArea desc;
    private Map<String, JSlider> propStrengths = new HashMap();
    private final Self self;
    private Pattern pattern;

    public class PropertyEditPanel extends JPanel {

        private final Property property;
        final int b = 4;

        public PropertyEditPanel(Property pr) {
            super(new GridLayout(1, 2));
            this.property = pr;

            setBorder(new EmptyBorder(b, b, b, b));

            JPanel left = new JPanel(new GridLayout(2, 1));
            left.setBorder(new EmptyBorder(b, b, b, b));

            Double v = pattern.get(pr.getID());
            double value = v != null ? v : 0.0;
            left.add(new JScaledLabel(pr.getID() + " (" + pr.getClass().getSimpleName() + ")", 1.25f));

            JSlider vf = new JSlider(0, 100, (int) (value * 100.0));
            left.add(vf);


            add(left);
            add(new JScrollPane(new JTextArea(pr.getName())));



//            JTextField vf = new JTextField(Double.toString(value));
//            add(vf);
            propStrengths.put(pr.getID(), vf);

        }
    }

    public PatternEditPanel(Self s) {
        super(new BorderLayout());
        this.self = s;
    }

    public PatternEditPanel(Self s, Pattern p) {
        this(s);
        setPattern(p);
    }

    public void setPattern(Pattern p) {
        this.pattern = p;
        removeAll();
        propStrengths.clear();

        JPanel header = new JPanel(new BorderLayout());
        {
            JLabel title = new JScaledLabel(p.getID(), 2.5f);
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
            JButton newPropButton = new JButton("Add Property...");
            newPropButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    addProperty();
                }
            });
            buttonPanel.add(newPropButton);

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


        for (Property pr : self.getProperties().values()) {
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
        pattern.clear();
        for (String p : propStrengths.keySet()) {
            double v = ((double) propStrengths.get(p).getValue()) / 100.0;
            if (v > 0.0) {
                pattern.put(p, v);
            }
        }
        //TODO save property descriptions
        //TODO save property descriptions
    }

    protected void addProperty() {
        NewPropertyPanel ndp = new NewPropertyPanel(self, pattern.getID()) {

            @Override protected void afterCreated(Property p) {
                setPattern(pattern);
            }
        };
        
        SwingWindow sw = new SwingWindow(ndp, 500, 200, false);
        sw.setTitle("New Property...");

    }
}
