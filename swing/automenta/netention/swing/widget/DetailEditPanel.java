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
import automenta.netention.graph.ValueEdge;
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
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.swing.BorderFactory;
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
import javax.swing.JSplitPane;
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
    private JTextArea nameEdit;
    //Tooltips
    final static String itsATooltip = "Selects Patterns for this Detail";
    final static String realOrImaginary = "Real details describe things that actually exist. \nImaginary details describe hypothetical or desired things.";
    boolean deletable;
    public final JPanel bottomBar;
    private final JPanel links;
    private final JSplitPane mainSplit;


    protected class LinkPanel extends JPanel {

        private final Link link;
        private final Node source;
        private final Node target;

        private LinkPanel(ValueEdge<Node, Link> edge) {
            super(new FlowLayout(FlowLayout.LEFT));

            //Pair<Node> endpoints = self.getGraph().getEndpoints(edge);
            this.link = edge.getValue();
            this.source = edge.getSourceNode();
            this.target = edge.getDestinationNode();

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

    private void buildAvailablePropertiesMenu(JMenu b) {
        String[] lp = new String[detail.getPatterns().size()];
        detail.getPatterns().toArray(lp);
        
        for (final Property p : self.getAvailableProperties(detail, lp).keySet()) {
            JMenuItem j = new JMenuItem(p.getName());
            j.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override public void run() {
                            addProperty(p);
                        }                        
                    });
                }                
            });
            b.add(j);
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
            
            realButton.setSelected(detail.getMode() == Mode.Real);
            imaginaryButton.setSelected(detail.getMode() == Mode.Imaginary);

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

            realButton.setEnabled(isEditable());
            imaginaryButton.setEnabled(isEditable());
            

            addButton(realButton);
            addButton(imaginaryButton);


            if (isEditable()) {
                JMenu t = new JMenu(/*"It's a..."*/);
                t.setIcon(Icons.getIcon("addPattern"));
                t.setToolTipText(itsATooltip);
                
                buildPatternMenu(t);
                add(t);
            }
            

            
