/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.swing;

import automenta.netention.*;
import automenta.netention.Self.SelfListener;
import automenta.netention.html.BasicDetailHTML;
import automenta.netention.html.DetailHTML;
import automenta.netention.survive.Environment;
import automenta.netention.swing.detail.DetailEditPanel;
import automenta.netention.swing.map.Map2DPanel;
import automenta.netention.swing.util.ButtonTabPanel;
import automenta.netention.swing.util.SwingWindow;
import automenta.netention.swing.widget.NowPanel.NotificationsPanel;
import automenta.netention.swing.widget.*;
import automenta.netention.swing.widget.email.MessageEditPanel;
import automenta.netention.swing.widget.survive.MapControlPanel;
import automenta.netention.value.string.StringIs;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.tree.TreeModel;
import org.openstreetmap.gui.jmapviewer.Coordinate;

/**
 * Displays a list of one's Details and a tabbed viewer of them 
 */
public class SelfBrowserPanel extends JPanel implements SelfListener {
    final Environment environment;
    

    private final JSplitPane content;
    private IndexView indexView;
    private final Self self;
    private final JTabbedPane contentTabs;
    int contentMargin = 6;
    int maxTabTitleLength = 24;
    
    static
    {
    // do not remove this line, necessary for Swing integration !
    JPopupMenu.setDefaultLightWeightPopupEnabled( false );
    }
    private final SelfSession config;

    @Override
    public void onDetailsAdded(Detail... d) {
        refreshView();
    }

    @Override
    public void onDetailsRemoved(Detail... d) {
        refreshView();
    }

    public class ViewMenu extends JPanel implements ActionListener {

        private final JToggleButton where;
        private final JToggleButton what;
        private final JToggleButton who;
        private final JToggleButton when;
        private final JToggleButton recent;
        private final JToggleButton frequent;
        private final JToggleButton survivalMap;
        private final JToggleButton now;
        private final ButtonGroup bg;

        public ViewMenu() {
            super(new FlowLayout(FlowLayout.LEFT, 0, 0));
            setToolTipText("Views");

            bg = new ButtonGroup() {
                @Override
                public void setSelected(ButtonModel model, boolean selected) {
                    if (selected) {
                        super.setSelected(model, selected);
                    } else {
                        clearSelection();
                    }
                }                
            };
            
            bg.add(now = new JToggleButton(Icons.getIcon("home")));
            now.setToolTipText("Now");
            now.addActionListener(this);

            bg.add(survivalMap = new JToggleButton(Icons.getIcon("map")));
            survivalMap.setToolTipText("Survival Map");
            survivalMap.addActionListener(this);
            
            bg.add(what = new JToggleButton(Icons.getIcon("what")));
            what.setToolTipText("What");
            what.addActionListener(this);
            
            
            bg.add(who = new JToggleButton(Icons.getIcon("who")));
            who.setToolTipText("Who");
            who.addActionListener(this);

            bg.add(where = new JToggleButton(Icons.getIcon("where")));
            where.setToolTipText("Where");
            where.addActionListener(this);
            
            bg.add(when = new JToggleButton(Icons.getIcon("when")));
            when.setToolTipText("When");
            when.addActionListener(this);
            
            bg.add(recent = new JToggleButton(Icons.getIcon("recent")));
            recent.setToolTipText("Recent");;
            recent.addActionListener(this);
            
            bg.add(frequent = new JToggleButton(Icons.getIcon("frequent")));
            frequent.setToolTipText("Frequent");
            frequent.addActionListener(this);

            add(now);
            add(when);
            add(what);
            add(survivalMap);
            add(who);
            add(where);
            add(recent);
            add(frequent);

            
            bg.setSelected(when.getModel(), true);
        }

        @Override
        public Component add(Component comp) {
            if (comp instanceof AbstractButton) {
                ((AbstractButton)comp).setBorderPainted(false);
            }
            return super.add(comp);
        }
        
        

        @Override
        public void actionPerformed(ActionEvent e) {
            
            if (now.isSelected()) {
                viewNow();
            } else if (what.isSelected()) {
                viewWhat();
            } else if (who.isSelected()) {
                viewWho();
            } else if (where.isSelected()) {
                viewWhere();
            } else if (when.isSelected()) {
                viewWhen();
            } else if (recent.isSelected()) {
                
            } else if (frequent.isSelected()) {
                //viewFrequent();
            } else if (survivalMap.isSelected()) {
                viewSurvival();
            }

        }
        //        JMenu viewMenu = new JMenu(/*"View"*/);
//        viewMenu.setIcon(Icons.getObjectIcon("view"));
//        viewMenu.setToolTipText("Views");
    }

    public SelfBrowserPanel(final Self self, final SelfSession config, final Environment e) {
        super(new BorderLayout());

        this.self = self;
        this.config = config;
        this.environment = e;
        
        self.addListener(this);

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

            JMenuItem newText = new JMenuItem("Text");
            newText.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, java.awt.event.InputEvent.CTRL_MASK));
            newText.addActionListener(new ActionListener() {

                @Override public void actionPerformed(ActionEvent e) {
                    newText();
                }
            });
            newMenu.add(newText);
            
            
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

            JMenuItem load = new JMenuItem("Load/Save JSON");
            load.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    SwingWindow w = new SwingWindow(new JSONIOPanel(self), 500, 400);
                }
                
            });
            
            newMenu.add(load);            
        }

