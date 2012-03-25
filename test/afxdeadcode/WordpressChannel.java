/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package afxdeadcode;

import automenta.netention.Session;
import net.bican.wordpress.Page;
import net.bican.wordpress.Wordpress;
import redstone.xmlrpc.XmlRpcFault;

/**
 *
 * @author SeH
 */
public class WordpressChannel extends Wordpress {

    public WordpressChannel() throws Exception {
        this(Session.get("wordpress.user"), Session.get("wordpress.pass"), Session.get("wordpress.url"));
        
        System.out.println(getUserInfo());
    }
    
    
    public WordpressChannel(String user, String pass, String url) throws Exception {
         //Wordpress wp = new Wordpress(user, pass, url);
        super(user, pass, url + "/xmlrpc.php");
        
         
    }
    
    public String newPost(String title, String html) throws XmlRpcFault {
        Page page = new Page();
        page.setTitle(title);
        page.setDescription(html);
        
        String id = newPost(page, true);
        return getPost(Integer.parseInt(id)).getLink();
    }
    

    public static void main(String[] args) throws Exception {
        Session.init();
        
        WordpressChannel x = new WordpressChannel(Session.get("wordpress.user"), Session.get("wordpress.pass"), Session.get("wordpress.url"));
        
        System.out.println("Posting..");
        System.out.println(x.newPost("Title2", "<b>HTML</b>"));
        
    }
    
}
