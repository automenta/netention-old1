/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.swing.widget;

import automenta.netention.Detail;
import automenta.netention.Link;
import automenta.netention.Link.HasStrength;
import automenta.netention.Mode;
import automenta.netention.Node;
import automenta.netention.Pattern;
import automenta.netention.Property;
import automenta.netention.PropertyValue;
import automenta.netention.Self;
import automenta.netention.graph.Pair;
import automenta.netention.graph.ValueDirectedEdge;
import automenta.netention.swing.Icons;
import automenta.netention.swing.property.BoolPropertyPanel;
import automenta.netention.swing.property.IntPropertyPanel;
import automenta.netention.swing.property.PropertyOptionPanel;
import automenta.netention.swing.property.RealPropertyPanel;
import automenta.netention.swing.property.SelectionPropertyPanel;
import automenta.netention.swing.property.StringPropertyPanel;
import automenta.netention.swing.util.JHyperLink;
import automenta.netention.value.bool.BoolProp;
import automenta.netention.value.integer.IntProp;
import automenta.netention.value.real.RealProp;
import automenta.netention.value.set.SelectionProp;
import automenta.netention.value.string.StringProp;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
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
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

/**
 * Displays a detail and its links for view/edit
 *      Set Mode (real, imaginary)
 *      Add/Remove Patterns
 *      Add new Properties
 *      Remove existing Properties
 * @author seh
 */
abstract public class DetailEditPanel extends JPanel {

    public final JPanel sentences;
    private Detail detail;
    private final Self self;
    private boolean editable;
    //private final JSplitPane contentSplit;
    private final DetailMenuBar menuBar;
    private List<PropertyOptionPanel> optionPanels = new LinkedList();
    long updateDelayMS = 650;
    private final JTextArea nameEdit;
    //Tooltips
    final static String itsATooltip = "Selects Patterns for this Detail";
    final static String realOrImaginary = "Real details describe things that actually exist. \nImaginary details describe hypothetical or desired things.";
    boolean deletable;
    public final JPanel bottomBar;

    protected class LinkPanel extends JPanel {

        private final Link link;
        private final Node source;
        private final Node target;

        private LinkPanel(ValueDirectedEdge<Node, Link> edge) {
            super(new FlowLayout(FlowLayout.LEFT));

            Pair<Node> endpoints = self.getGraph().getEndpoints(edge);
            this.link = edge.getValue();
            this.source = endpoints.getFirst();
            this.target = endpoints.getSecond();

            setOpaque(false);

            if (link instanceof HasStrength) {
                double strength = ((HasStrength) link).getStrength();
                JProgressBar jp = new JProgressBar(0, 1000);
                jp.setValue((int) (strength * 1000.0));
                add(jp);
            }

            Node other = getOther();

            JHyperLink la = new JHyperLink(other.getName() + " (" + link.toString() + ")", "", 1.0f);
            if (other instanceof Detail) {
                la.setIcon(Icons.getDetailIcon(self, (Detail) other));
            }
            add(la);

//            JLabel s = new JLabel(((int) (link.getStrength() * 100.0)) + "%");
//            add(s);

        }

        public Node getOther() {
            if (source.equals(detail.getID())) {
                return target;
            }
            return source;
        }
    }

    protected class DetailMenuBar extends JMenuBar {

        float menuFontScale = 1.25f;

        public DetailMenuBar() {
            super();
        }

        protected void refresh() {
            removeAll();

            final JToggleButton realButton = new JToggleButton(" ! ");
            realButton.setToolTipText("Real");
            realButton.addActionListener(new ActionListener() {

                @Override public void actionPerformed(ActionEvent e) {
                    if (detail.getMode() == Mode.Real) {
                        realButton.setSelected(true);   //undo the change since its already the same
                    } else if (!switchMode(Mode.Real)) {
                        realButton.setSelected(false);
                    }
                }
            });
            final JToggleButton imaginaryButton = new JToggleButton(" ? ");
            imaginaryButton.setToolTipText("Imaginary");
            imaginaryButton.addActionListener(new ActionListener() {

                @Override public void actionPerformed(ActionEvent e) {
                    if (detail.getMode() == Mode.Imaginary) {
                        imaginaryButton.setSelected(true); //undo the change since its already the same
                    } else if (!switchMode(Mode.Imaginary)) {
                        imaginaryButton.setSelected(false);
                    }
                }
            });
            if (detail.getMode() == Mode.Real) {
                realButton.setSelected(true);
            } else if (detail.getMode() == Mode.Imaginary) {
                imaginaryButton.setSelected(true);
            }

            realButton.setEnabled(isEditable());
            imaginaryButton.setEnabled(isEditable());

            addButton(realButton);
            addButton(imaginaryButton);


            if (isEditable()) {
                JMenu t = new JMenu(/*"It's a..."*/);
                t.setIcon(Icons.getIcon("addPattern"));
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
            }
            
//            if (detail.getPatterns().size()!=0)
//                add(new JSeparator(JSeparator.VERTICAL));

            for (String pid : detail.getPatterns()) {
                final Pattern p = self.getPatterns().get(pid);
                JMenu j = new JMenu(p.getID());
                j.setIcon(Icons.getPatternIcon(p));
                int numItems = 0, numExists = 0, totalItems = p.keySet().size();

                if (isEditable()) {
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
                        else {
                            numExists++;
                        }
                    }
                    j.setText(j.getText() + " (" + numExists + "/" + (totalItems) + ")");
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

                } else {
                    j.setEnabled(false);
                }


                add(j);


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

        public void addButton(JComponent c) {
            //c.setFont(c.getFont().deriveFont(c.getFont().getSize2D() * menuFontScale)));
            c.setFont(new Font("Monospace", Font.PLAIN, (int) (c.getFont().getSize2D() * menuFontScale)));
            add(c);
        }
    }

