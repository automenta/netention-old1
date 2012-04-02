/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package afxdeadcode;

import afxdeadcode.bots.HappySad;
import afxdeadcode.bots.RichPoor;
import automenta.netention.Session;


/**
 *
 * @author SeH
 */
public class RunHappySadRichPoor {
    

    public static void main(String[] args) throws Exception {

        long minTweetPeriod = Community.ms(0, 5, 0, 0); //in ms, safe @ 6m
        long analysisPeriod = Community.ms(0,0,1.0f,0);  //in ms
        long dontReinvestigate = Community.ms(1,0,0,0); //in ms
        long waitForNextQueries = Community.ms(2,0,0,0); //in ms
        int refreshAfterAnalysisCycles = 1 * 12000; //the great cycle... only makes sense relative to analysisPeriod... TODO find better way to specify this
        
        Session.init();
        
        afxdeadcode.Community c = new afxdeadcode.Community();
        new HappySad(c, minTweetPeriod, minTweetPeriod * 3 /8);
        new RichPoor(c, minTweetPeriod, minTweetPeriod * 7 /8);
        //s.queue(new SmartStupid(2 * 60, 2 * 60));
        
        c.setGeo(40.4399198, -80.0000702, 10);
        
        c.start(analysisPeriod, dontReinvestigate, waitForNextQueries, refreshAfterAnalysisCycles);
                
    }

}
