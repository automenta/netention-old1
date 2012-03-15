/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.feed;

import automenta.netention.Session;
import java.io.IOException;
import java.net.URL;
import mx.bigdata.jcalais.CalaisConfig;
import mx.bigdata.jcalais.CalaisObject;
import mx.bigdata.jcalais.CalaisResponse;
import mx.bigdata.jcalais.rest.CalaisRestClient;

/**
 *
 * @author SeH
 */
public class Calais  {
    private final CalaisRestClient client;
    private final CalaisConfig config;

    public Calais() {
        this(Session.get("opencalais.key"));
    }

    public Calais(String apiKey) {
        super();
        
        this.client = new CalaisRestClient(apiKey);
        
        config = new CalaisConfig();
        config.set(CalaisConfig.ConnParam.CONNECT_TIMEOUT, 10000);
        config.set(CalaisConfig.ConnParam.READ_TIMEOUT, 10000);        
        
    
//        CalaisResponse response = client.analyze("Prosecutors at the trial of former Liberian President Charles Taylor " 
//           + " hope the testimony of supermodel Naomi Campbell " 
//           + " will link Taylor to the trade in illegal conflict diamonds, "
//           + " which they say he used to fund a bloody civil war in Sierra Leone.");   
    }
    
    public CalaisResponse analyze(URL u) throws IOException { return client.analyze(u, config); }
    public CalaisResponse analyze(String s) throws IOException { return client.analyze(s, config); }
    
    public static void main(String[] args) throws IOException {
        Session.init();
        
        CalaisResponse x = new Calais().analyze("" );
        System.out.println(x.getInfo());
        for (CalaisObject o : x.getEntities()) {
            System.out.println("Entity: " + o);                    
        }
        for (CalaisObject o : x.getTopics()) {
            System.out.println("Topics: " + o);                    
        }
        for (CalaisObject o : x.getRelations()) {
            System.out.println("Relations: " + o);                    
        }
        for (CalaisObject o : x.getSocialTags()) {
            System.out.println("Social Tags: " + o);                    
        }
                
    }
}
