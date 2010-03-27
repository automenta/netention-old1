/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.swing.widget;

import automenta.netention.Detail;
import automenta.netention.Pattern;
import automenta.netention.Property;
import automenta.netention.PropertyValue;
import automenta.netention.Self;
import automenta.netention.swing.property.BoolPropertyPanel;
import automenta.netention.swing.property.IntPropertyPanel;
import automenta.netention.swing.property.PropertyOptionPanel;
import automenta.netention.swing.property.RealPropertyPanel;
import automenta.netention.swing.property.StringPropertyPanel;
import automenta.netention.swing.util.JScaledLabel;
import automenta.netention.value.BoolProp;
import automenta.netention.value.IntProp;
import automenta.netention.value.RealProp;
import automenta.netention.value.StringProp;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

/**
 * Displays a detail and its links for view/edit
 *      Set Mode (real, imaginary)
 *      Add/Remove Patterns
 *      Add new Properties
 *      Remove existing Properties
 * @author seh
 */
abstract public class DetailEditPanel extends JPanel {

    private final JPanel sentences;
    private Detail detail;
    private final Self self;
    private boolean editable;
    private final JSplitPane contentSplit;
    private final DetailLinksPanel linksPanel;
    private final DetailMenuBar menuBar;
    private List<PropertyOptionPanel> optionPanels = new LinkedList();
    long updateDelayMS = 650;

    protected class DetailMenuBar extends JMenuBar {

        public DetailMenuBar() {
            super();
        }

        protected void refresh() {
            removeAll();

            JMenu t = new JMenu("It's a...");
            for (String pid : self.getAvailablePatterns(detail)) {
                final Pattern p = self.getPatterns().get(pid);
                JMenuItem ti = new JMenuItem(p.getID());
                ti.addActionListener(new ActionListener() {

                    @Override public void actionPerformed(ActionEvent e) {
                        SwingUtilities.invokeLater(new Runnable() {

                            @Override public void run() {
                                addPattern(p);
                            }
                        });
                    }
                });
                t.add(ti);
            }
            add(t);

            for (String pid : detail.getPatterns()) {
                final Pattern p = self.getPatterns().get(pid);
                JMenu j = new JMenu(p.getID());

                int numItems = 0;

                for (String propid : p.keySet()) {
                    final Property prop = self.getProperty(propid);
                    if (self.acceptsAnotherProperty(detail, propid)) {
                        JMenuItem ji = new JMenuItem(prop.getName());
                        ji.addActionListener(new ActionListener() {

                            @Override public void actionPerformed(ActionEvent e) {
                                SwingUtilities.invokeLater(new Runnable() {

                                    @Override public void run() {
                                        addProperty(prop);
                                    }
                                });
                            }
                        });
                        numItems++;
                        j.add(ji);
                    }
                }

                if (numItems > 0) {
                    j.addSeparator();
                }
                JMenuItem remove = new JMenuItem("Remove " + p.getID());
                remove.addActionListener(new ActionListener() {

                    @Override public void actionPerformed(ActionEvent e) {
                        if (JOptionPane.showConfirmDialog(DetailEditPanel.this, "Remove " + p.getID() + " from this detail?", "Remove", JOptionPane.OK_CANCEL_OPTION) == 0) {
                            SwingUtilities.invokeLater(new Runnable() {

                                @Override public void run() {
                                    removePattern(p);
                                }
                            });
                        }
                    }
                });
                j.add(remove);

                add(j);
            }
        }
    }

    public DetailEditPanel(Self s, Detail d, boolean editable) {
        super(new GridBagLayout());

        this.self = s;

        setEditable(editable);

        GridBagConstraints gc = new GridBagConstraints();
        {
            gc.weightx = 1.0;
            gc.weighty = 0.0;
            gc.fill = gc.HORIZONTAL;
            gc.anchor = gc.NORTHWEST;
            gc.gridx = gc.gridy = 1;
        }

        menuBar = new DetailMenuBar();

        JPanel header = new JPanel(new BorderLayout());
        {
            header.add(new JScaledLabel(d.getName(), 2.5f), BorderLayout.CENTER);

            JPanel buttons = new JPanel(new FlowLayout());

            final JButton updateButton = new JButton("Update");
            updateButton.addActionListener(new ActionListener() {

                @Override public void actionPerformed(ActionEvent e) {
                    SwingUtilities.invokeLater(new Runnable() {

                        @Override public void run() {
                            updateButton.setEnabled(false);
                            updateDetail();
                            new Thread(new Runnable() {

                                @Override public void run() {
                                    try {
                                        Thread.sleep(updateDelayMS);
                                    } catch (InterruptedException ex) {
                                    }
                                    updateButton.setEnabled(true);
                                }
                            }).start();

                        }
                    });
                }
            });

            final JButton deleteButton = new JButton("Delete");
            deleteButton.addActionListener(new ActionListener() {
                @Override public void actionPerformed(ActionEvent e) {
                    if (0 == JOptionPane.showConfirmDialog(DetailEditPanel.this, "Delete this detail?", "Delete", JOptionPane.YES_NO_OPTION)) {
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override public void run() {
                                deleteThis();
                            }
                        });
                    }
                }
            });

