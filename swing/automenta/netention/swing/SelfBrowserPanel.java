/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.swing;

import automenta.netention.Detail;
import automenta.netention.Pattern;
import automenta.netention.impl.MemorySelf;
import automenta.netention.swing.util.SwingWindow;
import automenta.netention.swing.widget.DetailEditPanel;
import automenta.netention.swing.widget.NewDetailPanel;
import automenta.netention.swing.widget.PatternEditPanel;
import automenta.netention.swing.widget.SelfBrowserView;
import automenta.netention.swing.widget.WhatTreePanel;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Displays a list of one's Details and a tabbed viewer of them 
 */
public class SelfBrowserPanel extends JPanel {

    private final JSplitPane content;
    private SelfBrowserView typeTreePanel;
    private final MemorySelf self;
    private final JPanel contentPanel;

    int contentMargin = 6;

    public class ViewMenu extends JMenu implements ActionListener {
        private final JRadioButtonMenuItem where;
        private final JRadioButtonMenuItem what;
        private final JRadioButtonMenuItem who;
        private final ButtonGroup group;

        public ViewMenu() {
            super();
            setToolTipText("Views");

            what = new JRadioButtonMenuItem("What", Icons.getObjectIcon("what"));
            what.addActionListener(this);
            who = new JRadioButtonMenuItem("Who", Icons.getObjectIcon("who"));
            who.addActionListener(this);
            where = new JRadioButtonMenuItem("Where", Icons.getObjectIcon("where"));
            where.addActionListener(this);

            add(what);
            add(who);
            add(where);

            group = new ButtonGroup();
            group.add(what);
            group.add(who);
            group.add(where);

            setIcon(what.getIcon());
            what.setSelected(true);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (what.isSelected()) {
                setIcon(what.getIcon());
                viewWhat();
            }
            else if (who.isSelected()) {
                setIcon(who.getIcon());
                viewWho();
            }
            else if (where.isSelected()) {
                setIcon(where.getIcon());
                viewWhere();
            }
        }

        //        JMenu viewMenu = new JMenu(/*"View"*/);
//        viewMenu.setIcon(Icons.getObjectIcon("view"));
//        viewMenu.setToolTipText("Views");

    }

    public SelfBrowserPanel(final MemorySelf self) {
        super(new BorderLayout());

        this.self = self;

        JMenuBar menubar = new JMenuBar();

        JMenu newMenu = new JMenu(/*"Add"*/);
        newMenu.setIcon(Icons.getObjectIcon("add"));
        newMenu.setToolTipText("Add...");
        {
            JMenuItem newDetail = new JMenuItem("Detail...");
            newDetail.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
            newDetail.addActionListener(new ActionListener() {
                @Override public void actionPerformed(ActionEvent e) {
                    newDetail();
                }
            });
            newMenu.add(newDetail);

            newMenu.addSeparator();

            JMenuItem newPattern = new JMenuItem("Pattern...");
            newPattern.addActionListener(new ActionListener() {
                @Override public void actionPerformed(ActionEvent e) {
                    newPattern();
                }
            });
            newMenu.add(newPattern);

            newMenu.addSeparator();

            JMenuItem ambientMessages = new JMenuItem("Ambient Messages");
            newMenu.add(ambientMessages);
        }

        JMenu netMenu = new JMenu(/*"Network"*/);
        netMenu.setIcon(Icons.getObjectIcon("network"));
        netMenu.setToolTipText("Network");
        {
            JMenuItem load = new JMenuItem("Import...");
            netMenu.add(load);
            JMenuItem save = new JMenuItem("Export...");
            netMenu.add(save);

        }

        JMenu viewMenu = new ViewMenu();

        menubar.add(viewMenu);
        menubar.add(newMenu);
        menubar.add(netMenu);
        
        add(menubar, BorderLayout.NORTH);

        content = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

        viewWhat();


        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(contentMargin, contentMargin, contentMargin, contentMargin));
        content.setRightComponent(contentPanel);

        content.setDividerLocation(0.45);

        add(content, BorderLayout.CENTER);

        updateUI();
    }

    public void selectObject(Object o) {
        //System.out.println("selecting: " + o);

        contentPanel.removeAll();

        if (o != null) {
            if (o instanceof Pattern) {
                //content.setRightComponent(new PatternEditPanel(self, (Pattern)o));
                contentPanel.add(new PatternEditPanel(self, (Pattern) o) {
                    @Override protected void deleteThis() {
                        selectObject(null);
                        self.removePattern(pattern);
                        refreshView();
                    }
                }, BorderLayout.CENTER
                );

            } else if (o instanceof Detail) {
                final Detail d = (Detail) o;
                contentPanel.add(new DetailEditPanel(self, d, true) {

                    @Override protected void patternChanged() {
                        refreshView();
                        typeTreePanel.selectObject(d);
                    }

                    @Override
                    protected void deleteThis() {
                        selectObject(null);
                        self.removeDetail(d);
                        refreshView();
                    }
                }, BorderLayout.CENTER);
            } else {
                //content.setRightComponent(new JLabel("Select something."));
                contentPanel.add(new JLabel("Select something."), BorderLayout.CENTER);
            }
        }

        contentPanel.updateUI();
    }

    protected void refreshView() {
        typeTreePanel.refresh();

        //TODO un-hack this
        if (typeTreePanel instanceof WhatTreePanel) {
            final WhatTreePanel w = ((WhatTreePanel)typeTreePanel);
            w.getTree().addTreeSelectionListener(new TreeSelectionListener() {
                @Override public void valueChanged(TreeSelectionEvent e) {
                    DefaultMutableTreeNode selected = (DefaultMutableTreeNode) w.getTree().getSelectionPath().getLastPathComponent();
                    selectObject(selected.getUserObject());
                }
            });
        }
    }

    public void newDetail() {
        NewDetailPanel ndp = new NewDetailPanel(self) {

            @Override protected void afterCreated(Detail d) {
                selectObject(d);
                refreshView();
            }
        };
        SwingWindow sw = new SwingWindow(ndp, 500, 500, false);
        sw.setTitle("New Detail...");
    }

    public void newPattern() {
        String newPatternName = JOptionPane.showInputDialog("New Pattern ID");
        Pattern p = new Pattern(newPatternName);
        self.addPattern(p);
        selectObject(p);
        refreshView();
    }
   

    protected void viewWhat() {        
        typeTreePanel = new WhatTreePanel(self);
        refreshView();

        content.setLeftComponent(new JScrollPane((JPanel)typeTreePanel));
    }

    protected void viewWho() {
        content.setLeftComponent(new JPanel());
    }
    
    protected void viewWhere() {
        content.setLeftComponent(new JPanel());
    }
    
}