//            if (detail.getPatterns().size()!=0)
//                add(new JSeparator(JSeparator.VERTICAL));

            for (String pid : detail.getPatterns()) {
                final Pattern p = self.getPatterns().get(pid);
                JMenu j = new JMenu(p.getName());
                j.setIcon(Icons.getPatternIcon(p));
                int numItems = 0, numExists = 0;
                
                Collection<String> cp = self.getProperties(p);
                int totalItems = cp.size();

                if (isEditable()) {
                    
                    for (String propid : cp) {
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
                        } else {
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

        
        public void buildPatternMenu(final JMenu t, final String pid, final JMenu parent) {
            final Pattern p = self.getPatterns().get(pid);

            JMenuItem ti = new JMenuItem(p.getName());
            ti.setEnabled(!detail.getPatterns().contains(pid));
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
            
            Collection<String> children = self.getSubPatterns(pid);
            if (children.size() > 0) {
                JMenu ji = new JMenu(p.getName());
                ji.setIcon(Icons.getPatternIcon(p));

                ji.add(ti);
                ji.addSeparator();
                
                if (parent!=null) {
                    parent.add(ji);
                }
                else {
                    t.add(ji);
                }
                
                for (String s : children) {
                    buildPatternMenu(t, s, ji);
                }
            }
            else {
                if (parent!=null) {
                    parent.add(ti);
                }
                else {
                    t.add(ti);
                }
                
            }
            
            
            
            
        }
        
        public void buildPatternMenu(JMenu t) {
                
            List<String> roots = new LinkedList();
            for (Pattern p : self.getPatterns().values()) {
                if (p.getParents().isEmpty())
                    roots.add(p.id);
            }
            
            for (String pid : roots) {
                buildPatternMenu(t, pid, null);
            }
            
//                for (String pid : self.getAvailablePatterns(detail)) {
//                    final Pattern p = self.getPatterns().get(pid);
//                    JMenuItem ti = new JMenuItem(p.getID());
//                    ti.setIcon(Icons.getPatternIcon(p));
//                    ti.addActionListener(new ActionListener() {
//
//                        @Override public void actionPerformed(ActionEvent e) {
//                            SwingUtilities.invokeLater(new Runnable() {
//
//                                @Override public void run() {
//                                    addPattern(p);
//                                }
//                            });
//                        }
//                    });
//                    t.add(ti);
//                }
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

        add(menuBar, gc);

        gc.gridy++;




        {
            gc.weightx = 1.0;
            gc.weighty = 1.0;
            gc.fill = gc.BOTH;
            gc.anchor = gc.NORTHWEST;
            gc.gridx = 1;
            //gc.gridy = 3;
        }


        sentences = new JPanel(new GridBagLayout());
        links = new JPanel(new GridBagLayout());
        
        mainSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        mainSplit.setTopComponent(new JScrollPane(sentences));
        mainSplit.setBottomComponent(new JScrollPane(links));
        mainSplit.setDividerLocation(0.5f);
        
        add(mainSplit, gc);

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

        if (detail.getValues().size() > 0) {
            if (nextMode == Mode.Unknown) {
                if (detail.getPatterns().size() > 0) {
                    if (0 == JOptionPane.showConfirmDialog(DetailEditPanel.this, "Change mode to Thought?  This will remove all properties.", "Change Mode", JOptionPane.YES_NO_OPTION)) {
                        detail.getValues().clear();
                        updateDetail();
                        refreshUI();
                        return true;
                    }
                } else {
                    detail.getValues().clear();
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
                    for (PropertyValue p : detail.getValues()) {
                        existingProperties.add(self.getProperty(p.getProperty()));
                    }

                    detail.getValues().clear();
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

    protected void setDetailName(String s) {
        detail.setName(s);
    }
    
    public void setDetail(Detail d) {
        this.detail = d;
        sentences.removeAll();
        links.removeAll();

        menuBar.refresh();

        final GridBagConstraints gc = new GridBagConstraints();

        //if (d.getPatterns().size() == 0) {
        //} else {

            sentences.setAlignmentY(TOP_ALIGNMENT);

            gc.weightx = 1.0;
            gc.weighty = 0.0;
            gc.anchor = gc.NORTHWEST;
            gc.gridx = 1;
            gc.gridy = 1;
            gc.ipadx = 4;
            gc.ipady = 4;
            gc.insets = new Insets(4, 4, 4, 4);

            optionPanels.clear();

            gc.fill = gc.HORIZONTAL;
            
            nameEdit = new JTextArea(d.getName());
            nameEdit.setEditable(isEditable());
            nameEdit.setOpaque(isEditable());
            nameEdit.addKeyListener(new KeyListener() {

                @Override
                public void keyTyped(KeyEvent e) {
                    setDetailName(nameEdit.getText());
                }

                @Override
                public void keyPressed(KeyEvent e) {
                    setDetailName(nameEdit.getText());
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    setDetailName(nameEdit.getText());
                }

            });
            //nameEdit.setWrapStyleWord(true);
            //nameEdit.setLineWrap(true);
            //nameEdit.setFont(nameEdit.getFont().deriveFont(nameEdit.getFont().getSize2D() * 1.7f));
            sentences.add(nameEdit, gc);

            gc.fill = gc.NONE;

            for (final PropertyValue pv : detail.getValues()) {
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
                    nextLine = stylePropertyPanel(pop);
                }

                sentences.add(nextLine, gc);
            }

            if (isEditable()) {

                JPanel addPropertyPanel = new JPanel(new BorderLayout());
                {
                    JMenuBar bb = new JMenuBar();
                    
                    JMenu b = new JMenu();
                    b.setIcon(Icons.getIcon("addPattern"));
                    b.setToolTipText("Add Property");
                    buildAvailablePropertiesMenu(b);
                    
                    bb.add(b);

                    addPropertyPanel.add(bb);
                }
                gc.gridy++;
                sentences.add(addPropertyPanel, gc);
            }
            

            
            self.updateLinks(new Runnable() {

                @Override
                public void run() {
                    Set<ValueEdge<Node, Link>> ae = self.getGraph().getAdjacentEdges(detail);
                    GridBagConstraints gc = new GridBagConstraints();
                    gc.gridy = 0;

                    if (ae != null) {
                        for (ValueEdge<Node, Link> e : ae) {
                            gc.gridy++;
                            links.add(new LinkPanel(e), gc);
                        }
                    }
                    
                    gc.gridy++;
                    gc.fill = gc.VERTICAL;
                    gc.weighty = 1.0;
                    links.add(Box.createVerticalBox(), gc);

                    updateUI();
                }
            }, detail);


            gc.gridy++;
            gc.fill = gc.VERTICAL;
            gc.weighty = 1.0;
            sentences.add(Box.createVerticalBox(), gc);
        //}

        //headerIcon.setIcon(Icons.getDetailIcon(self, d));

        updateUI();

    }

    public JComponent stylePropertyPanel(PropertyOptionPanel p) {
        p.setBorder(BorderFactory.createLoweredSoftBevelBorder());
        return p;
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
            return new SelectionPropertyPanel(self, detail, (SelectionProp) prop, pv, editable);
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

        if (!detail.getPatterns().contains(p.getID())) {
            updateDetail(); //TODO this assumes that the data is to be updated when patterns changed.  is this right?
            detail.getPatterns().add(p.getID());
            patternChanged();
            refreshUI();
        }
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
        detail.getValues().add(pv);
        refreshUI();
    }

    synchronized protected void removeProperty(PropertyValue pv) {
        detail.getValues().remove(pv);
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
