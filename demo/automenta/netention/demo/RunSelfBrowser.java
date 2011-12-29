/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.demo;

import automenta.netention.impl.MemorySelf;
import automenta.netention.demo.Demo;
import automenta.netention.demo.Demo;
import automenta.netention.demo.SeedEnvironment;
import automenta.netention.demo.SeedSelfBuilder;
import automenta.netention.swing.SelfBrowserPanel;
import automenta.netention.swing.util.SwingWindow;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 *
 * @author seh
 */
public class RunSelfBrowser implements Demo {

    public JPanel newPanel() {
        final Logger logger = Logger.getLogger(SelfBrowserPanel.class.getName());

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
        //self.addPlugin(new Twitter());

        final MemorySelf mSelf = self;

        return new SelfBrowserPanel(mSelf, new SeedEnvironment());
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {

                SwingWindow window = new SwingWindow(new RunSelfBrowser().newPanel(), 900, 800, true) {

//                    @Override
//                    protected void onClosing() {
//                        //SAVE ON EXIT
//                        try {
//
//                            mSelf.save(filePath);
//                            //JSONIO.save(mSelf, filePath);
//                            logger.log(Level.INFO, "Saved " + filePath);
//                        } catch (Exception ex) {
//                            logger.log(Level.SEVERE, null, ex);
//                        }
//                    }
                    
                };
            }
        });

    }

    @Override
    public String getName() {
        return "Self Browser";
    }

    @Override
    public String getDescription() {
        return "";
    }

}
