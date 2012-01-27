/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.swing.detail.action;

import automenta.netention.Detail;
import automenta.netention.PropertyValue;
import automenta.netention.Self;
import automenta.netention.impl.MemorySelf;
import automenta.netention.swing.detail.DetailAction;
import automenta.netention.swing.util.SwingWindow;
import automenta.netention.swing.widget.email.MessageEditPanel;

/**
 *
 * @author seh
 */
public class SendAction implements DetailAction {

    @Override
    public String getLabel() {
        return "Send";
    }

    @Override
    public String getDescription() {
        return "Edit and publish this to one or more targets";
    }
    
    @Override
    public boolean applies(Self s, Detail d) {
        return true;
    }

    @Override
    public Runnable getRun(final Self s, final Detail detail) {
        return new Runnable() {
            @Override public void run() {
                final String html = detailToHTML(detail);
                new SwingWindow(new MessageEditPanel(detail.getName(), html), 800, 600);
            }            
        };
    }

   
    public static String detailToHTML(Detail d) {
        StringBuffer x = new StringBuffer("<html>");
        x.append("<h1>" + d.getName() + "</h1><br/>");
        x.append("<b>" + d.getPatterns() + "</b><br/>");
        for (PropertyValue pv : d.getValues()) {
            x.append(pv.toString() + "<br/>");
        }
        x.append("<br/><pre>" + MemorySelf.toJSON(d) + "</pre>");
        x.append("</html>");
        return x.toString();
    }
}