//        JMenu netMenu = new JMenu(/*"Network"*/);
//        netMenu.setIcon(Icons.getIcon("network"));
//        netMenu.setToolTipText("Network");
//        {
//
//        }

        JPanel viewMenu = new ViewMenu();

        menubar.add(newMenu);

        viewMenu.add(menubar, 0);
        
        add(viewMenu, BorderLayout.NORTH);

        content = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

        viewWhen();

        //contentPanel = new JPanel(new BorderLayout());
        //contentPanel.setBorder(new EmptyBorder(contentMargin, contentMargin, contentMargin, contentMargin));
        contentTabs = new JTabbedPane();

        content.setRightComponent(contentTabs);

        content.setDividerLocation(0.45);

        add(content, BorderLayout.CENTER);
        
        updateUI();
    }

    public void removeTabs() {
        contentTabs.removeAll();
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
                        indexView.selectObject(d);
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
        indexView.refresh();

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
    
    public void newText() {
        ReadTextDocumentPanel rd = new ReadTextDocumentPanel();
        rd.setSelf(self);

        rd.add(new ReadTextDocumentPanel.Sentenceize("Sentence-ize"));
        rd.add(new ReadTextDocumentPanel.Paragraphize("Paragraph-ize"));
        rd.add(new ReadTextDocumentPanel.TwitterSearch("Twitter Search"));
        rd.add(new ReadTextDocumentPanel.RSSItemize("RSS Item-ize"));
        
        new SwingWindow(rd, 800, 600, false);
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
        Detail d = new Detail("", Mode.Unknown);
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
        viewTree(new ItemTreePanel.TypeTreeModel(self));
    }
    protected void viewWhen() {
        viewTree(new ItemTreePanel.WhenTreeModel(self));
    }

    protected void viewTree(final TreeModel model) {
        indexView = new ItemTreePanel(self) {

            @Override
            public TreeModel getModel() {
                return model;
            }
            
            
            @Override public void onDoubleLeftClick(Object item) {
                addTab(item);
            }

            @Override
            public void onRightClick(final MouseEvent e) {
                super.onRightClick(e);
                
                JPopupMenu j = new JPopupMenu();
                
                final List<Detail> details = getSelectedDetails();
                if (details.size() > 0) {
                    JMenuItem a = new JMenuItem("Delete " + details.size() + " details...");
                    a.addActionListener(new ActionListener() {
                        @Override public void actionPerformed(ActionEvent xe) {
                            int n = JOptionPane.showConfirmDialog(
                                        SelfBrowserPanel.this,
                                        "Delete " + details.size() + " details?",
                                        "Delete Details",
                                        JOptionPane.YES_NO_OPTION);
                            if (n == JOptionPane.YES_OPTION) {
                                SwingUtilities.invokeLater(new Runnable() {
                                    @Override public void run() {
                                        deleteDetails(details);
                                        refreshView();
                                    }                                    
                                });
                            }

                        }                        
                    });
                    
                    JMenuItem b = new JMenuItem("Synthesize " + details.size() + " details...");
                    b.addActionListener(new ActionListener() {
                        final DetailHTML detailHTML = new BasicDetailHTML();
                        
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            StringBuilder html = new StringBuilder();
                            StringBuilder names = new StringBuilder();
                            for (Detail d : details) {
                                names.append(d.getName() + " ");
                                final String h = "<html>" + detailHTML.getHTML(self, d, false) + "</html>";
                                html.append(h);                                
                            }
                            
                            new SwingWindow(new MessageEditPanel(self, names.toString().trim(), html.toString()), 800, 600);
                            
                        }                        
                    });

                    
                    j.add(b);
                    j.addSeparator();
                    j.add(a);
                }
                

                
                j.show(e.getComponent(), e.getX(), e.getY());
                
            }
            
            

        };
        refreshView();

        content.setLeftComponent(new JScrollPane((JPanel) indexView));
        content.setRightComponent(contentTabs);
        updateUI();        
    }
    
    protected void viewNow() {
        content.setLeftComponent(new NotificationsPanel(self));
        content.setRightComponent(new NowPanel(self, config) {
            @Override
            public void addDetail(Coordinate h) {
                String name = "At: " + h.getLat() + ", " + h.getLon();
                Detail d = new Detail(name, Mode.Real, "Located");
                d.add("currentLocation", new StringIs(h.getLat() + ", " + h.getLon()));
                self.addDetail(d);
                addTab(d);
                viewWhen();
            }            
        });
        updateUI();
    }
    
//    /** TODO the code in this method is not actually frequent, but the JOGL Graph view */
//    protected void viewFrequent() {
//        indexView = new GraphPanel(self);
//        refreshView();
//
//        content.setLeftComponent((JPanel)indexView);
//        updateUI();
//    }

    protected void viewWho() {
        content.setLeftComponent(new JPanel());
        updateUI();
    }

    protected void viewWhere() {
        content.setLeftComponent(new JPanel());
        updateUI();
    }
    
    protected void viewSurvival() {
        Map2DPanel map = new Map2DPanel();
        
        content.setRightComponent(new JScrollPane(map));
        
        content.setLeftComponent(new MapControlPanel(self, map, environment));
        updateUI();
    }
    
    protected void deleteDetails(Collection<Detail> d) {
        self.removeDetail(d.toArray(new Detail[d.size()]));
    }
}
