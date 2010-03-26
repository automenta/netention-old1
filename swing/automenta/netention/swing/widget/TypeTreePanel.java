/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.swing.widget;

import automenta.netention.Detail;
import automenta.netention.Mode;
import automenta.netention.Pattern;
import automenta.netention.impl.MemorySelf;
import automenta.netention.swing.Icons;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import org.apache.commons.collections15.multimap.MultiHashMap;

/**
 *
 * @author seh
 */
public class TypeTreePanel extends JPanel {

    float textScale = 1.25f;

    private final MemorySelf self;
    public final JTree tree;

    public class TypeTreeModel extends DefaultTreeModel {

        public TypeTreeModel() {
            super(new DefaultMutableTreeNode("All"));
            refresh();
        }

        protected void refresh() {
            ((DefaultMutableTreeNode) root).removeAllChildren();

            MultiHashMap<String, Detail> patterns = new MultiHashMap();
            for (Detail d : self.getDetails().values()) {
                for (String s : d.getPatterns()) {
                    patterns.put(s, d);
                }
            }
            for (String p : patterns.keySet()) {
                Pattern pat = self.getPatterns().get(p);
                DefaultMutableTreeNode pNode = new DefaultMutableTreeNode(pat);
                ((DefaultMutableTreeNode) root).add(pNode);
                for (Detail d : patterns.get(p)) {
                    DefaultMutableTreeNode dNode = new DefaultMutableTreeNode(d);
                    pNode.add(dNode);
                }
            }
        }
    }

    public TypeTreePanel(MemorySelf self) {
        super(new BorderLayout());
        this.self = self;

        TreeModel treeModel = new TypeTreeModel();
        tree = new JTree(treeModel);

        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer() {

            public Component getTreeCellRendererComponent(JTree tree,
                Object value, boolean sel, boolean expanded, boolean leaf,
                int row, boolean hasFocus) {

                super.getTreeCellRendererComponent(tree, value, sel,
                    expanded, leaf, row, hasFocus);

                Object nodeObj = ((DefaultMutableTreeNode) value).getUserObject();
                // check whatever you need to on the node user object
                setIcon(getObjectIcon(nodeObj));


                setBorderSelectionColor(sel ? getBackgroundSelectionColor() : getBackground());
                
                return this;
            }

            public Icon getObjectIcon(Object o) {
                if (o instanceof Detail) {
                    Detail d = (Detail) o;
                    if (d.getMode() == Mode.Imaginary) {
                        return Icons.getIcon("media/tango32/status/dialog-information.png");
                    } else if (d.getMode() == Mode.Real) {
                        return Icons.getIcon("media/tango32/apps/accessories-text-editor.png");
                    } else {
                        return Icons.getIcon("media/tango32/categories/applications-office.png");
                    }

                } else if (o instanceof Pattern) {
                    return Icons.getIcon("media/tango32/categories/applications-system.png");
                }
                return null;
            }
        };
        {
            renderer.setFont(getFont().deriveFont((float)(getFont().getSize() * textScale)));

            final int b = 6;
            renderer.setBorder(new EmptyBorder(b, b, b, b));
        }
        
        tree.setCellRenderer(renderer);

        add(tree, BorderLayout.CENTER);
    }
}
