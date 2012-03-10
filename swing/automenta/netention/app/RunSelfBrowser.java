/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.app;

import automenta.netention.Pattern;
import automenta.netention.Self;
import automenta.netention.craigslist.AddCraigslistPatterns;
import automenta.netention.craigslist.CraigslistRefreshAction;
import automenta.netention.demo.AddDefaultPatterns;
import automenta.netention.demo.Demo;
import automenta.netention.demo.SeedEnvironment;
import automenta.netention.html.BasicDetailHTML;
import automenta.netention.ieml.AddENTPMflowcycles;
import automenta.netention.ieml.AddIEMLPatterns;
import automenta.netention.impl.LogToMessage;
import automenta.netention.impl.MemorySelf;
import automenta.netention.survive.data.EDIS;
import automenta.netention.survive.data.NuclearFacilities;
import automenta.netention.swing.SelfBrowserPanel;
import automenta.netention.swing.SelfSession;
import automenta.netention.swing.detail.action.SendAction;
import automenta.netention.swing.util.SwingWindow;
import java.awt.Component;
import java.awt.Font;
import java.util.Enumeration;
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

        {
            
            self.addAction(new SendAction(new BasicDetailHTML()));
            self.addAction(new CraigslistRefreshAction(false));
            self.addAction(new CraigslistRefreshAction(true));
                        
        }
        
        
        new AddDefaultPatterns().add(self);

        /*
        try {
            new AddOodlePatterns().add(self);

            //refactor hierarchy
            self.refactorPatternParent("oodle:sale", "Built");
            self.removePattern(self.getPattern("oodle:sale"));

            self.getPattern("oodle:vehicle").addParent("Built");

        } catch (Exception ex2) {
            ex2.printStackTrace();
        }
        * 
        */

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
        
        Pattern disaster; 
        {
            self.addPattern(disaster = new Pattern("Disaster").setIconURL("media://edis/DS_VW.terror.png"));

            //new EDIS().init(self, disaster).update(self);
            
        }
        
        //new IntentionalCommunities(self, "schema/ic.org.xml");
        
        new LogToMessage(self);
        
        return self;
    }

    public JPanel newPanel() {
//        final Logger logger = Logger.getLogger(SelfBrowserPanel.class.getName());

//        final String filePath = "/tmp/netention1";       
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
        Self self = newDefaultSelf();
        SelfSession sc = new SelfSession();

        JPanel j = new SelfBrowserPanel(self, sc, new SeedEnvironment());
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
