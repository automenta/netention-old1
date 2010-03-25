/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.swing.widget;

import automenta.netention.Detail;
import automenta.netention.Pattern;
import automenta.netention.impl.MemorySelf;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import org.apache.commons.collections15.multimap.MultiHashMap;

/**
 *
 * @author seh
 */
public class TypeTreePanel extends JPanel {
    private final MemorySelf self;
    public final JTree tree;

    public class TypeTreeModel extends DefaultTreeModel {

        public TypeTreeModel() {
            super(new DefaultMutableTreeNode("All"));
            refresh();
        }

        protected void refresh() {
            ((DefaultMutableTreeNode)root).removeAllChildren();

            MultiHashMap<String, Detail> patterns = new MultiHashMap();
            for (Detail d : self.getDetails().values()) {
                for (String s : d.getPatterns()) {
                    patterns.put(s, d);
                }
            }
            for (String p : patterns.keySet()) {
                Pattern pat = self.getPatterns().get(p);
                DefaultMutableTreeNode pNode = new DefaultMutableTreeNode(pat);
                ((DefaultMutableTreeNode)root).add(pNode);
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
        add(tree, BorderLayout.CENTER);
    }

}
