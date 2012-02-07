/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.swing.detail.action;

import automenta.netention.Detail;
import automenta.netention.Self;
import automenta.netention.html.DetailHTML;
import automenta.netention.swing.detail.DetailAction;
import automenta.netention.swing.util.SwingWindow;
import automenta.netention.swing.widget.email.MessageEditPanel;

/**
 *
 * @author seh
 */
public class SendAction implements DetailAction {
    private final DetailHTML detailHTML;

    public SendAction(DetailHTML h) {
        super();
        this.detailHTML = h;
    }

    
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
                final String html = "<html>" + detailHTML.getHTML(s, detail) + "</html>";
                new SwingWindow(new MessageEditPanel(detail.getName(), html), 800, 600);
            }            
        };
    }

   
}
