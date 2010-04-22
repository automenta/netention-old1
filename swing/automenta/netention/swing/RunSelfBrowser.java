/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.swing;

import automenta.netention.impl.MemorySelf;
import automenta.netention.io.twitter.Twitter;
import automenta.netention.swing.util.SwingWindow;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;

/**
 *
 * @author seh
 */
public class RunSelfBrowser {

    public static void main(String[] args) {
        final Logger logger = Logger.getLogger(SelfBrowserPanel.class.getName());

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
        }

        final String filePath = "/tmp/netention1";

        //LOAD
        MemorySelf self;
        try {
            self = MemorySelf.load(filePath);
            //self = JSONIO.load(filePath);
            logger.log(Level.INFO, "Loaded " + filePath);
        } catch (Exception ex) {
            System.out.println("unable to load " + filePath + " : " + ex);
            self = new MemorySelf("me", "Me");
            new SeedSelfBuilder().build(self);
            logger.log(Level.INFO, "Loaded Seed Self");
        }
        self.addPlugin(new Twitter());

        final MemorySelf mSelf = self;
        SwingWindow window = new SwingWindow(new SelfBrowserPanel(self), 900, 800, true) {

            @Override
            protected void onClosing() {
                //SAVE ON EXIT
                try {
                    mSelf.save(filePath);
                    //JSONIO.save(mSelf, filePath);
                    logger.log(Level.INFO, "Saved " + filePath);
                } catch (Exception ex) {
                    logger.log(Level.SEVERE, null, ex);
                }
            }
        };

    }
}
