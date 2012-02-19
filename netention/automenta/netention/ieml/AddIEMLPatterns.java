
package automenta.netention.ieml;

import automenta.netention.Pattern;
import automenta.netention.Self;
import automenta.netention.value.set.SelectionProp;

/**
 *
 * @author seh
 */
public class AddIEMLPatterns {

    public void add(Self self) {

        Pattern ieml = self.addPattern(new Pattern("IEML"));

        {
            //http://www.ieml.org/english/events.html
            /*
             * woreflect 	waact 	yknow 	owant 	ecan
            wuperceive 	wereconstitute 	uexpress 	acommit 	ido
            jsignifier mutation 	gdocumentary mutation 	sthought 	blanguage 	tmemory
            hmutation of meaning 	cpersonal mutation 	ksociety 	maffect 	nworld
            pmutation of referent 	xmaterial mutation 	dtruth 	flife 	lspace
             */


            SelectionProp p = new SelectionProp("ieml_event", "Event",
                    "reflect", "act", "know", "want", "can",
                    "perceive", "reconstitute", "express", "commit", "do",
                    "signifier mutation", "documentary mutation", "thought", "language", "memory",
                    "mutation of meaning", "personal mutation", "society", "affect", "world",
                    "mutation of referent", "material mutation", "truth", "life", "space");

            p.setCardinalityMax(-1);
            self.addProperty(p, ieml.getID());
        }

        {

            //http://www.ieml.org/english/ooom_1.html

            /*woyto get one's bearings into knowledge 
             * wooto establish principles 	
             * woeto identify competencies
             * wouto get one's bearings into information 
             * woato find one's fitting place 
             * woito choose equipment
             * 
            wayto master knowledge 
             * waoto stick to principles 
             * waeto master competencies 
             * wauto study the documentation 
             * waato cultivate one's character 
             * waito use the equipment
             * 
            wuyquestioning accepted knowledge 
             * wuoto question oneself 
             * wueto assess competencies 	
             * wuuto find information 
             * wuato study characters 
             * wuito test the equipment
             * 
            weyto rebuild knowledge 
             * weoto renew objectives 
             * weeto renew competencies 
             * weuto refresh one's documentation 
             * weato serve one's community 
             * weito renew the equipment*/

            SelectionProp p = new SelectionProp("ieml_cycle_behaviors", "Behavior Cycle",
                    "to get one's bearings into knowledge ",
                    "to establish principles",
                    "to identify competencies",
                    "to get one's bearings into information",
                    "to find one's fitting place",
                    "to choose equipment",
                    "to master knowledge",
                    "to stick to principles",
                    "to master competencies",
                    "to study the documentation",
                    "to cultivate one's character",
                    "to use the equipment",
                    "questioning accepted knowledge",
                    "to question oneself",
                    "to assess competencies",
                    "to find information",
                    "to study characters",
                    "to test the equipment",
                    "to rebuild knowledge",
                    "to renew objectives",
                    "to renew competencies",
                    "to refresh one's documentation",
                    "to serve one's community",
                    "to renew the equipment");

            p.setCardinalityMax(-1);
            self.addProperty(p, ieml.getID());
        }
        
        {
            //http://www.ieml.org/english/oooo_1.html
            /*
             * wowoto be born 	wowato work 	wowuto suffer 	woweto eat / to drink
            wawoto sleep 	wawato be fulfilled 	wawuto celebrate 	waweto bring forth / to give birth
            wuwoto survive 	wuwato lament 	wuwuto discover 	wuweto love
            wewoto get used to 	wewato grow old 	wewuto make love 	weweto die
             */
            SelectionProp p = new SelectionProp("ieml_cycle_lifestages", "Life Stages Cycle",
             "to be born",
             "to work",
             "to suffer",
             "to eat / to drink",
             "to sleep",
             "to be fulfilled",
             "to celebrate",
             "to bring forth / to give birth",
                
             "to survive",
             "to lament",
             "to discover",
             "to love",
                                            
             "to get used to",
             "to grow old",
             "to make love",
             "to die"
             );
            p.setCardinalityMax(-1);
            self.addProperty(p, ieml.getID());
        }
        
        {
            //http://www.ieml.org/english/ommo_1.html
            /*
             * wowoto be born 	wowato work 	wowuto suffer 	woweto eat / to drink
            wawoto sleep 	wawato be fulfilled 	wawuto celebrate 	waweto bring forth / to give birth
            wuwoto survive 	wuwato lament 	wuwuto discover 	wuweto love
            wewoto get used to 	wewato grow old 	wewuto make love 	weweto die
             */
            SelectionProp p = new SelectionProp("ieml_production_lifecycle", "Production Cycle",
             "to examine possible media",
             "to consider possible discourse",
             "to examine possible meanings",
             "to examine one's path",
             "to examine possible subjects",
             "review of possible methods",
                 
             "to choose form of expression",
             "to choose one's discourse",
             "to choose an interpretation",
             "to orient one's path",
             "to choose a subject",
             "to orient one's work",
                    
             "to master a medium",
             "to make a discourse one's own",
             "to follow a method of interpretation",
             "to prepare oneself for change",
             "to be equal to a subject",
             "to make tools one's own",
             
             "to analyze one's language",
             "to deliver a speech",
             "to state the meaning",
             "to commit to change",
             "to explain one's subject",
             "to explain one's work",
            
             "to assume one's style",
             "to commit into one's discourse",
             "to defend an interpretation",
             "to accept one's transformation",
             "to get involved in one's subject",
             "to get involved in one's work",
            
             "to refine one's language",
             "to elaborate one's discourse",
             "to elaborate an interpretation",
             "to work on oneself",                    
             "to construct one's subject",
             "to work with matter"
             );
            p.setCardinalityMax(-1);
            self.addProperty(p, ieml.getID());
        }

    }
}