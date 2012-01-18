/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.ieml;

import automenta.netention.Pattern;
import automenta.netention.impl.MemorySelf;
import automenta.netention.value.set.SelectionProp;

/**
 *
 * @author seh
 */
public class IEMLBuilder {

    public void build(MemorySelf ms) {
        //http://www.ieml.org/english/events.html

        Pattern ieml = new Pattern("IEML");
        ms.addPattern(ieml);

        {
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
            ms.addProperty(p, ieml.getID());
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
                    "woy to get one's bearings into knowledge ",
                    "woo to establish principles",
                    "woe to identify competencies",
                    "wou to get one's bearings into information",
                    "woa to find one's fitting place",
                    "woi to choose equipment",
                    "way to master knowledge",
                    "wao to stick to principles",
                    "wae to master competencies",
                    "wau to study the documentation",
                    "waa to cultivate one's character",
                    "wai to use the equipment",
                    "wuy questioning accepted knowledge",
                    "wuo to question oneself",
                    "wue to assess competencies",
                    "wuu to find information",
                    "wua to study characters",
                    "wui to test the equipment",
                    "wey to rebuild knowledge",
                    "weo to renew objectives",
                    "wee to renew competencies",
                    "weu to refresh one's documentation",
                    "wea to serve one's community",
                    "wei to renew the equipment");

            p.setCardinalityMax(-1);
            ms.addProperty(p, ieml.getID());
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
             "wowo to be born",
             "wowa to work",
             "wowu to suffer",
             "wowe to eat / to drink",
             "wawo to sleep",
             "wawa to be fulfilled",
             "wawu to celebrate",
             "wawe to bring forth / to give birth",
                
             "wuwo to survive",
             "wuwa to lament",
             "wuwu to discover",
             "wuwe to love",
                                            
             "wewo to get used to",
             "wewa to grow old",
             "wewu to make love",
             "wewe to die"
             );
            p.setCardinalityMax(-1);
            ms.addProperty(p, ieml.getID());
        }

    }
}
