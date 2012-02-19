/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.swing.detail.action;

import automenta.netention.Detail;
import automenta.netention.action.DetailAction;
import automenta.netention.html.DetailHTML;
import automenta.netention.swing.util.SwingWindow;
import automenta.netention.swing.widget.email.MessageEditPanel;

/**
 *
 * @author seh
 */
public class SendAction extends DetailAction {
    private final DetailHTML detailHTML;

    public SendAction(DetailHTML h) {
        super("Send");
        this.detailHTML = h;
    }

    
    @Override
    public String getDescription() {
        return "Edit and publish this to one or more targets";
    }
    
    @Override
    public double applies(Detail d) {
        return 1.0;
    }

    @Override
    public Runnable getRun(final Detail detail) {
        return new Runnable() {
            @Override public void run() {
                final String html = "<html>" + detailHTML.getHTML(getSelf(), detail) + "</html>";
                new SwingWindow(new MessageEditPanel(detail.getName(), html), 800, 600);
            }            
        };
    }

   
}
