/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.swing;

import automenta.netention.Detail;
import automenta.netention.Mode;
import automenta.netention.Pattern;
import automenta.netention.Property;
import automenta.netention.impl.MemoryDetail;
import automenta.netention.impl.MemorySelf;
import automenta.netention.swing.util.ButtonTabPanel;
import automenta.netention.swing.widget.DetailEditPanel;
import automenta.netention.swing.widget.NewPropertyPanel;
import automenta.netention.swing.widget.PatternEditPanel;
import automenta.netention.swing.widget.SelfBrowserView;
import automenta.netention.swing.widget.WhatTreePanel;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Date;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
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
    private final JTabbedPane contentTabs;
    int contentMargin = 6;
    int maxTabTitleLength = 24;

    public class ViewMenu extends JMenu implements ActionListener {

        private final JRadioButtonMenuItem where;
        private final JRadioButtonMenuItem what;
        private final JRadioButtonMenuItem who;
        private final ButtonGroup group;
        private final JRadioButtonMenuItem when;
        private final JRadioButtonMenuItem recent;
        private final JRadioButtonMenuItem frequent;

        public ViewMenu() {
            super();
            setToolTipText("Views");

            what = new JRadioButtonMenuItem("What", Icons.getIcon("what"));
            what.addActionListener(this);
            who = new JRadioButtonMenuItem("Who", Icons.getIcon("who"));
            who.addActionListener(this);
            where = new JRadioButtonMenuItem("Where", Icons.getIcon("where"));
            where.addActionListener(this);
            
            when = new JRadioButtonMenuItem("When", Icons.getIcon("when"));
            //when.addActionListener(this);
            recent = new JRadioButtonMenuItem("Recent", Icons.getIcon("recent"));
            //when.addActionListener(this);
            frequent = new JRadioButtonMenuItem("Frequent", Icons.getIcon("frequent"));
            //when.addActionListener(this);

            add(what);
            add(who);
            add(where);
            add(when);
            add(recent);
            add(frequent);

            group = new ButtonGroup();
            group.add(what);
            group.add(who);
            group.add(where);
            group.add(when);
            group.add(recent);
            group.add(frequent);

            setIcon(what.getIcon());
            what.setSelected(true);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (what.isSelected()) {
                setIcon(what.getIcon());
                viewWhat();
            } else if (who.isSelected()) {
                setIcon(who.getIcon());
                viewWho();
            } else if (where.isSelected()) {
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
        newMenu.setIcon(Icons.getIcon("add"));
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

            JMenuItem newProperty = new JMenuItem("Property...");
            newProperty.addActionListener(new ActionListener() {

                @Override public void actionPerformed(ActionEvent e) {
                    newProperty();
                }
            });
            newMenu.add(newProperty);

            newMenu.addSeparator();

            JMenuItem ambientMessages = new JMenuItem("Ambient Messages");
            newMenu.add(ambientMessages);
        }

        JMenu netMenu = new JMenu(/*"Network"*/);
        netMenu.setIcon(Icons.getIcon("network"));
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

        //contentPanel = new JPanel(new BorderLayout());
        //contentPanel.setBorder(new EmptyBorder(contentMargin, contentMargin, contentMargin, contentMargin));
        contentTabs = new JTabbedPane();

        content.setRightComponent(contentTabs);

        content.setDividerLocation(0.45);

        add(content, BorderLayout.CENTER);

        updateUI();
    }

    public void addTab(JComponent c, String title) {
        int index = contentTabs.getTabCount();
        if (title.length() > maxTabTitleLength) {
            title = title.substring(0, maxTabTitleLength);
        }
        contentTabs.insertTab(title, null, c, title, index);
        contentTabs.setTabComponentAt(index, new ButtonTabPanel(contentTabs));
        contentTabs.setSelectedIndex(index);
        contentTabs.updateUI();
    }

    public void addTab(Object o) {
        //System.out.println("selecting: " + o);

        JComponent tabContent = null;
        String title = "";

        if (o != null) {
            if (o instanceof Pattern) {
                //content.setRightComponent(new PatternEditPanel(self, (Pattern)o));
                tabContent = new PatternEditPanel(self, (Pattern) o) {

                    @Override protected void deleteThis() {
                        self.removePattern(pattern);
                        contentTabs.removeTabAt(contentTabs.getSelectedIndex());
                        refreshView();
                    }
                };
                title = ((Pattern) o).getID();
            } else if (o instanceof Detail) {
                final Detail d = (Detail) o;
                tabContent = new DetailEditPanel(self, d, true) {

                    @Override protected void patternChanged() {
                        refreshView();
                        typeTreePanel.selectObject(d);
                    }

                    @Override
                    protected void deleteThis() {
                        self.removeDetail(d);
                        contentTabs.removeTabAt(contentTabs.getSelectedIndex());
                        refreshView();
                    }
                };
                title = d.getName();
            } else {
                //content.setRightComponent(new JLabel("Select something."));
                //contentPanel.add(new JLabel("Select something."), BorderLayout.CENTER);
            }
        }
        if (tabContent != null) {
            addTab(tabContent, title);
        }

    }

    protected void refreshView() {
        typeTreePanel.refresh();

        //TODO un-hack this
        if (typeTreePanel instanceof WhatTreePanel) {
            final WhatTreePanel w = ((WhatTreePanel) typeTreePanel);
            w.getTree().addTreeSelectionListener(new TreeSelectionListener() {

                @Override public void valueChanged(TreeSelectionEvent e) {
                    DefaultMutableTreeNode selected = (DefaultMutableTreeNode) w.getTree().getSelectionPath().getLastPathComponent();
                    addTab(selected.getUserObject());
                }
            });
        }
    }

    public void newProperty() {
        NewPropertyPanel ndp = new NewPropertyPanel(self) {

            @Override protected void afterCreated(Property p) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override public void run() {
                        closeThis();
                        refreshView();
                    }
                });

            }

            @Override public void closeThis() {
                int s = contentTabs.getSelectedIndex();
                if (s != -1)
                    contentTabs.removeTabAt(s);
            }
        };

        addTab(ndp, "New Property...");

    }

    public void newDetail() {
//        final NewDetailPanel ndp = new NewDetailPanel(self) {
//
//            @Override protected void afterCreated(final Detail d) {
//                SwingUtilities.invokeLater(new Runnable() {
//                    @Override public void run() {
//                        addTab(d);
//                        refreshView();
//                    }
//                });
//            }
//
//            @Override public void closeThis() {
//                int s = contentTabs.getSelectedIndex();
//                if (s != -1)
//                    contentTabs.removeTabAt(s);
//            }
//        };
        MemoryDetail d = new MemoryDetail("", Mode.Unknown);
        d.setName(new Date().toLocaleString() + " I'm thinking about...");
        self.addDetail(d);
        addTab(d);
        refreshView();
    }

    public void newPattern() {
        String newPatternName = JOptionPane.showInputDialog("New Pattern ID");
        Pattern p = new Pattern(newPatternName);
        self.addPattern(p);
        addTab(p);
        refreshView();
    }

    protected void viewWhat() {
        typeTreePanel = new WhatTreePanel(self);
        refreshView();

        content.setLeftComponent(new JScrollPane((JPanel) typeTreePanel));
    }

    protected void viewWho() {
        content.setLeftComponent(new JPanel());
    }

    protected void viewWhere() {
        content.setLeftComponent(new JPanel());
    }
}
