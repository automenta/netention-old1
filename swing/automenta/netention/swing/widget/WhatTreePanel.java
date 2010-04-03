/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.swing.widget;

import automenta.netention.Detail;
import automenta.netention.Pattern;
import automenta.netention.impl.MemorySelf;
import automenta.netention.swing.Icons;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import org.apache.commons.collections15.multimap.MultiHashMap;

/**
 *
 * @author seh
 */
public class WhatTreePanel extends JPanel implements SelfBrowserView {

    float textScale = 1.25f;
    private final MemorySelf self;
    private JTree tree;
    private TypeTreeModel treeModel;

    public class TypeTreeModel extends DefaultTreeModel {

        public TypeTreeModel() {
            super(new DefaultMutableTreeNode("All"));
            refresh();
        }

        protected void refresh() {
            ((DefaultMutableTreeNode) root).removeAllChildren();


            MultiHashMap<String, Detail> patterns = new MultiHashMap();
            for (String p : self.getPatterns().keySet()) {
                patterns.put(p, null);
            }

            for (Detail d : self.getDetails().values()) {
                if (d.getPatterns().size() > 0) {
                    for (String s : d.getPatterns()) {
                        patterns.put(s, d);
                    }
                } else {
                    patterns.put("Other", d);
                }
            }

            for (String p : patterns.keySet()) {
                Pattern pat = self.getPatterns().get(p);
                DefaultMutableTreeNode pNode;
                if (pat != null) {
                    pNode = new DefaultMutableTreeNode(pat);
                } else {
                    pNode = new DefaultMutableTreeNode(p);
                }
                ((DefaultMutableTreeNode) root).add(pNode);
                for (Detail d : patterns.get(p)) {
                    if (d != null) {
                        DefaultMutableTreeNode dNode = new DefaultMutableTreeNode(d);
                        pNode.add(dNode);
                    }
                }
            }
        }
    }

    public WhatTreePanel(MemorySelf self) {
        super(new BorderLayout());


        this.self = self;

    }

    public void refresh() {
        removeAll();

        treeModel = new TypeTreeModel();
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

                if (!leaf) {
                    if (expanded) {
                        setFont(getFont().deriveFont(Font.PLAIN));
                    } else {
                        setFont(getFont().deriveFont(Font.BOLD));
                    }
                } else {
                    setFont(getFont().deriveFont(Font.PLAIN));
                }


                setBorderSelectionColor(
                    sel ? getBackgroundSelectionColor() : getBackground());



                return this;


            }

            public Icon getObjectIcon(Object o) {
                if (o instanceof Detail) {
                    Detail d = (Detail) o;
                    return Icons.getObjectIcon(d.getPatterns());
//                    if (d.getMode() == Mode.Imaginary) {
//                        return Icons.getIcon("media/tango32/status/dialog-information.png");
//
//
//                    } else if (d.getMode() == Mode.Real) {
//                        return Icons.getIcon("media/tango32/apps/accessories-text-editor.png");
//                    } else {
//                        return Icons.getIcon("media/tango32/categories/applications-office.png");
//                    }

                } else if (o instanceof Pattern) {
                    Pattern p = (Pattern) o;
                    return Icons.getObjectIcon(p.getID());
                }
                return null;
            }
        };


        {
            renderer.setFont(getFont().deriveFont((float) (getFont().getSize() * textScale)));

            final int b = 6;
            renderer.setBorder(new EmptyBorder(b, b, b, b));


        }


        tree.setCellRenderer(renderer);
        tree.setRootVisible(false);

        add(tree, BorderLayout.CENTER);
    }

    public JTree getTree() {
        return tree;
    }

    /** expands to the first path containing 'd' as a leaf */
    public void selectObject(Detail d) {
        //TODO impl this
    }
}