            buttons.add(deleteButton);
            buttons.add(updateButton);

            header.add(buttons, BorderLayout.EAST);

            header.add(menuBar, BorderLayout.SOUTH);
        }

        add(header, gc);

        {
            gc.gridy++;
            gc.weighty = 1.0;
            gc.fill = gc.BOTH;
        }

        contentSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        add(contentSplit, gc);

        sentences = new JPanel(new GridBagLayout());
        sentences.setBackground(Color.WHITE);
        contentSplit.setTopComponent(new JScrollPane(sentences));

        linksPanel = new DetailLinksPanel(self, d);
        //contentSplit.setBottomComponent(new JScrollPane(linksPanel));

        setDetail(d);

        contentSplit.setDividerLocation(1.0);

    }

    protected void setDetail(Detail d) {
        this.detail = d;
        sentences.removeAll();

        menuBar.refresh();

        sentences.setAlignmentY(TOP_ALIGNMENT);

        GridBagConstraints gc = new GridBagConstraints();
        gc.weightx = 1.0;
        gc.weighty = 0.0;
        gc.fill = gc.HORIZONTAL;
        gc.anchor = gc.NORTHWEST;
        gc.gridx = 1;

        optionPanels.clear();

        for (final PropertyValue pv : detail.getProperties()) {
            gc.gridy++;
            JComponent nextLine = getLinePanel(pv);
            if (nextLine instanceof PropertyOptionPanel) {
                PropertyOptionPanel pop = (PropertyOptionPanel) nextLine;
                final Property property = self.getProperty(pv.getProperty());
                JPopupMenu popup = new JPopupMenu();
                JMenuItem removeItem = new JMenuItem("Remove");
                removeItem.addActionListener(new ActionListener() {

                    @Override public void actionPerformed(ActionEvent e) {
                        if (0 == JOptionPane.showConfirmDialog(DetailEditPanel.this, "Remove " + property.getName() + "?", "Remove", JOptionPane.YES_NO_OPTION)) {
                            removeProperty(pv);
                        }
                    }
                });
                popup.add(removeItem);
                pop.getNameLabel().addPopup(popup);
                optionPanels.add(pop);
            }

            final Color alternateColor = new Color(0.95f, 0.95f, 0.95f);

            nextLine.setOpaque(true);
            nextLine.setBackground(gc.gridy % 2 == 0 ? Color.WHITE : alternateColor);

            sentences.add(nextLine, gc);
        }

        gc.gridy++;
        gc.fill = gc.VERTICAL;
        gc.weighty = 1.0;
        sentences.add(Box.createVerticalBox(), gc);

        updateUI();

    }

    protected JComponent getLinePanel(PropertyValue pv) {
        Property prop = self.getProperty(pv.getProperty());
        if (prop instanceof IntProp) {
            //Int must be tested before Real because it is a subclass of it
            return new IntPropertyPanel(self, detail, pv, editable);
        } else if (prop instanceof RealProp) {
            return new RealPropertyPanel(self, detail, pv, editable);
        } else if (prop instanceof StringProp) {
            return new StringPropertyPanel(self, detail, pv, editable);
        } else if (prop instanceof BoolProp) {
            return new BoolPropertyPanel(self, detail, pv, editable);
        }
//        } else if (prop instanceof NodeProp) {
//            return new NodePropertyPanel(self, detail, pv, editable);
//        }

        return new JLabel(pv.toString());

    }

    protected void setEditable(boolean editable) {
        this.editable = editable;
    }


    synchronized protected void addPattern(Pattern p) {
        detail.getPatterns().add(p.getID());
        patternChanged();
        refreshUI();
    }

    synchronized protected void removePattern(Pattern p) {
        detail.getPatterns().remove(p.getID());
        //TODO remove properties?
        patternChanged();
        refreshUI();
    }

    synchronized protected void addProperty(Property p) {
        PropertyValue pv = p.newDefaultValue(detail.getMode());
        pv.setProperty(p.getID());
        detail.getProperties().add(pv);
        refreshUI();
    }

    synchronized protected void removeProperty(PropertyValue pv) {
        detail.getProperties().remove(pv);
        refreshUI();
    }

    protected synchronized void refreshUI() {
        setDetail(detail);
    }

    /** writes contents of UI widgets to the detail */
    protected synchronized void updateDetail() {
        for (PropertyOptionPanel pop : optionPanels) {
            pop.widgetToValue();
        }

    }

    abstract protected void deleteThis();
    abstract protected void patternChanged();

}
