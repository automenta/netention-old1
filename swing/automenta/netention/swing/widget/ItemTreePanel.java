/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.swing.widget;

import automenta.netention.Detail;
import automenta.netention.Node;
import automenta.netention.Pattern;
import automenta.netention.Self;
import automenta.netention.swing.Icons;
import automenta.netention.swing.util.MLTreeSelectionModel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.*;
import org.apache.commons.collections15.IteratorUtils;
import org.apache.commons.collections15.multimap.MultiHashMap;

/**
 *
 * @author seh
 */
abstract public class ItemTreePanel extends JPanel implements IndexView {

    float textScale = 1.25f;
    private final Self self;
    private JTree tree;
    private TreeModel treeModel;
    private boolean sticky;

    public static class WhenTreeModel extends DefaultTreeModel {
        private final Self self;

        public WhenTreeModel(Self self) {
            super(new DefaultMutableTreeNode("All"));
            this.self = self;
            refresh();
        }

        protected void refresh() {
            ((DefaultMutableTreeNode) root).removeAllChildren();


            List<Node> d = Self.getDetailsByTime(self.iterateNodes(), false);

            for (Node n : d) {
                if (shows(n)) {
                    DefaultMutableTreeNode pNode = new DefaultMutableTreeNode(n);
                    ((DefaultMutableTreeNode) root).add(pNode);
                }
            }
        }
        
        public boolean shows(Node n) {
            return true;
        }
    }
    
    public static class TypeTreeModel extends DefaultTreeModel {
        private final Self self;
        private MultiHashMap<String, Detail> patterns;
        private final boolean includeInstances;

        public TypeTreeModel(Self self) {
            this(self, true);
        }
        
        public TypeTreeModel(Self self, boolean includeInstances) {
            super(new DefaultMutableTreeNode("All"));
            this.self = self;
            this.includeInstances = includeInstances;
            refresh();
        }
        
        

        public void buildPatternMenu(final DefaultMutableTreeNode t, final String pid) {
            final Pattern p = self.getPattern(pid);

            
            DefaultMutableTreeNode ss = new DefaultMutableTreeNode(self.getPattern(pid));            
            t.add(ss);
            
            if (includeInstances) {
                Collection<Detail> dp = patterns.get(pid);
                if (dp!=null)
                    for (Detail d : dp) {
                        if (d != null) {
                            DefaultMutableTreeNode dNode = new DefaultMutableTreeNode(d);
                            ss.add(dNode);
                        }
                    }
            }

            Collection<String> children = self.getSubPatterns(pid);
            if (children.size() > 0) {
                for (String s : children) {
                    buildPatternMenu(ss, s);
                }
            }

        }

        
        protected void refresh() {
            ((DefaultMutableTreeNode) root).removeAllChildren();

            patterns = new MultiHashMap();
            for (String p : self.getPatterns()) {
                patterns.put(p, null);
            }

            for (Node n : IteratorUtils.toList(self.iterateNodes())) {
                if (n instanceof Detail) {
                    Detail d = (Detail)n;
                    if (d.getPatterns().size() > 0) {
                        for (String s : d.getPatterns()) {
                            patterns.put(s, d);
                        }
                    } else {
                        patterns.put("Thought", d);
                    }
                }
            }
            
            List<String> roots = new LinkedList();
            for (String sp : self.getPatterns()) {
                Pattern p = self.getPattern(sp);
                if (p.getParents().isEmpty()) {
                    roots.add(p.getID());
                }
            }

            for (String pid : roots) {
                buildPatternMenu((DefaultMutableTreeNode)root, pid);
            }
            

        }
    }

//    class ActionJList extends MouseAdapter {
//
//        protected JList list;
//
//        public ActionJList(JList l) {
//            list = l;
//        }
//
//        public void mouseClicked(MouseEvent e) {
//            if (e.getClickCount() == 2) {
//                int index = list.locationToIndex(e.getPoint());
//                ListModel dlm = list.getModel();
//                Object item = dlm.getElementAt(index);;
//                list.ensureIndexIsVisible(index);
//                System.out.println("Double clicked on " + item);
//            }
//        }
//    }

    class ActionJTree extends MouseAdapter {

        protected JTree list;

        public ActionJTree(JTree l) {
            list = l;
        }

