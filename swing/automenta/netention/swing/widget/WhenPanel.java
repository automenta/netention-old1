/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.swing.widget;

import automenta.netention.Detail;
import automenta.netention.Node;
import automenta.netention.Pattern;
import automenta.netention.impl.MemorySelf;
import automenta.netention.swing.Icons;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.Icon;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.ListModel;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

/**
 *
 * @author seh
 */
abstract public class WhenPanel extends JPanel implements IndexView {

    float textScale = 1.25f;
    private final MemorySelf self;
    private JTree tree;
    private WhenTreeModel treeModel;

    public class WhenTreeModel extends DefaultTreeModel {

        public WhenTreeModel() {
            super(new DefaultMutableTreeNode("All"));
            refresh();
        }

        protected void refresh() {
            ((DefaultMutableTreeNode) root).removeAllChildren();


            List<Node> d = MemorySelf.getDetailsByTime(self.iterateNodes(), false);

            for (Node n : d) {
                DefaultMutableTreeNode pNode = new DefaultMutableTreeNode(n);
                ((DefaultMutableTreeNode) root).add(pNode);

            }
        }
    }

    class ActionJList extends MouseAdapter {

        protected JList list;

        public ActionJList(JList l) {
            list = l;
        }

        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                int index = list.locationToIndex(e.getPoint());
                ListModel dlm = list.getModel();
                Object item = dlm.getElementAt(index);;
                list.ensureIndexIsVisible(index);
                System.out.println("Double clicked on " + item);
            }
        }
    }

    class ActionJTree extends MouseAdapter {

        protected JTree list;

        public ActionJTree(JTree l) {
            list = l;
        }

        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                TreePath path = tree.getClosestPathForLocation(e.getPoint().x, e.getPoint().y);
                DefaultMutableTreeNode item = (DefaultMutableTreeNode) path.getLastPathComponent();                
                onOpened(item.getUserObject());
            }
        }
    }

    abstract public void onOpened(Object item);

    public WhenPanel(MemorySelf self) {
        super(new BorderLayout());


        this.self = self;

    }

    public void refresh() {
        removeAll();

        treeModel = new WhenTreeModel();
        tree = new JTree(treeModel);

        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer() {

            public Component getTreeCellRendererComponent(JTree tree,
                    Object value, boolean sel, boolean expanded, boolean leaf,
                    int row, boolean hasFocus) {

                super.getTreeCellRendererComponent(tree, value, sel,
                        expanded, leaf, row, hasFocus);

                Object nodeObj = ((DefaultMutableTreeNode) value).getUserObject();

                if (nodeObj instanceof Pattern) {
                    Pattern p = (Pattern) nodeObj;
                    setText(p.getName());
                    setToolTipText(p.getID());
                } else if (nodeObj instanceof Detail) {
                    Detail d = (Detail) nodeObj;
                    setText("<html><b>" + d.getName() + "</b><br/>" + d.getWhen() + "</html>");

                }

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
                    return Icons.getDetailIcon(self, d);
                } else if (o instanceof Pattern) {
                    Pattern p = (Pattern) o;
                    return Icons.getPatternIcon(p);
                }
                return Icons.getIcon("thought");
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
        tree.addMouseListener(new ActionJTree(tree));

        updateUI();
    }

    public JTree getTree() {
        return tree;
    }

    /**
     * expands to the first path containing 'd' as a leaf
     */
    public void selectObject(Detail d) {
        //TODO impl this
    }
}
