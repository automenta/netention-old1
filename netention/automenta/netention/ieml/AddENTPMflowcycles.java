package automenta.netention.ieml;

import automenta.netention.Pattern;
import automenta.netention.impl.MemorySelf;
import automenta.netention.value.set.SelectionProp;

/**
 *
 * @author ims
 */
public class AddENTPMflowcycles {

    public void add(MemorySelf self) {

        Pattern ENTPMflowcycles = self.addPattern(new Pattern("ENTPM flowcycles"));
        
        addContent(self, ENTPMflowcycles);
        addData(self, ENTPMflowcycles);
        addExp(self, ENTPMflowcycles);
        addInfo(self, ENTPMflowcycles);
        
        
    
    }

    private void addContent(MemorySelf self, Pattern ENTPMflowcycles) {

        Pattern ENTPMcontentflowcycles = self.addPattern(new Pattern("ENTPM content flowcycle", ENTPMflowcycles.getID()));
        
        {
            //ENTPM Content Flowcycle - Identifier
            /*
             * scale: ego <-> cause
             * individual collective output
             */


            SelectionProp p = new SelectionProp("entpm_identifier", "Identifier",
                    "individual", 
                    "collective", 
                    "output");
            p.setDescription("identifier is composed of blah blah");

            p.setCardinalityMax(-1);
            self.addProperty(p, ENTPMcontentflowcycles.getID());
        }

        {
            //ENTPM Content Flowcycle - Source
            /*
             * scale: culture <-> imagination
             * art literature games
             */


            SelectionProp p = new SelectionProp("entpm_source", "Source", 
                    "art", 
                    "literature", 
                    "games");

            p.setCardinalityMax(-1);
            self.addProperty(p, ENTPMcontentflowcycles.getID());
        }
        
        
        {
            //ENTPM Content Flowcycle - Architecture
            /*
             * scale: informational <-> experienced
             * consolidated distributed biological
             */


            SelectionProp p = new SelectionProp("entpm_architecture", "Architecture",
                    "consolidated", 
                    "distributed", 
                    "biological");

            p.setCardinalityMax(-1);
            self.addProperty(p, ENTPMcontentflowcycles.getID());
        }
        
        {
            //ENTPM Content Flowcycle - Creation
            /*
             * scale: idea <-> implementation
             * play work life
             */


            SelectionProp p = new SelectionProp("entpm_creation", "Creation", 
                    "play", 
                    "work", 
                    "life");

            p.setCardinalityMax(-1);
            self.addProperty(p, ENTPMcontentflowcycles.getID());
        }
        
        
        {
            //ENTPM Content Flowcycle - Dimension
            /*
             * scale: virtual <-> actual
             * technology physicality mind
             */


            SelectionProp p = new SelectionProp("entpm_dimension", "Dimension",     
                    "technology", 
                    "physicality", 
                    "mind");

            p.setCardinalityMax(-1);
            self.addProperty(p, ENTPMcontentflowcycles.getID());
        }
        
        
        
     
    }

    private void addData(MemorySelf self, Pattern ENTPMflowcycles) {
    
        Pattern ENTPMdataflowcycles = self.addPattern(new Pattern("ENTPM data flowcycle", ENTPMflowcycles.getID()));

        {
            //ENTPM Data Flowcycle - Notifiers
            /*
             * scale: off <-> on
             * language meaning context
             */


            SelectionProp p = new SelectionProp("entpm_notifiers", "Notifiers",
                    "language", 
                    "meaning", 
                    "context");

            p.setCardinalityMax(-1);
            self.addProperty(p, ENTPMdataflowcycles.getID());
        }

        {
            //ENTPM Data Flowcycle - Patterns
            /*
             * scale: cycles <-> phases
             * chance choice result
             */


            SelectionProp p = new SelectionProp("entpm_patterns", "Patterns", 
                    "chance", 
                    "choice", 
                    "result");

            p.setCardinalityMax(-1);
            self.addProperty(p, ENTPMdataflowcycles.getID());
        }
        
        
        {
            //ENTPM Data Flowcycle - Temperature
            /*
             * scale: hot <-> cold
             * chaos order uncertainty
             */


            SelectionProp p = new SelectionProp("entpm_temperature", "Temperature",
                    "chaos", 
                    "order", 
                    "uncertainty");

            p.setCardinalityMax(-1);
            self.addProperty(p, ENTPMdataflowcycles.getID());
        }
        
        {
            //ENTPM Data Flowcycle - Connections
            /*
             * scale: micro <-> macro
             * positions modes statistics
             */


            SelectionProp p = new SelectionProp("entpm_connections", "Connections", 
                    "positions", 
                    "modes", 
                    "statistics");

            p.setCardinalityMax(-1);
            self.addProperty(p, ENTPMdataflowcycles.getID());
        }
        
        
        {
            //ENTPM Exp Flowcycle - Hyperconsciousness
            /*
             * scale: conscious <-> subconscious
             * presence non-presence emergence
             */


            SelectionProp p = new SelectionProp("entpm_hyperconsciousness", "Hyperconsciousness",     
                    "presence", 
                    "non-presence", 
                    "emergence");

            p.setCardinalityMax(-1);
            self.addProperty(p, ENTPMdataflowcycles.getID());
        }
    }

