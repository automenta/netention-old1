package automenta.netention.swing.widget;

import automenta.netention.Pattern;
import automenta.netention.Property;
import automenta.netention.Self;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class PatternEditPanel extends JPanel {

    private JTextArea desc;
    private Map<String, JTextField> propStrengths = new HashMap();
    private final Self self;
    private Pattern pattern;

    public PatternEditPanel(Self s) {
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
        JLabel title = new JLabel(p.getID());
        add(title, BorderLayout.NORTH);
        JPanel j = new JPanel(new BorderLayout());
        add(j, BorderLayout.CENTER);
        desc = new JTextArea(p.getDescription(), 4, 40);
        j.add(new JScrollPane(desc), BorderLayout.NORTH);
        JPanel propPanel = new JPanel();
        propPanel.setLayout(new BoxLayout(propPanel, BoxLayout.PAGE_AXIS));
        j.add(new JScrollPane(propPanel), BorderLayout.CENTER);
        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                save();
            }
        });
        add(updateButton, BorderLayout.SOUTH);
        for (Property pr : self.getProperties().values()) {
            JPanel line = new JPanel(new GridLayout(1, 3));
            Double v = p.get(pr.getID());
            double value = v != null ? v : 0.0;
            line.add(new JLabel(pr.getID()));
            line.add(new JTextField(pr.getName()));
            JTextField vf = new JTextField(Double.toString(value));
            line.add(vf);
            propStrengths.put(pr.getID(), vf);
            propPanel.add(line);
            //propertyModel.addElement(p);
        }
        updateUI();
    }

    protected void save() {
        //save description
        pattern.setDescription(desc.getText());
        //save property strengths
        pattern.clear();
        for (String p : propStrengths.keySet()) {
            double v = Double.parseDouble(propStrengths.get(p).getText());
            if (v > 0.0) {
                pattern.put(p, v);
            }
        }
        //TODO save property descriptions
        //TODO save property descriptions
    }
}
