/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.demo;

import automenta.netention.craigslist.AddCraigslistPatterns;
import automenta.netention.craigslist.AddOodlePatterns;
import automenta.netention.ieml.AddIEMLPatterns;
import automenta.netention.impl.MemorySelf;
import automenta.netention.rdf.AddOWLPatterns;
import automenta.netention.swing.SelfBrowserPanel;
import automenta.netention.swing.util.SwingWindow;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author seh
 */
public class RunSelfBrowser implements Demo {

    public static MemorySelf newDefaultSelf() {
        MemorySelf self = new MemorySelf("me", "Me");
        
        new AddDefaultPatterns().add(self);
        
        try {
            new AddOodlePatterns().add(self);
        } catch (Exception ex2) {
            ex2.printStackTrace();
        }
        
        new AddIEMLPatterns().add(self);

        //ImportOWL.load("schema/SUMO.owl", self);
        AddOWLPatterns.add("schema/sumodlfull.owl", self);
        //ImportOWL.load("schema/foaf.owl", self);
        //ImportOWL.load("schema/biography.owl", self);
        //ImportOWL.load("schema/sweetAll.owl", self);
        
        new AddCraigslistPatterns().add(self);
        
        return self;
    }

    public JPanel newPanel() {
        final Logger logger = Logger.getLogger(SelfBrowserPanel.class.getName());

        final String filePath = "/tmp/netention1";

        //LOAD
        MemorySelf self = newDefaultSelf();
        try {
            self = MemorySelf.load(filePath);
            //self = JSONIO.load(filePath);
            logger.log(Level.INFO, "Loaded " + filePath);
        } catch (Exception ex) {
            System.out.println("unable to load " + filePath + " : " + ex);
            self = newDefaultSelf();
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
