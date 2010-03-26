/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.swing;

import automenta.netention.Detail;
import automenta.netention.Mode;
import automenta.netention.Pattern;
import automenta.netention.impl.MemoryDetail;
import automenta.netention.impl.MemorySelf;
import automenta.netention.swing.util.SwingWindow;
import automenta.netention.swing.widget.DetailEditPanel;
import automenta.netention.swing.widget.PatternEditPanel;
import automenta.netention.swing.widget.TypeTreePanel;
import automenta.netention.value.BoolProp;
import automenta.netention.value.IntProp;
import automenta.netention.value.NodeProp;
import automenta.netention.value.RealProp;
import automenta.netention.value.StringProp;
import automenta.netention.value.bool.BoolEquals;
import automenta.netention.value.bool.BoolIs;
import automenta.netention.value.integer.IntegerEquals;
import automenta.netention.value.integer.IntegerIs;
import automenta.netention.value.node.NodeEquals;
import automenta.netention.value.node.NodeIs;
import automenta.netention.value.real.RealEquals;
import automenta.netention.value.real.RealIs;
import automenta.netention.value.string.StringEquals;
import automenta.netention.value.string.StringIs;
import java.awt.BorderLayout;
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
        typeTreePanel.tree.addTreeSelectionListener(new TreeSelectionListener() {

            @Override public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode selected = (DefaultMutableTreeNode) typeTreePanel.tree.getSelectionPath().getLastPathComponent();
                selectObject(selected.getUserObject());
            }
        });
        content.setLeftComponent(new JScrollPane(typeTreePanel));

        contentPanel = new JPanel(new BorderLayout());
        content.setRightComponent(contentPanel);

        content.setDividerLocation(0.45);

        add(content, BorderLayout.CENTER);

        updateUI();
    }

    public void selectObject(Object o) {
        contentPanel.removeAll();
        if (o instanceof Pattern) {
            //content.setRightComponent(new PatternEditPanel(self, (Pattern)o));
            contentPanel.add(new JScrollPane(new PatternEditPanel(self, (Pattern) o)), BorderLayout.CENTER);

        } else if (o instanceof Detail) {
            contentPanel.add(new DetailEditPanel(self, (Detail) o, true), BorderLayout.CENTER);
        } else {
            //content.setRightComponent(new JLabel("Select something."));
            contentPanel.add(new JLabel("Select something."), BorderLayout.CENTER);
        }
        contentPanel.updateUI();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
        }

        MemorySelf self = new MemorySelf("me", "Me");
        self.addPattern(new Pattern("Built"));
        self.addPattern(new Pattern("Mobile"));
        {
            self.addProperty(new IntProp("numWheels", "Number of Wheels"));
            self.addProperty(new StringProp("manufacturer", "Manufacturer"));
            self.addProperty(new RealProp("wheelRadius", "Wheel Radius"));
            self.addProperty(new NodeProp("anotherObject", "Another Object", "Built"));
            self.addProperty(new BoolProp("hasKickStand", "Has Kickstand"));
        }

        MemoryDetail d1 = new MemoryDetail("Real Bike", Mode.Real, "Built");
        MemoryDetail d2 = new MemoryDetail("Imaginary Bike", Mode.Imaginary, "Mobile", "Built");
        MemoryDetail d3 = new MemoryDetail("Empty Description", Mode.Real);

        {
            d1.addProperty("numWheels", new IntegerIs(4));
            d1.addProperty("manufacturer", new StringIs("myself"));
            d1.addProperty("wheelRadius", new RealIs(16.0));
            d1.addProperty("hasKickStand", new BoolIs(true));
            d1.addProperty("anotherObject", new NodeIs(d2.getID()));
        }

        {
            d2.addProperty("numWheels", new IntegerEquals(4));
            d2.addProperty("manufacturer", new StringEquals("myself"));
            d2.addProperty("wheelRadius", new RealEquals(16.0));
            d2.addProperty("hasKickStand", new BoolEquals(true));
            d2.addProperty("anotherObject", new NodeEquals(d1.getID()));
        }
        
        self.addDetail(d1);
        self.addDetail(d2);
        self.addDetail(d3);

        new SwingWindow(new SelfPanel(self), 900, 800, true);
    }

}