    public DetailEditPanel(Self s, Detail d, boolean editable) {
        this(s, d, editable, true);
    }

    public DetailEditPanel(Self s, Detail d, boolean editable, boolean deletable) {
        super(new GridBagLayout());

        this.self = s;

        setEditable(editable);

        int b = 4;
        setBorder(new EmptyBorder(b, b, b, b));

        GridBagConstraints gc = new GridBagConstraints();
        {
            int ins = 1;
            gc.insets = new Insets(ins, ins, ins, ins);

            gc.weightx = 1.0;
            gc.weighty = 0;
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
        nameEdit = new JTextArea(d.getName());
        nameEdit.setEditable(isEditable());
        nameEdit.setOpaque(isEditable());
        nameEdit.setWrapStyleWord(true);
        nameEdit.setLineWrap(true);
        nameEdit.setFont(nameEdit.getFont().deriveFont(nameEdit.getFont().getSize2D() * 1.7f));
        add(nameEdit, gc);

        gc.gridy++;

        add(menuBar, gc);

        {
            gc.weightx = 1.0;
            gc.weighty = 1.0;
            gc.fill = gc.BOTH;
            gc.anchor = gc.NORTHWEST;
            gc.gridx = 1;
            gc.gridy = 3;
        }


        sentences = new JPanel(new GridBagLayout());

        add(new JScrollPane(sentences), gc);

        {
            bottomBar = new JPanel(new FlowLayout(FlowLayout.RIGHT));



            if (isDeletable()) {
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
                bottomBar.add(deleteButton);
            }

            if (isEditable()) {
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

                bottomBar.add(updateButton);
            } else {
                final JButton refreshButton = new JButton("Refresh");
                bottomBar.add(refreshButton);
            }

            GridBagConstraints gcx = new GridBagConstraints();
            gcx.weightx = 1.0;
            gcx.weighty = 0.0;
            gcx.fill = gc.NONE;
            gcx.anchor = gc.SOUTHEAST;
            gcx.gridx = 1;
            gcx.gridy = 4;

            add(bottomBar, gcx);

        }

        setDetail(d);

    }

    public boolean isDeletable() {
        return deletable;
    }

    public void setDeletable(boolean deletable) {
        this.deletable = deletable;
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

    public void setDetail(Detail d) {
        this.detail = d;
        sentences.removeAll();

        nameEdit.setText(d.getName());

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
            gc.ipadx = 4;
            gc.ipady = 4;
            gc.insets = new Insets(4,4,4,4);

            optionPanels.clear();

            for (final PropertyValue pv : detail.getProperties()) {
                gc.gridy++;
                JComponent nextLine = getLinePanel(pv);
                if (nextLine instanceof PropertyOptionPanel) {
                    PropertyOptionPanel pop = (PropertyOptionPanel) nextLine;
                    final Property property = self.getProperty(pv.getProperty());
                    
                    if (isEditable()) {
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

                        pop.setPopup(popup);
                    }

                    optionPanels.add(pop);
                }

                sentences.add(nextLine, gc);
            }

            self.updateLinks(new Runnable() {

                @Override
                public void run() {
                    Set<ValueDirectedEdge<Node, Link>> ae = self.getGraph().getAdjacentEdges(detail);

                    if (ae != null) {
                        for (ValueDirectedEdge<Node, Link> e : ae) {
                            gc.gridy++;
                            sentences.add(new LinkPanel(e), gc);
                        }
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
        } else if (prop instanceof SelectionProp) {
            return new SelectionPropertyPanel(self, detail, (SelectionProp)prop, pv, editable);
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

    public boolean isEditable() {
        return editable;
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

        if ((detail.getPatterns().size() == 0) && (detail.getMode() == Mode.Unknown)) {
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
        detail.setName(nameEdit.getText());
        for (PropertyOptionPanel pop : optionPanels) {
            pop.widgetToValue();
        }

    }

    abstract protected void deleteThis();

    abstract protected void patternChanged();
}
