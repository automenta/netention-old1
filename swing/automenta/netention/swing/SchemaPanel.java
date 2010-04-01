/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.swing;

import automenta.netention.swing.widget.PatternEditPanel;
import automenta.netention.Pattern;
import automenta.netention.Self;
import automenta.netention.impl.MemorySelf;
import automenta.netention.swing.util.SwingWindow;
import automenta.netention.value.IntProp;
import automenta.netention.value.StringProp;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * Panel for editing Patterns and Properties:
 *      Add Pattern(name, description)
 *      Add Property(name, description, type, cardinality, defaultValue)
 *      View/Edit Pattern's Properties (by associating)
 *      View/Edit a Property
 *      Open Example Editor Window for a Selection of Patterns
 * @author seh
 */
@Deprecated public class SchemaPanel extends JPanel {

    private Self self;
    private final DefaultListModel patternModel;
    private final JList patternList;
    private final PatternEditPanel editPanel;
    private Pattern selectedPattern = null;

    public SchemaPanel(Self self) {
        super();

        this.self = self;

        setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        {
            JButton loadButton = new JButton("Open...");
            loadButton.addActionListener(new ActionListener() {

                @Override public void actionPerformed(ActionEvent e) {
                    loadPattern();
                }
            });
            buttonPanel.add(loadButton);

            JButton newPatternButton = new JButton("New Pattern...");
            newPatternButton.addActionListener(new ActionListener() {
                @Override public void actionPerformed(ActionEvent e) {
                    newPattern();
                }
            });
            buttonPanel.add(newPatternButton);

            JButton newPropertyButton = new JButton("New Property...");
            newPropertyButton.addActionListener(new ActionListener() {
                @Override public void actionPerformed(ActionEvent e) {
                    newProperty();
                }
            });
            buttonPanel.add(newPropertyButton);

            JButton saveButton = new JButton("Save...");
            saveButton.addActionListener(new ActionListener() {

                @Override public void actionPerformed(ActionEvent e) {
                    savePattern();
                }
            });
            buttonPanel.add(saveButton);

        }
        add(buttonPanel, BorderLayout.NORTH);

        patternModel = new DefaultListModel();
        patternList = new JList(patternModel);


        editPanel = new PatternEditPanel(self) {

            @Override
            protected void deleteThis() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

        };

        add(patternList, BorderLayout.WEST);
        add(editPanel, BorderLayout.CENTER);

        updateSchema();

        patternList.addListSelectionListener(new ListSelectionListener() {

            @Override public void valueChanged(ListSelectionEvent e) {
                selectedPattern = (Pattern) patternList.getSelectedValue();
                editPanel.setPattern(selectedPattern);
            }
        });
    }

    protected void updateSchema() {
        patternModel.clear();

        for (Pattern p : self.getPatterns().values()) {
            patternModel.addElement(p);
        }

        updateUI();
    }

    public void setSelf(Self s) {
        this.self = s;
        updateSchema();
    }

    protected void newPattern() {
        String patternName = JOptionPane.showInputDialog("Pattern Name");
        Pattern p = new Pattern(patternName);
        self.addPattern(p);
        updateSchema();
    }
    
    protected void newProperty() {

    }

    protected void loadPattern() {
        String path = JOptionPane.showInputDialog("Filename");
        if (path != null) {
            try {
                MemorySelf ms = MemorySelf.load(path);
                setSelf(ms);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Can not load from: " + path);
            }
        }
    }

    protected void savePattern() {
        if (self instanceof MemorySelf) {
            String path = JOptionPane.showInputDialog("Filename");
            if (path != null) {
                MemorySelf ms = (MemorySelf) self;
                try {
                    ms.save(path);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Can not save " + self + " to: " + path);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Can not save " + self);
        }
    }


    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
        }

        MemorySelf self = new MemorySelf("me", "Me");
        self.addPattern(new Pattern("Built"));
        self.addPattern(new Pattern("Mobile"));
        self.addProperty(new IntProp("numWheels", "Number of Wheels"));
        self.addProperty(new StringProp("manufacturer", "Manufacturer"));
        new SwingWindow(new SchemaPanel(self), 800, 600, true);
    }

}
