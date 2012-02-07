/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.swing.property;

import automenta.netention.Detail;
import automenta.netention.Self;
import automenta.netention.Value;
import automenta.netention.swing.util.JHyperLink;
import automenta.netention.value.Comment;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import net.atlanticbb.tantlinger.shef.HTMLEditorPane;

/**
 *
 * @author seh
 */
public class CommentPanel extends PropertyOptionPanel {

    public CommentPanel(Self self, Detail d, final Comment comment, boolean editable) {
        super(self, d, comment, editable);
        
        
        setCurrentOption(new PropertyOption<Comment>(" ") {

            final HTMLEditorPane p = new HTMLEditorPane();
            
            @Override
            public Comment widgetToValue(Comment value) {
                //value.setText(p.getText());
                comment.setText(p.getText());
                return value;
            }

            @Override
            public Comment newDefaultValue() {
                return new Comment();
            }

            @Override
            public JPanel newEditPanel(Comment value) {
                JPanel j = new JPanel(new BorderLayout());
                
                p.setText(value.getText());
                j.add(p, BorderLayout.CENTER);
                
                return j;
            }

            @Override
            public boolean accepts(Value v) {
                return (v instanceof Comment);
            }

            
        });
        refresh();
    }
    
    public boolean showsTypeSelect() {
        return false;
    }
    
    public boolean showsLabel() {
        return true;
    }
    
}