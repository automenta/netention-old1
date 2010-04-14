/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.swing.widget;

import automenta.netention.Detail;
import automenta.netention.Link;
import automenta.netention.Mode;
import automenta.netention.Pattern;
import automenta.netention.Property;
import automenta.netention.PropertyValue;
import automenta.netention.Self;
import automenta.netention.swing.Icons;
import automenta.netention.swing.property.BoolPropertyPanel;
import automenta.netention.swing.property.IntPropertyPanel;
import automenta.netention.swing.property.PropertyOptionPanel;
import automenta.netention.swing.property.RealPropertyPanel;
import automenta.netention.swing.property.StringPropertyPanel;
import automenta.netention.swing.util.JHyperLink;
import automenta.netention.value.BoolProp;
import automenta.netention.value.IntProp;
import automenta.netention.value.RealProp;
import automenta.netention.value.StringProp;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
import javax.swing.JTextArea;
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
    private final DetailMenuBar menuBar;
    private List<PropertyOptionPanel> optionPanels = new LinkedList();
    long updateDelayMS = 650;
    private final JTextArea headerLabel;

    //Tooltips
    final static String itsATooltip = "Selects Patterns for this Detail";
    final static String realOrImaginary = "Real details describe things that actually exist. \nImaginary details describe hypothetical or desired things.";

    protected class LinkPanel extends JPanel {

        private final Link link;

        private LinkPanel(Link l) {
            super(new FlowLayout(FlowLayout.LEFT));
            this.link = l;

            setOpaque(false);

            String otherID = getOther();

            Detail other = self.getDetails().get(otherID);

            JHyperLink la = new JHyperLink(other.getName() + " (" + l.toString() + ")", "", 1.2f);
            la.setIcon(Icons.getDetailIcon(self, other));
            add(la);

            JLabel s = new JLabel(((int) (link.getStrength() * 100.0)) + "%");
            add(s);

        }

        public String getOther() {
            if (link.getSource().equals(detail.getID())) {
                return link.getTarget();
            }
            return link.getSource();
        }
    }

    protected class DetailMenuBar extends JMenuBar {

        float menuFontScale = 1.25f;

        public DetailMenuBar() {
            super();
        }

        protected void refresh() {
            removeAll();

            JMenu t = new JMenu("It's a...");
            t.setToolTipText(itsATooltip);
            for (String pid : self.getAvailablePatterns(detail)) {
                final Pattern p = self.getPatterns().get(pid);
                JMenuItem ti = new JMenuItem(p.getID());
                ti.setIcon(Icons.getPatternIcon(p));
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
                j.setIcon(Icons.getPatternIcon(p));
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
                        String extraMessage = "";
                        if (detail.getPatterns().size() == 1) {
                            extraMessage = "\nThis will remove all properties since there will be no patterns remaining.";
                        }

                        if (JOptionPane.showConfirmDialog(DetailEditPanel.this, "Remove " + p.getID() + " from this detail?" + extraMessage, "Remove", JOptionPane.OK_CANCEL_OPTION) == 0) {
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

            if (detail.getPatterns().size() > 0) {
                String currentModeString = getModeString(detail.getMode());
                JMenu modeMenu = new JMenu("..." + currentModeString);
                modeMenu.setToolTipText(realOrImaginary);

                JMenuItem realItem = new JMenuItem(getModeString(Mode.Real));
                realItem.addActionListener(new ActionListener() {

                    @Override public void actionPerformed(ActionEvent e) {
                        if (!switchMode(Mode.Real)) {
                        }
                    }
                });
                modeMenu.add(realItem);

                JMenuItem imagItem = new JMenuItem(getModeString(Mode.Imaginary));
                imagItem.addActionListener(new ActionListener() {

                    @Override public void actionPerformed(ActionEvent e) {
                        if (!switchMode(Mode.Imaginary)) {
                        }
                    }
                });
                modeMenu.add(imagItem);

                add(modeMenu);

            } else {
                add(new JLabel("Thought"));
                switchMode(Mode.Unknown);
            }

            add(Box.createHorizontalGlue());
        }

        public String getModeString(Mode mode) {
            if (mode == Mode.Real) {
                return "in Reality";
            } else if (mode == Mode.Imaginary) {
                return "in Imagination";
            } else {
                return "Thought";
            }
        }

        @Override
        public JMenu add(JMenu c) {
            c.setFont(c.getFont().deriveFont(c.getFont().getSize2D() * menuFontScale));
            return super.add(c);
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
            gc.anchor = gc.NORTH;
            gc.gridx = gc.gridy = 1;
        }

        menuBar = new DetailMenuBar();

//        JPanel header = new JPanel(new BorderLayout(4, 4));
//        {
//            //headerIcon = new JLabel("");
//            //header.add(headerIcon, BorderLayout.WEST);
//            //headerIcon.setVerticalAlignment(JLabel.TOP);
//
//
//            //header.add(headerLabel, BorderLayout.CENTER);
//
//            header.add(menuBar, BorderLayout.NORTH);
//        }

        add(menuBar, gc);


        contentSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        {
            gc.weightx = 1.0;
            gc.weighty = 0.1;
            gc.fill = gc.BOTH;
            gc.anchor = gc.NORTHWEST;
            gc.gridx = 1;
            gc.gridy = 2;
        }
        add(contentSplit, gc);

        headerLabel = new JTextArea(d.getName());
        headerLabel.setFont(headerLabel.getFont().deriveFont(headerLabel.getFont().getSize2D() * 1.7f));

        sentences = new JPanel(new GridBagLayout());
        sentences.setBackground(Color.WHITE);


        contentSplit.setTopComponent(new JScrollPane(headerLabel));
        contentSplit.setBottomComponent(new JScrollPane(sentences));


        {
            JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));

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

            gc.weightx = 1.0;
            gc.weighty = 0.0;
            gc.fill = gc.NONE;
            gc.anchor = gc.SOUTHEAST;
            gc.gridx = 1;
            gc.gridy = 3;

            add(buttons, gc);

        }

        setDetail(d);

        contentSplit.setResizeWeight(0.85);
        contentSplit.setDividerLocation(0.5);

    }

    /**
     * returns false if the switch was cancelled.  if no switch was necessary, it returns true.
     * @param nextMode
     * @return
     */
    public boolean switchMode(Mode nextMode) {
        Mode currentMode = detail.getMode();
        if (currentMode == nextMode) {
            return true;
        }

        if (detail.getProperties().size() > 0) {
            if (nextMode == Mode.Unknown) {
                if (detail.getPatterns().size() > 0) {
                    if (0 == JOptionPane.showConfirmDialog(DetailEditPanel.this, "Change mode to Thought?  This will remove all properties.", "Change Mode", JOptionPane.YES_NO_OPTION)) {
                        detail.getProperties().clear();
                        updateDetail();
                        refreshUI();
                        return true;
                    }
                } else {
                    detail.getProperties().clear();
                    updateDetail();
                    patternChanged();
                    refreshUI();
                    return true;
                }
                return false;
            } else {
                String modeName = (nextMode == Mode.Real) ? "Real" : "Imaginary";
                if (0 == JOptionPane.showConfirmDialog(DetailEditPanel.this, "Change mode to " + modeName + "?  This will reset all properties.", "Change Mode", JOptionPane.YES_NO_OPTION)) {
                    detail.setMode(nextMode);

                    List<Property> existingProperties = new LinkedList();
                    for (PropertyValue p : detail.getProperties()) {
                        existingProperties.add(self.getProperty(p.getProperty()));
                    }

                    detail.getProperties().clear();
                    for (Property p : existingProperties) {
                        addProperty(p);
                    }

                    updateDetail();

                    refreshUI();
                    return true;
                }
                return false;
            }
        } else {
            detail.setMode(nextMode);
            refreshUI();
            return true;
        }
    }

    protected void setDetail(Detail d) {
        this.detail = d;
        sentences.removeAll();

        menuBar.refresh();

        final GridBagConstraints gc = new GridBagConstraints();

        if (d.getPatterns().size() == 0) {
        } else {

            sentences.setAlignmentY(TOP_ALIGNMENT);

            gc.weightx = 1.0;
            gc.weighty = 0.0;
            gc.fill = gc.HORIZONTAL;
            gc.anchor = gc.NORTHWEST;
            gc.gridx = 1;
            gc.gridy = 1;

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

            self.updateLinks(new Runnable() {

                @Override
                public void run() {
                    List<Link> edges = new LinkedList();
                    Collection<Link> inEdges = self.getLinks().getInEdges(detail);
                    Collection<Link> outEdges = self.getLinks().getOutEdges(detail);

                    if (inEdges != null) {
                        edges.addAll(inEdges);
                    }
                    if (outEdges != null) {
                        edges.addAll(outEdges);
                    }

                    if (edges.size() == 0) {
                        return;
                    }


                    for (Link l : edges) {
                        gc.gridy++;
                        sentences.add(new LinkPanel(l), gc);
                    }

                    updateUI();
                }
            }, detail);


            gc.gridy++;
            gc.fill = gc.VERTICAL;
            gc.weighty = 1.0;
            sentences.add(Box.createVerticalBox(), gc);
        }

        //headerIcon.setIcon(Icons.getDetailIcon(self, d));

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

    protected void chooseInitialMode() {
        Object[] options = {"Imaginary", "Real"};
        int n = JOptionPane.showOptionDialog(this,
            "Is this detail Real or Imaginary?\n\n" + realOrImaginary,
            "Select Mode",
            JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[1]);
        if (n == 1) {
            detail.setMode(Mode.Real);
        } else {
            detail.setMode(Mode.Imaginary);
        }
    }

    synchronized protected void addPattern(Pattern p) {
        updateDetail(); //TODO this assumes that the data is to be updated when patterns changed.  is this right?

        if (detail.getPatterns().size() == 0) {
            chooseInitialMode();
        }
        detail.getPatterns().add(p.getID());
        patternChanged();
        refreshUI();
    }

    synchronized protected void removePattern(Pattern p) {
        updateDetail(); //TODO this assumes that the data is to be updated when patterns changed.  is this right?

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
        detail.setName(headerLabel.getText());
        for (PropertyOptionPanel pop : optionPanels) {
            pop.widgetToValue();
        }

    }

    abstract protected void deleteThis();

    abstract protected void patternChanged();
}
