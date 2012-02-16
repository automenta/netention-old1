/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.irc;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.PircBot;

/**
 *
 * @author seh
 */
public class IRCServerConnection extends PircBot {
    
    private Map<String, IRCChannel> chans = new HashMap();
    
    public IRCServerConnection(String name, String ircHost) throws IOException, IrcException {
        super();
        this.setName(name);
        this.setLogin(name);
        this.setFinger("netention");
        this.setVersion("netention");
        this.connect(ircHost);        
        
    }

    public static String getVERSION() {
        return VERSION;
    }

    public void join(String channel, IRCChannel c) {
        chans.put(channel, c);
        c.onConnect(this, channel);
        joinChannel(channel);
    }
    public void leave(String channel) {
        chans.remove(channel);
    }
    
    public void onMessage(String channel, String sender,
                       String login, String hostname, String message) {
//        if (message.equalsIgnoreCase("time")) {
//            String time = new java.util.Date().toString();
//            sendMessage(channel, sender + ": The time is now " + time);
//        }
//        System.out.println(channel + " " + sender + " " + login + " " + hostname + " : " + message);
        
        IRCChannel c = chans.get(channel);
        if (c!=null)
            c.onMessage(sender, login, hostname, message);
        
    }
    
    
    public static void main(String[] args) throws Exception {
        IRCServerConnection i = new IRCServerConnection("sseehh_netention1", "irc.freenode.net");
        
        String c = "#b350f99700cd057aa9ab284c31b5eb2b";
        i.join(c, new IRCChannel() {

            @Override
            public void onMessage(String sender, String login, String hostname, String message) {
                System.out.println(this + " received: " + message);
            }
            
        });
        
        //i.sendMessage(c, "Hi");
        
        //i.disconnect();
    }
}
