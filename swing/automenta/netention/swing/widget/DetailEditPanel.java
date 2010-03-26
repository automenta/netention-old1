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
import automenta.netention.swing.property.RealPropertyPanel;
import automenta.netention.swing.property.StringPropertyPanel;
import automenta.netention.swing.util.JScaledLabel;
import automenta.netention.value.BoolProp;
import automenta.netention.value.IntProp;
import automenta.netention.value.RealProp;
import automenta.netention.value.StringProp;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

/**
 * Displays a detail and its links for view/edit
 *      Set Mode (real, imaginary)
 *      Add/Remove Patterns
 *      Add new Properties
 *      Remove existing Properties
 * @author seh
 */
public class DetailEditPanel extends JPanel {

    private final JPanel sentences;
    private Detail detail;
    private final Self self;
    private boolean editable;
    private final JSplitPane contentSplit;
    private final DetailLinksPanel linksPanel;
    private final DetailMenuBar menuBar;

    protected class DetailMenuBar extends JMenuBar {

        public DetailMenuBar() {
            super();
        }


        protected void refresh() {
            removeAll();
            for (String pid : detail.getPatterns()) {
                Pattern p = self.getPatterns().get(pid);
                JMenu j = new JMenu(p.getID());
                //TODO add props
                add(j);
            }
        }
    }

    public DetailEditPanel(Self s, Detail d, boolean editable) {
        super(new GridBagLayout());

        this.self = s;

        setEditable(editable);

        GridBagConstraints gc = new GridBagConstraints();
        gc.weightx = 1.0;
        gc.weighty = 0.0;
        gc.fill = gc.HORIZONTAL;
        gc.anchor = gc.NORTHWEST;
        gc.gridx = gc.gridy = 1;

        menuBar = new DetailMenuBar();
        
        JPanel header = new JPanel(new BorderLayout());
        header.add(new JScaledLabel(d.getName(), 2.5f), BorderLayout.NORTH);
        header.add(menuBar, BorderLayout.SOUTH);
        add(header, gc);

        
        gc.gridy++;
        gc.weighty = 1.0;
        gc.fill = gc.BOTH;

        contentSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        add(contentSplit, gc);

        sentences = new JPanel(new GridBagLayout());
        sentences.setBackground(Color.WHITE);
        contentSplit.setTopComponent(new JScrollPane(sentences));

        linksPanel = new DetailLinksPanel(self, d);
        contentSplit.setBottomComponent(new JScrollPane(linksPanel));

        contentSplit.setDividerLocation(0.75);

        setDetail(d);
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


        for (PropertyValue pv : detail.getProperties()) {
            gc.gridy++;
            JComponent nextLine = getLinePanel(pv);
            final Color alternateColor = new Color(0.95f, 0.95f, 0.95f);
            
            nextLine.setOpaque(true);
            nextLine.setBackground(gc.gridy % 2 == 0 ? Color.WHITE : alternateColor);

            sentences.add(nextLine, gc);
            //gc.gridx+=1;
        }

        gc.gridy++;
        gc.fill = gc.VERTICAL;
        gc.weighty = 1.0;
        sentences.add(Box.createVerticalBox(), gc);

        sentences.updateUI();

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
}
