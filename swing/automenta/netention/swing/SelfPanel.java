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
import automenta.netention.swing.widget.PatternEditPanel;
import automenta.netention.swing.widget.TypeTreePanel;
import java.awt.BorderLayout;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.UIManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Displays a list of one's Details and a tabbed viewer of them 
 */
public class SelfPanel extends JPanel {

    private final JSplitPane content;
    private final TypeTreePanel typeTreePanel;
    private final MemorySelf self;
    private final JPanel contentPanel;

    public SelfPanel(MemorySelf self) {
        super(new BorderLayout());

        this.self = self;

        JMenuBar menubar = new JMenuBar();

        JMenu newMenu = new JMenu("Add");
        {
            JMenuItem newReal = new JMenuItem("Real...");
            newMenu.add(newReal);
            JMenuItem newImaginary = new JMenuItem("Imaginary...");
            newMenu.add(newImaginary);

            newMenu.addSeparator();

            JMenuItem newPattern = new JMenuItem("Pattern...");
            newMenu.add(newPattern);
        }

        JMenu netMenu = new JMenu("Network");
        {
            JMenuItem load = new JMenuItem("Import...");
            netMenu.add(load);
            JMenuItem save = new JMenuItem("Export...");
            netMenu.add(save);
        }

        JMenu viewMenu = new JMenu("View");

        menubar.add(newMenu);
        menubar.add(netMenu);
        menubar.add(viewMenu);
        add(menubar, BorderLayout.NORTH);

        content = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        typeTreePanel = new TypeTreePanel(self);
        refreshTypeTree();

        content.setLeftComponent(new JScrollPane(typeTreePanel));

        contentPanel = new JPanel(new BorderLayout());
        content.setRightComponent(contentPanel);

        content.setDividerLocation(0.45);

        add(content, BorderLayout.CENTER);

        updateUI();
    }

    public void selectObject(Object o) {
        System.out.println("selecting: " + o);

        contentPanel.removeAll();

        if (o != null) {
            if (o instanceof Pattern) {
                //content.setRightComponent(new PatternEditPanel(self, (Pattern)o));
                contentPanel.add(new PatternEditPanel(self, (Pattern) o), BorderLayout.CENTER);

            } else if (o instanceof Detail) {
                final Detail d = (Detail) o;
                contentPanel.add(new DetailEditPanel(self, d, true) {

                    @Override protected void patternChanged() {
                        refreshTypeTree();
                        typeTreePanel.selectObject(d);
                    }

                    @Override
                    protected void deleteThis() {
                        selectObject(null);
                        self.removeDetail(d);
                        refreshTypeTree();
                    }
                }, BorderLayout.CENTER);
            } else {
                //content.setRightComponent(new JLabel("Select something."));
                contentPanel.add(new JLabel("Select something."), BorderLayout.CENTER);
            }
        }

        contentPanel.updateUI();
    }

    protected void refreshTypeTree() {
        typeTreePanel.refresh();
        typeTreePanel.getTree().addTreeSelectionListener(new TreeSelectionListener() {

            @Override public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode selected = (DefaultMutableTreeNode) typeTreePanel.getTree().getSelectionPath().getLastPathComponent();
                selectObject(selected.getUserObject());
            }
        });
    }

    public static void main(String[] args) {
        final Logger logger = Logger.getLogger(SelfPanel.class.getName());

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
        }

        final String filePath = "/tmp/netention1";

        //LOAD
        MemorySelf self;
        try {
            self = MemorySelf.load(filePath);
            logger.log(Level.INFO, "Loaded " + filePath);
        } catch (Exception ex) {
            self = new MemorySelf("me", "Me");
            new SeedSelfBuilder().build(self);
            logger.log(Level.INFO, "Loaded Seed Self");
        }

        final MemorySelf mSelf = self;
        SwingWindow window = new SwingWindow(new SelfPanel(self), 900, 800, true) {

            @Override
            protected void onClosing() {
                //SAVE ON EXIT
                try {
                    mSelf.save(filePath);
                    logger.log(Level.INFO, "Saved " + filePath);
                } catch (Exception ex) {
                    logger.log(Level.SEVERE, null, ex);
                }
            }
        };

    }
}
