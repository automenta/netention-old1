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
    
    final static long minTweetPeriod = 6 * 60; //in s, safe @ 6

    public static void main(String[] args) throws Exception {
        Session.init();
        
        afxdeadcode.Community c = new afxdeadcode.Community();
        new HappySad(c, minTweetPeriod, minTweetPeriod * 3 /8);
        new RichPoor(c, minTweetPeriod, minTweetPeriod * 7 /8);
        //s.queue(new SmartStupid(2 * 60, 2 * 60));
        c.start();
        
        
    }

}