        public void mouseClicked(MouseEvent e) {
            if ((e.getClickCount() == 2) && (e.getButton() == MouseEvent.BUTTON1)) {
                TreePath path = tree.getClosestPathForLocation(e.getPoint().x, e.getPoint().y);
                DefaultMutableTreeNode item = (DefaultMutableTreeNode) path.getLastPathComponent();                
                onDoubleLeftClick(item.getUserObject());
            }
            else if (e.getButton() == MouseEvent.BUTTON3) {
                onRightClick(e);
            }
        }
    }

    abstract public void onDoubleLeftClick(Object item);

    /**
     * note: may need refresh() since it is not called in constructor
     * @param self 
     */
    public ItemTreePanel(Self self) {
        super(new BorderLayout());


        this.self = self;

    }

    public void setSticky(boolean sticky) {
        this.sticky = sticky;
    }

    public boolean isSticky() {
        return sticky;
    }
    
    abstract public TreeModel getModel();
    
    public void refresh() {
        removeAll();

        treeModel = getModel();
        if (treeModel instanceof WhenTreeModel) {
            ((WhenTreeModel)treeModel).refresh();
        }
        else if (treeModel instanceof TypeTreeModel) {
            ((TypeTreeModel)treeModel).refresh();
        }
        
        if (isSticky()) {
            tree = new JTree(treeModel) { 

                //@see MLTreeSelectionModel

                @Override
                public void setSelectionPath(TreePath path) {

                        //System.out.println("MLDebugJTree: setSelectionPath(" + path + ")");

                        if (!isPathSelected(path)) {
                            addSelectionPath(path);
                        }
                        else {
                            removeSelectionPath(path);
                        }

                        return;
                        //super.setSelectionPath(path);
                }

                @Override
                public void setSelectionPaths(TreePath[] paths) {

                        //System.out.println("MLDebugJTree: setSelectionPaths(" + paths + ")");

                        addSelectionPaths(paths);

                        return;
                }

                @Override
                public void setSelectionRow(int row) {

                        //System.out.println("MLDebugJTree: setSelectionRow(" + row + ")");

                        addSelectionRow(row);


                        return;
                        //super.setSelectionRow(row);
                }

                @Override
                public void setSelectionRows(int[] rows) {

                        //System.out.println("MLDebugJTree: setSelectionRows(" + rows + ")");

                        addSelectionRows(rows);

                        return;
                        //super.setSelectionRows(rows);
                }

            };
            
            tree.setSelectionModel(new MLTreeSelectionModel());
        }
        else {
            tree = new JTree(treeModel);
        }

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
                    //setText("<html><b>" + d.getName() + "</b><br/>" + d.getWhen() + "</html>");
                    setText(d.getName());

                }

                // check whatever you need to on the node user object
                setIcon(Icons.getObjectIcon(self, nodeObj));

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


                this.updateUI();

                return this;


            }

        };


        {
            renderer.setFont(getFont().deriveFont((float) (getFont().getSize() * textScale)));

            final int b = 6;
            renderer.setBorder(new EmptyBorder(b, b, b, b));


        }

        tree.setCellRenderer(renderer);
        tree.setRootVisible(false);
        tree.setToggleClickCount(1);
        tree.setShowsRootHandles(true);

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
    
    public List<Object> getSelectedObjects() {
        List<Object> l = new LinkedList();
        for (TreePath t : tree.getSelectionPaths()) {
            l.add(((DefaultMutableTreeNode)t.getLastPathComponent()).getUserObject());
        }
        return l;
    }
    
    public List<Pattern> getSelectedPatterns() {
        List<Pattern> p = new LinkedList();
        
        if (tree!=null) {
            TreePath[] paths = tree.getSelectionPaths();
            if (paths!=null) {
                for (TreePath t : paths) {
                    Object o = ((DefaultMutableTreeNode)t.getLastPathComponent()).getUserObject();
                    if (o instanceof Pattern)
                        p.add((Pattern)o);
                }
            }
        }
        
        return p;
    }
    public List<Detail> getSelectedDetails() {
        List<Detail> p = new LinkedList();
        
        if (tree!=null) {
            TreePath[] paths = tree.getSelectionPaths();
            if (paths!=null) {
                for (TreePath t : paths) {
                    Object o = ((DefaultMutableTreeNode)t.getLastPathComponent()).getUserObject();
                    if (o instanceof Detail)
                        p.add((Detail)o);
                }
            }
        }
        
        return p;
    }

    public void onRightClick(MouseEvent e) {
        
    }
}
