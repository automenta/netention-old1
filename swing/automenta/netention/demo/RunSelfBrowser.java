/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.demo;

import automenta.netention.Self;
import automenta.netention.craigslist.AddCraigslistPatterns;
import automenta.netention.craigslist.AddOodlePatterns;
import automenta.netention.ieml.AddENTPMflowcycles;
import automenta.netention.ieml.AddIEMLPatterns;
import automenta.netention.impl.MemorySelf;
import automenta.netention.survive.data.NuclearFacilities;
import automenta.netention.survive.data.EDIS;
import automenta.netention.swing.SelfBrowserPanel;
import automenta.netention.swing.SelfSession;
import automenta.netention.swing.util.SwingWindow;
import java.awt.Component;
import java.awt.Font;
import java.util.Enumeration;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

/**
 *
 * @author seh
 */
public class RunSelfBrowser implements Demo {

    public static void adjustGlobalFontSize(Float percentChange, Component root) {

        Enumeration keySet = UIManager.getDefaults().keys();
        while (keySet.hasMoreElements()) {
            Object key = keySet.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof Font) {
                Font f = (Font) value;
                Float sizef = f.getSize2D() * percentChange;
                int size = Math.round(sizef);
                FontUIResource resf = new FontUIResource(f.getName(), f.getStyle(), size);
                UIManager.put(key, resf);
            }
        }
        SwingUtilities.updateComponentTreeUI(root);

    }

    public static MemorySelf newDefaultSelf() {
        MemorySelf self = new MemorySelf("me", "Me");

        new AddDefaultPatterns().add(self);

        try {
            new AddOodlePatterns().add(self);

            //refactor hierarchy
            self.refactorPatternParent("oodle:sale", "Built");
            self.removePattern(self.getPattern("oodle:sale"));

            self.getPattern("oodle:vehicle").addParent("Built");

        } catch (Exception ex2) {
            ex2.printStackTrace();
        }

        new AddIEMLPatterns().add(self);

        //ImportOWL.load("schema/SUMO.owl", self);
        /*
        {
            AddOWLPatterns.add("schema/sumodlfull.owl", self);

            self.getPattern("http://stuarthendren.net/resource/sumodlfull.owl#Artifact").addParent("Built");
        }
        */
        

        //ImportOWL.load("schema/foaf.owl", self);
        //ImportOWL.load("schema/biography.owl", self);
        //ImportOWL.load("schema/sweetAll.owl", self);

        new AddCraigslistPatterns().add(self);

        new AddENTPMflowcycles().add(self);

        NuclearFacilities.add(self, "schema/IAEA_Nuclear_Facilities.csv");
        new EDIS(self);
        
        return self;
    }

    public JPanel newPanel() {
        final Logger logger = Logger.getLogger(SelfBrowserPanel.class.getName());

        final String filePath = "/tmp/netention1";
        
        Self self = newDefaultSelf();

//        //LOAD
//        MemorySelf self = newDefaultSelf();
//        try {
//            self = MemorySelf.load(filePath);
//            //self = JSONIO.load(filePath);
//            logger.log(Level.INFO, "Loaded " + filePath);
//        } catch (Exception ex) {
//            System.out.println("unable to load " + filePath + " : " + ex);
//            self = newDefaultSelf();
//            logger.log(Level.INFO, "Loaded Seed Self");
//        }
        //self.addPlugin(new Twitter());

        //TODO load Selfconfig from file and save when exiting
        SelfSession sc = new SelfSession();

        final Self mSelf = self;

        JPanel j = new SelfBrowserPanel(mSelf, sc, new SeedEnvironment());
        return j;
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
                adjustGlobalFontSize(1.3f, window);

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
