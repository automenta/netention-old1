/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.irc;

import java.io.IOException;
import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.PircBot;

/**
 *
 * @author seh
 */
public class IRCChannel extends PircBot {
    
    public IRCChannel(String name, String ircHost) throws IOException, IrcException {
        super();
        this.setName(name);
        this.setLogin("netention");
        this.setFinger("netention");
        this.setVersion("netention");
        this.connect(ircHost);        
        
    }

    public static String getVERSION() {
        return VERSION;
    }
    
    
    public void onMessage(String channel, String sender,
                       String login, String hostname, String message) {
//        if (message.equalsIgnoreCase("time")) {
//            String time = new java.util.Date().toString();
//            sendMessage(channel, sender + ": The time is now " + time);
//        }
        System.out.println(channel + " " + sender + " " + login + " " + hostname + " : " + message);
    }
    
    public static void main(String[] args) throws Exception {
        IRCChannel i = new IRCChannel("sseehh_netention", "irc.freenode.net");
        
        String c = "#b350f99700cd057aa9ab284c31b5eb2b";
        i.joinChannel(c);
        
        //i.sendMessage(c, "Hi");
        
        //i.disconnect();
    }
}
