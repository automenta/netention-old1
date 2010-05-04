/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.plugin.webdav;

import com.googlecode.sardine.DavResource;
import com.googlecode.sardine.Sardine;
import com.googlecode.sardine.SardineFactory;
import com.googlecode.sardine.util.SardineException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author seh
 */
public class WebDavNetwork {
    private final String url;
    private final String login;
    private final Sardine connection;

    public WebDavNetwork(String url, String login, String password) throws SardineException {
        super();
        this.url = url;
        this.login = login;
        
        connection = SardineFactory.begin(login, password);

    }

    public Sardine getConnection() {
        return connection;
    }
    
    public static void main(String[] args) {
        try {
            WebDavNetwork n = new WebDavNetwork("", "wintermute", "abc123abc");
            for (DavResource r : n.getConnection().getResources("http://webdav.mydrive.ch/")) {
                System.out.println(r);
            }

        } catch (SardineException ex) {
            Logger.getLogger(WebDavNetwork.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
