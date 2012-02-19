package automenta.netention.swing.property;

import automenta.netention.*;
import automenta.netention.swing.util.TransparentFlowPanel;
import automenta.netention.value.node.NodeEquals;
import automenta.netention.value.node.NodeIs;
import automenta.netention.value.node.NodeProp;
import java.util.Iterator;
import java.util.Set;
import javax.swing.JComboBox;
import javax.swing.JPanel;


public class NodePropertyPanel extends PropertyOptionPanel {

    public NodePropertyPanel(Self s, Detail d, PropertyValue v, boolean editable) {
        super(s, d, v, editable);
        
        if (getMode() != Mode.Imaginary) {

            addOption(new PropertyOption<NodeIs>("is") {

                JComboBox rta = getComboBox();

                @Override public boolean accepts(Value v) {
                    return v.getClass().equals(NodeIs.class);
                }

                @Override public NodeIs newDefaultValue() {
                    return new NodeIs();
                }

                @Override public NodeIs widgetToValue(NodeIs r) {
                    Object x = rta.getSelectedItem();
                    if (x!=null) {
                        if (x instanceof Detail)
                            r.setValue(  ((Detail)x).getID() );
                    }
                    return r;
                }

                @Override public JPanel newEditPanel(NodeIs value) {
                    setValue(value);
                    setReal();

                    JPanel p = new TransparentFlowPanel();
                    rta.setSelectedItem(getSelf().getDetail(value.getNode()));
                    p.add(rta);


                    return p;
                }
            });

        } 
        if (getMode() != Mode.Real) {
            addOption(new PropertyOption<NodeEquals>("exactly") {

                JComboBox rta = getComboBox();

                @Override public boolean accepts(Value v) {
                    return v.getClass().equals(NodeEquals.class);
                }

                @Override public NodeEquals widgetToValue(NodeEquals r) {
                    Object x = rta.getSelectedItem();
                    if (x!=null) {
                        if (x instanceof Detail)
                            r.setValue(  ((Detail)x).getID() );
                    }
                    return r;
                }

                @Override public NodeEquals newDefaultValue() {
                    return new NodeEquals();
                }

                @Override public JPanel newEditPanel(NodeEquals value) {
                    setValue(value);
                    setImaginary();

                    JPanel p = new TransparentFlowPanel();
                    rta.setSelectedItem(getSelf().getDetail(value.getNode()));
                    p.add(rta);

                    
                    return p;
                }
            });

        }

        refresh();

    }

    public JComboBox getComboBox() {
        JComboBox jb = new JComboBox();
        jb.setEditable(isEditable());
        
        final NodeProp p = (NodeProp)getProperty();
        final Set<String> ranges = p.getRanges();
        
        Iterator<Node> d = getSelf().iterateNodes();
        while (d.hasNext()) {
            Node n = d.next();
            if (n instanceof Detail) {
                Detail dd = (Detail)n;
                
                if (ranges.isEmpty())
                    jb.addItem(dd);
                else {
                    boolean added = false;
                    for (String r : ranges) {
                        for (String dp : dd.getPatterns()) {
                            if (r.equals(dp) || getSelf().isSuperPattern(r, dp)) {
                                jb.addItem(dd);
                                added = true;
                                break;
                            }
                        }
                        if (added)
                            break;
                    }
                }
                
            }
        }
        
        if (jb.getItemCount() == 0) {
            jb.setEnabled(false);
            jb.setToolTipText("No valid choices available.");
        }
        
        return jb;
    }
    
}