    private void addExp(MemorySelf self, Pattern ENTPMflowcycles) {
        
        Pattern ENTPMexpflowcycles = self.addPattern(new Pattern("ENTPM exp flowcycle", ENTPMflowcycles.getID()));

        {
            //ENTPM Exp Flowcycle - Embodied
            /*
             * scale: full <-> empty
             * tolerance amplification decayed
             */


            SelectionProp p = new SelectionProp("entpm_embodied", "Embodied",
                    "tolerance", 
                    "amplification", 
                    "decayed");

            p.setCardinalityMax(-1);
            self.addProperty(p, ENTPMexpflowcycles.getID());
        }

        {
            //ENTPM Exp Flowcycle - Textural
            /*
             * scale: smooth <-> coarse
             * lived dreamed remembered
             */


            SelectionProp p = new SelectionProp("entpm_textural", "Textural", 
                    "lived", 
                    "dreamed", 
                    "remembered");

            p.setCardinalityMax(-1);
            self.addProperty(p, ENTPMexpflowcycles.getID());
        }
        
        
        {
            //ENTPM Exp Flowcycle - Flowing
            /*
             * scale: delayed <-> paced
             * discovered mystery struggle
             */


            SelectionProp p = new SelectionProp("entpm_flowing", "Flowing",
                    "discovered", 
                    "mystery", 
                    "struggled");

            p.setCardinalityMax(-1);
            self.addProperty(p, ENTPMexpflowcycles.getID());
        }
        
        {
            //ENTPM Exp Flowcycle - Profound
            /*
             * scale: deep <-> wide
             * aesthetics emotion sensation
             */


            SelectionProp p = new SelectionProp("entpm_profound", "Profound", 
                    "aesthetics", 
                    "emotion", 
                    "sensation");

            p.setCardinalityMax(-1);
            self.addProperty(p, ENTPMexpflowcycles.getID());
        }
        
        
        {
            //ENTPM Exp Flowcycle - Superficial
            /*
             * scale: high <-> narrow
             * delivered transformed alluded
             */


            SelectionProp p = new SelectionProp("entpm_superficial", "Superficial",     
                    "delivered", 
                    "transformed", 
                    "alluded");

            p.setCardinalityMax(-1);
            self.addProperty(p, ENTPMexpflowcycles.getID());
        }
        
    }

    private void addInfo(MemorySelf self, Pattern ENTPMflowcycles) {
        
        Pattern ENTPMinfoflowcycles = self.addPattern(new Pattern("ENTPM info flowcycles", ENTPMflowcycles.getID()));

        {
            //ENTPM Information Flowcycle - Organism
            /*
             * scale: cybernetic <-> hybrid
             * human animal robotic
             */


            SelectionProp p = new SelectionProp("entpm_organism", "Organism",
                    "human", 
                    "animal", 
                    "robotic");

            p.setCardinalityMax(-1);
            self.addProperty(p, ENTPMinfoflowcycles.getID());
        }

        {
            //ENTPM Information Flowcycle - Composition
            /*
             * scale: raw <-> synthetic
             * natural artificial processed
             */


            SelectionProp p = new SelectionProp("entpm_composition", "Composition", 
                    "natural", 
                    "artificial", 
                    "processed");

            p.setCardinalityMax(-1);
            self.addProperty(p, ENTPMinfoflowcycles.getID());
        }
        
        
        {
            //ENTPM Information Flowcycle - Shape
            /*
             * scale: dense <-> loose
             * nets blobs spikes
             */


            SelectionProp p = new SelectionProp("entpm_shape", "Shape",
                    "nets", 
                    "blobs", 
                    "spikes");

            p.setCardinalityMax(-1);
            self.addProperty(p, ENTPMinfoflowcycles.getID());
        }
        
        {
            //ENTPM Information Flowcycle - Communication
            /*
             * scale: abstract <-> precise
             * words numbers form
             */


            SelectionProp p = new SelectionProp("entpm_communication", "Communication", 
                    "words", 
                    "numbers", 
                    "form");

            p.setCardinalityMax(-1);
            self.addProperty(p, ENTPMinfoflowcycles.getID());
        }
        
        
        {
            //ENTPM Information Flowcycle - Environment
            /*
             * scale: reality <-> dream
             * space earth mind
             */


            SelectionProp p = new SelectionProp("entpm_environment", "Environment",     
                    "space", 
                    "earth", 
                    "mind");

            p.setCardinalityMax(-1);
            self.addProperty(p, ENTPMinfoflowcycles.getID());
        }
    }
}
