/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.irc;

import automenta.netention.Session;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

/**
 *
 * @author SeH
 */
public class XMPPChannel extends XMPPConnection{
    //http://abhijeetmaharana.com/blog/2007/10/28/writing-a-gtalk-jabberxmpp-client/

    public static ConnectionConfiguration getSecureConnectionConfiguration(String server, int port, String service) {
        ConnectionConfiguration connConfig = new ConnectionConfiguration(server, port, service);
        connConfig.setSecurityMode(ConnectionConfiguration.SecurityMode.enabled);
        //connConfig.setSocketFactory(new DefaultSSLSocketFactory());        
        return connConfig;
    }
    
    public XMPPChannel() throws XMPPException {
        this(Session.get("xmpp.server"), Integer.parseInt(Session.get("xmpp.port")), Session.get("xmpp.service"),
                Session.get("xmpp.username"),
                Session.get("xmpp.password"));
    }
    
    public XMPPChannel(String server, int port, String service, String username, String password) throws XMPPException {
        this(username, password, getSecureConnectionConfiguration(server, port, service));
    }
    
    public XMPPChannel(String username, String password, ConnectionConfiguration cc) throws XMPPException {
        super(cc);
//        ConnectionConfiguration config = 
//                new ConnectionConfiguration("talk.google.com", 
//                                                5222, 
//                                                "gmail.com");
        
        
        connect();
        login(username, password);
                
    }
    
    public static void main(String[] args) throws Exception {
        Session.init();
        new XMPPChannel(Session.get("xmpp.server"), Integer.parseInt(Session.get("xmpp.port")), Session.get("xmpp.service"),
                Session.get("xmpp.username"),
                Session.get("xmpp.password")
                );
    }
    
 
    
}
