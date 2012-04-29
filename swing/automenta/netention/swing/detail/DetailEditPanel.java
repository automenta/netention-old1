/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.swing.detail;

import automenta.netention.Link.HasStrength;
import automenta.netention.*;
import automenta.netention.action.DetailAction;
import automenta.netention.graph.ValueEdge;
import automenta.netention.index.SchemaIndex;
import automenta.netention.index.SchemaIndex.SchemaComponent;
import automenta.netention.index.SchemaIndex.SchemaResult;
import automenta.netention.swing.Icons;
import automenta.netention.swing.property.*;
import automenta.netention.swing.util.JHyperLink;
import automenta.netention.swing.util.JScaledTextArea;
import automenta.netention.swing.util.SwingWindow;
import automenta.netention.swing.widget.CompletePropertiesPanel;
import automenta.netention.swing.widget.PropertySearch;
import automenta.netention.value.Comment;
import automenta.netention.value.bool.BoolProp;
import automenta.netention.value.integer.IntProp;
import automenta.netention.value.node.NodeProp;
import automenta.netention.value.real.RealProp;
import automenta.netention.value.set.SelectionProp;
import automenta.netention.value.string.StringProp;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.*;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Displays a detail and its links for view/edit Set Mode (real, imaginary)
 * Add/Remove Patterns Add new Properties Remove existing Properties
 *
 * @author seh
 */
abstract public class DetailEditPanel extends JPanel {
    private static final Logger logger = Logger.getLogger(DetailEditPanel.class.toString());
    
    private static boolean richComments = false;

    public final JPanel sentences;
    private Detail detail;
    private final Self self;
    private boolean editable;
    //private final JSplitPane contentSplit;
    private final DetailMenuBar menuBar;
    private List<PropertyOptionPanel> optionPanels = new LinkedList();
    long updateDelayMS = 650;
    private JScaledTextArea nameEdit;
    //Tooltips
    final static String itsATooltip = "Selects Patterns for this Detail";
    final static String realOrImaginary = "Real details describe things that actually exist. \nImaginary details describe hypothetical or desired things.";
    boolean deletable;
    public final JPanel bottomBar;
    private final JPanel links;
    private final JSplitPane mainSplit;
    private JPanel addPropertyPanel;
    private JPanel belowName;
    private final SchemaIndex index;


    protected class LinkPanel extends JPanel {

        private final Link link;
        private final Node source;
        private final Node target;

        private LinkPanel(ValueEdge<Node, Link> edge, double groupMin, double groupMax) {
            super(new FlowLayout(FlowLayout.LEFT));

            //Pair<Node> endpoints = self.getGraph().getEndpoints(edge);
            this.link = edge.getValue();
            this.source = edge.getSourceNode();
            this.target = edge.getDestinationNode();

            setOpaque(false);

            if (link instanceof HasStrength) {
                double strength = ((HasStrength) link).getStrength();
                JProgressBar jp = new JProgressBar(0, 1000);
                jp.setValue((int) ((strength / groupMax) * 1000.0));
                jp.setToolTipText(Double.toString(strength));
                add(jp);
            }

            Node other = getOther();

            JHyperLink la = new JHyperLink(other.getName() + " (" + link.toString() + ")", "", 1.0f);
            if (other instanceof Detail) {
                la.setIcon(Icons.getDetailIcon(self, (Detail) other));
            }
            add(la);


        }

        public Node getOther() {
            if (source.getID().equals(detail.getID())) {
                return target;
            }
            return source;
        }
    }

