/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.app;

import automenta.netention.Self;
import automenta.netention.Session;
import automenta.netention.swing.CommunityBrowserPanel;
import automenta.netention.swing.SelfSession;
import automenta.netention.swing.util.SwingWindow;
import javax.swing.JPanel;

/**
 *
 * @author seh
 */
public class RunCommunityBrowser {

    public JPanel newPanel() throws Exception {
        Self self = RunSelfBrowser.newDefaultSelf();
        SelfSession sc = new SelfSession();

        JPanel j = new CommunityBrowserPanel(self, sc);
        return j;
    }

    public static void main(String[] args) throws Exception  {

        Session.init();
        
//        SwingUtilities.invokeLater(new Runnable() {
//
//            @Override
//            public void run() {
                SwingWindow window = new SwingWindow(new RunCommunityBrowser().newPanel(), 1200, 800, true);
                //adjustGlobalFontSize(1.3f, window);
//            }
//        });

    }

}