    private int buildAvailablePropertiesMenu(JMenu b) {
        int count = 0;
        b.removeAll();
        for (final Property p : self.getAvailableProperties(detail).keySet()) {
            JMenuItem j = new JMenuItem(p.getName());
            j.setToolTipText(p.getID());
            j.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    SwingUtilities.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            addProperty(p);
                        }
                    });
                }
            });
            b.add(j);
            count++;
        }
        return count;
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

                @Override
                public void actionPerformed(ActionEvent e) {
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

                @Override
                public void actionPerformed(ActionEvent e) {
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
                JMenu t = new JMenu(/*
                         * "It's a..."
                         */);
                t.setIcon(Icons.getIcon("addPattern"));
                t.setToolTipText(itsATooltip);

                {
                    JMenuItem findItem = new JMenuItem("Find...");
                    findItem.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            new SwingWindow(new FindPatternPanel(), 500, 300).setTitle("Find Patterns or Properties...");
                        }                        
                    });
                    t.add(findItem);

                    t.addSeparator();
                }
                
                buildPatternMenu(t);
                add(t);
            }



//            if (detail.getPatterns().size()!=0)
//                add(new JSeparator(JSeparator.VERTICAL));

            for (String pid : detail.getPatterns()) {
                final Pattern p = self.getPattern(pid);
                if (p == null) {
                    //log error?
                    continue;
                }

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

                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    SwingUtilities.invokeLater(new Runnable() {

                                        @Override
                                        public void run() {
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
                    JMenuItem remove = new JMenuItem("Remove " + p.getName() + "...");
                    remove.setToolTipText(p.getID());
                    remove.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String extraMessage = "";
                            if (detail.getPatterns().size() == 1) {
                                extraMessage = "\nThis will remove all properties since there will be no patterns remaining.";
                            }

                            if (JOptionPane.showConfirmDialog(DetailEditPanel.this, "Remove " + p.getID() + " from this detail?" + extraMessage, "Remove", JOptionPane.OK_CANCEL_OPTION) == 0) {
                                SwingUtilities.invokeLater(new Runnable() {

                                    @Override
                                    public void run() {
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
            final Pattern p = self.getPattern(pid);

            JMenuItem ti = new JMenuItem(p.getName());
            ti.setIcon(Icons.getPatternIcon(p));
            ti.setEnabled(willAcceptPattern(detail, pid));
            ti.setToolTipText(p.getID());
            if (ti.isEnabled()) {
                ti.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        SwingUtilities.invokeLater(new Runnable() {

                            @Override
                            public void run() {
                                addPattern(p);
                            }
                        });
                    }
                });
            }

            Collection<String> children = self.getSubPatterns(pid);
            if (children.size() > 0) {
                JMenu ji = new JMenu(p.getName());
                ji.setIcon(Icons.getPatternIcon(p));

                ti.setFont(ti.getFont().deriveFont(Font.BOLD));
                ji.add(ti);
                ji.addSeparator();

                if (parent != null) {
                    parent.add(ji);
                } else {
                    t.add(ji);
                }

                for (String s : children) {
                    buildPatternMenu(t, s, ji);
                }
            } else {
                if (parent != null) {
                    parent.add(ti);
                } else {
                    t.add(ti);
                }

            }




        }

        public void buildPatternMenu(JMenu t) {

            List<String> roots = new LinkedList();
            for (String sp : self.getPatterns()) {
                Pattern p = self.getPattern(sp);
                if (p.getParents().isEmpty()) {
                    roots.add(p.getID());
                }
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

        private boolean willAcceptPattern(final Detail detail, final String pid) {
            if (detail.getPatterns().contains(pid)) {
                return false;
            }
            for (final String d : detail.getPatterns()) {

                //if pid is a superclass of d, return false
                if (self.isSuperPattern(pid, d)) {
                    return false;
                }
            }
            return true;
        }
    }

    public DetailEditPanel(Self s, Detail d, boolean editable) {
        this(s, d, editable, true);
    }

    public DetailEditPanel(Self s, Detail d, boolean editable, boolean deletable) {
        //super(new GridBagLayout());
        super(new BorderLayout());

        this.self = s;
        this.detail = d;
        this.index = new SchemaIndex(s); //TODO pass this as constructor arg to avoid reconstructing it each time this component is generated

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

        add(menuBar, BorderLayout.NORTH);

        {
            gc.gridy++;
            gc.weightx = 1.0;
            gc.weighty = 0.99;
            gc.fill = gc.BOTH;
            gc.anchor = gc.NORTHWEST;
            gc.gridx = 1;
        }


        sentences = new JPanel(new GridBagLayout());
        sentences.setBackground(getBackgroundColor());
        
        links = new JPanel(new GridBagLayout());

        mainSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        mainSplit.setTopComponent(new JScrollPane(sentences));
        mainSplit.setBottomComponent(new JScrollPane(links));

        add(mainSplit, BorderLayout.CENTER);

        {
            bottomBar = new JPanel(new FlowLayout(FlowLayout.RIGHT));

            GridBagConstraints gcx = new GridBagConstraints();
            gcx.weightx = 1.0;
            gcx.weighty = 0.01;
            gcx.fill = gc.BOTH;
            gcx.anchor = gc.NORTH;
            gcx.gridx = 1;
            gcx.gridy = 3;

            add(new JScrollPane(bottomBar), BorderLayout.SOUTH);

        }


        refreshUI();

    }

    public Color getBackgroundColor() { return Color.WHITE; }
    
    public synchronized void refreshBottomBar() {

        bottomBar.removeAll();

        
        if (isDeletable()) {
            final JButton deleteButton = new JButton("Delete");
            deleteButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (0 == JOptionPane.showConfirmDialog(DetailEditPanel.this, "Delete this detail?", "Delete", JOptionPane.YES_NO_OPTION)) {
                        SwingUtilities.invokeLater(new Runnable() {

                            @Override
                            public void run() {
                                deleteThis();
                            }
                        });
                    }
                }
            });
            bottomBar.add(deleteButton);
        } 

        for (final DetailAction da : self.getDetailActions(detail)) {
                final JButton b = new JButton(da.getLabel());
                b.setToolTipText(da.getDescription());
                b.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        SwingUtilities.invokeLater(new Runnable() {

                            @Override public void run() {
                                //TODO use a global threadpool
                                
                                Runnable rr = new Runnable() {

                                    @Override public void run() {
                                        da.getRun(self, detail).run();
                                        refreshUI();   
                                    }
                                    
                                };
                                
                                //new Thread(rr).start();
                                
                                self.queue(rr);
                            }
                        });
                    }
                });

                bottomBar.add(b);

        }

        if (isEditable()) {
            final JButton updateButton = new JButton("Update");
            updateButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    SwingUtilities.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            updateButton.setEnabled(false);
                            saveToDetail();
                            refreshLinks();
                            refreshBottomBar();
                            updateUI();
                            new Thread(new Runnable() {

                                @Override
                                public void run() {
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
        
        bottomBar.updateUI();


    }

    public boolean isDeletable() {
        return deletable;
    }

    public void setDeletable(boolean deletable) {
        this.deletable = deletable;
    }

    /**
     * returns false if the switch was cancelled. if no switch was necessary, it
     * returns true.
     *
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
                        saveToDetail();
                        refreshUI();
                        return true;
                    }
                } else {
                    detail.getValues().clear();
                    saveToDetail();
                    patternChanged();
                    refreshUI();
                    return true;
                }
                return false;
            } else {
                String modeName = (nextMode == Mode.Real) ? "Real" : "Imaginary";
                if (0 == JOptionPane.showConfirmDialog(DetailEditPanel.this, "Change mode to " + modeName + "?  This will reset all properties.", "Change Mode", JOptionPane.YES_NO_OPTION)) {
                    detail.setMode(nextMode);

                    List<PropertyValue> existingProperties = new LinkedList(detail.getValues());
                    
                    detail.getValues().clear();
                    for (PropertyValue p : existingProperties) {
                        if (p instanceof Comment) {
                            addComment( ((Comment)p).getText() ) ;
                        }
                        else {
                            addProperty(self.getProperty(p.getProperty()));
                        }
                    }


                    saveToDetail();

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
        s = s.trim();
        
        if (detail.getName().equals(s))
            return;
        
        detail.setName(s);
        
        final String x = s;
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                List<SchemaResult> suggestions = index.getSuggestions(x);
                belowName.removeAll();
                for (final SchemaResult r : suggestions) {
                    if (r.type == SchemaComponent.Pattern) {
                        if (canAddPattern(r.id)) {
                            final JButton j = new JButton(r.toString(self));
                            j.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent ae) {
                                    addPattern(self.getPattern(r.id));
                                    belowName.remove(j);
                                    belowName.updateUI();
                                }                                
                            });
                            belowName.add(j);
                        }
                    }
                    else if (r.type == SchemaComponent.Property) {
                        if (self.acceptsAnotherProperty(detail, r.id)) {
                            final JButton j = new JButton(r.toString(self));
                            j.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent ae) {
                                    List<Pattern> lp = self.getPatternsOfProperty(r.id);
                                    for (Pattern alreadyAdded : detail.getPatterns(self)) {
                                        lp.remove(alreadyAdded);
                                    }
                                    
                                    addProperty(self.getProperty(r.id));
                                    
                                    if (lp.size() == 1) {
                                        addPattern(lp.get(0));
                                    }
                                    else if (lp.size() > 1) {
                                        //TODO pop up a question box allowing selection                                        
                                        logger.severe("Multiple patterns relevant to added, but adding only the first. TODO pop up a question box allowing selection: " + lp);
                                        addPattern(lp.get(0));                                        
                                    }
                                    
                                    belowName.remove(j);
                                    belowName.updateUI();
                                }                                
                            });
                            belowName.add(j);
                        }
                    }
                }
                belowName.updateUI();
            }
            
        });
    }

    public synchronized void setDetail(Detail d) {
        this.detail = d;
        sentences.removeAll();

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

        nameEdit = new JScaledTextArea(d.getName(), 1.5f);

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
        
        //belowName = new JPanel(new FlowLayout());
        belowName = new JPanel(new GridLayout(0, 3, 4, 4));
        
        JPanel subjectWrapper = new JPanel(new BorderLayout());
        //subjectWrapper.add(new JScrollPane(nameEdit, JScrollPane.VERTICAL_SCROLLBAR, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);
        subjectWrapper.add(nameEdit, BorderLayout.CENTER);
        subjectWrapper.add(belowName, BorderLayout.SOUTH);
        int w = 4;        
        subjectWrapper.setBorder(BorderFactory.createEmptyBorder(w, w, w, w));
        
        sentences.add(subjectWrapper, gc);

        gc.fill = gc.NONE;

        int line = 0;
        for (final PropertyValue pv : detail.getValues()) {
            final int lline = line;
            gc.gridy++;
            JComponent nextLine = getLinePanel(pv);
            if (nextLine instanceof PropertyOptionPanel) {
                PropertyOptionPanel pop = (PropertyOptionPanel) nextLine;

                final Property property = self.getProperty(pv.getProperty());

                if (isEditable()) {
                    JPopupMenu popup = new JPopupMenu();
                   
                    if (line!=0) {
                        JMenuItem moveUp = new JMenuItem("Move Up");
                        moveUp.addActionListener(new ActionListener() {
                            @Override public void actionPerformed(ActionEvent e) {
                                detail.getValues().remove(pv);
                                detail.getValues().add(lline-1,pv);
                                refreshUI();
                            }                        
                        });
                        popup.add(moveUp);
                    }
                    
                    
                    if (line!=detail.getValues().size()-1) {
                        JMenuItem moveDown = new JMenuItem("Move Down");
                        moveDown.addActionListener(new ActionListener() {
                            @Override public void actionPerformed(ActionEvent e) {
                                detail.getValues().remove(pv);
                                detail.getValues().add(lline+1,pv);
                                refreshUI();
                            }                        
                        });
                        popup.add(moveDown);
                    }
                    
                    popup.addSeparator();
                    
                    JMenuItem removeItem = new JMenuItem("Remove");

                    boolean required = false;
                    if (property!=null) {
                        int minCard = property.getCardinalityMin();
                        if (minCard > 0) {
                            if (Self.getPropertyCount(d, property.getID()) - 1 < minCard) {
                                required = true;
                            }
                        }
                    }

                    if (required) {
                        removeItem.setEnabled(false);

//                            removeItem.setText("Remove all " + property.getName() + " properties...");
//                            removeItem.addActionListener(new ActionListener() {
//
//                                @Override public void actionPerformed(ActionEvent e) {
//                                
//                                    removePattern(property.get);
//                                }
//                            });
                    } else {
                        removeItem.addActionListener(new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                String name = "Comment";
                                if (property!=null) name = property.getName();
                                if (0 == JOptionPane.showConfirmDialog(DetailEditPanel.this, "Remove " + name + "?", "Remove", JOptionPane.YES_NO_OPTION)) {
                                    removeProperty(pv);
                                }
                            }
                        });
                    }

                    popup.add(removeItem);

                    pop.setPopup(popup);
                }
                optionPanels.add(pop);
                nextLine = stylePropertyPanel(pop);
            }

            sentences.add(nextLine, gc);
            line++;
        }

        if (isEditable()) {

            {
                
                JMenuBar bb = new JMenuBar();
                bb.setOpaque(false);
                bb.setBorderPainted(false);

                JMenu b = new JMenu();

                if (buildAvailablePropertiesMenu(b) > 0) {
                    b.setIcon(Icons.getIcon("addPattern"));
                    b.setToolTipText("Add Property");
                    bb.add(b);
                }

                JButton pm = new JButton(Icons.getFileIcon("media://tango32/actions/edit-find.png", 24, 24));
                pm.setToolTipText("Find Property");
                pm.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        addSearchedProperty();
                    }                    
                });
                bb.add(pm);
                
                JButton cm = new JButton(Icons.getFileIcon("media://tango32/actions/format-indent-more.png", 24, 24));
                cm.setToolTipText("Add Comment");
                cm.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        addComment();
                    }                    
                });
                bb.add(cm);

                if (buildAvailablePropertiesMenu(b) > 0) {

                    JButton c = new JButton(Icons.getFileIcon("media://tango32/actions/media-seek-forward.png", 24, 24));
                    c.setToolTipText("Complete All");
                    c.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            JDialog jd = new JDialog();
                            jd.setSize(600, 400);
                            jd.getContentPane().add(new CompletePropertiesPanel(self, detail, new Runnable() {

                                @Override
                                public void run() {
                                    refreshUI();
                                }
                            }));
                            jd.setVisible(true);
                        }
                    });
                    bb.add(c);

                }
                
                addPropertyPanel = new JPanel(new BorderLayout());
                addPropertyPanel.add(bb, BorderLayout.CENTER);
                addPropertyPanel.setOpaque(false);
                gc.anchor = GridBagConstraints.EAST;
                gc.gridy++;
                sentences.add(addPropertyPanel, gc);

            }
        }

        gc.gridy++;
        gc.fill = gc.VERTICAL;
        gc.weighty = 1.0;
        sentences.add(Box.createVerticalBox(), gc);
        //}

        //headerIcon.setIcon(Icons.getDetailIcon(self, d));

        refreshLinks();

        refreshBottomBar();

        mainSplit.setContinuousLayout(true);
        mainSplit.setOneTouchExpandable(true);
        mainSplit.setResizeWeight(1.0);
        mainSplit.setDividerLocation(0.8f);

        updateUI();

    }

    public void refreshLinks() {
        links.removeAll();
        self.updateLinks(new Runnable() {

            @Override
            public void run() {
                Set<ValueEdge<Node, Link>> ae = self.getGraph().getAdjacentEdges(detail);
                GridBagConstraints gc = new GridBagConstraints();
                gc.gridy = 0;
                
                if (ae != null) {
                    double minStrength = 0;
                    double maxStrength = 0;
                    int cc = 0;
                    for (ValueEdge<Node, Link> e : ae) {
                        Link l = e.getValue();
                        if (l instanceof HasStrength) {
                            double s = ((HasStrength)l).getStrength();
                           
                            if ((cc == 0) || (s > maxStrength)) maxStrength = s;
                            if ((cc == 0) || (s < minStrength)) minStrength = s;
                            
                            cc++;
                        }
                                                    
                    }
                    
                    //sort
                    List<ValueEdge<Node, Link>> be = new ArrayList(ae);
                    Collections.sort(be, new Comparator<ValueEdge<Node, Link>>() {
                        @Override public int compare(final ValueEdge<Node, Link> a, final ValueEdge<Node, Link> b) {
                            double as = 0;
                            double bs = 0;
                            if (a.getValue() instanceof HasStrength)
                                as = ((HasStrength)a.getValue()).getStrength();
                            if (b.getValue() instanceof HasStrength)
                                bs = ((HasStrength)b.getValue()).getStrength();
                            return Double.compare(bs, as);
                        }                        
                    });
                    
                    for (ValueEdge<Node, Link> e : be) {
                        gc.gridy++;
                        gc.anchor = GridBagConstraints.WEST;
                        links.add(new LinkPanel(e, minStrength, maxStrength), gc);
                    }
                }

                gc.gridy++;
                gc.fill = gc.VERTICAL;
                gc.weighty = 1.0;
                links.add(Box.createVerticalBox(), gc);

                links.updateUI();
            }
        }, detail);
        
    }
    
    public JComponent stylePropertyPanel(PropertyOptionPanel p) {
        //p.setBorder(BorderFactory.createLoweredBevelBorder());
        return p;
    }

    public static JComponent getLinePanel(Self self, Detail detail, final PropertyValue pv, boolean editable) {
        if (pv instanceof Comment) {
            if (richComments)
                return new RichCommentPanel(self, detail, (Comment)pv, editable);
            else
                return new CommentPanel(self, detail, (Comment)pv, editable);
        }
        
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
        } else if (prop instanceof NodeProp) {
            return new NodePropertyPanel(self, detail, pv, editable);
        }

        return new JLabel(pv.toString());

    }

    protected JComponent getLinePanel(PropertyValue pv) {
        return getLinePanel(self, detail, pv, editable);
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
    
    public boolean canAddPattern(String patternID) {
        return !detail.getPatterns().contains(patternID);
    }

    synchronized protected void addPattern(Pattern p) {

        if (canAddPattern(p.getID())) {
            saveToDetail(); //TODO this assumes that the data is to be updated when patterns changed.  is this right?

            detail.getPatterns().add(p.getID());

            for (String x : self.getProperties(p)) {
                Property px = self.getProperty(x);
                for (int i = 0; i < self.moreValuesRequired(detail, x); i++) {
                    addProperty(px);
                }
            }

            patternChanged();
            refreshUI();
        }
    }

    synchronized protected void removePattern(Pattern p) {
        saveToDetail(); //TODO this assumes that the data is to be updated when patterns changed.  is this right?

        detail.getPatterns().remove(p.getID());
        //TODO remove properties?
        patternChanged();
        refreshUI();
    }
    
    synchronized protected  void addComment(String text) {
        Comment c = new Comment(text);
        detail.getValues().add(c);
        refreshUI();        
    }
    
    synchronized protected void addSearchedProperty() {
        PropertySearch ps = new PropertySearch(self, detail) {

            @Override
            public void onSelected(String property, List<String> patterns) {
                    addProperty(self.getProperty(property));
                    for (String x : patterns)
                        addPattern(self.getPattern(x));
                    addPropertyPanel.remove(this);
                    addPropertyPanel.updateUI();
                
            }

            @Override
            public void onCancel() {
                addPropertyPanel.remove(this);
                addPropertyPanel.updateUI();
            }
            
        };
        addPropertyPanel.add(ps, BorderLayout.SOUTH);
        addPropertyPanel.updateUI();
    }
    
    synchronized protected void addComment() {
        addComment("");
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

    /**
     * writes contents of UI widgets to the detail
     */
    protected synchronized void saveToDetail() {
        for (PropertyOptionPanel pop : optionPanels) {
            pop.widgetToValue();
        }

    }

    abstract protected void deleteThis();

    abstract protected void patternChanged();
}
