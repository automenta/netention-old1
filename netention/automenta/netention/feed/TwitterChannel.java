/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.feed;

import automenta.netention.Detail;
import automenta.netention.Mode;
import automenta.netention.NMessage;
import automenta.netention.value.geo.GeoPointIs;
import automenta.netention.value.string.StringIs;
import java.util.LinkedList;
import java.util.List;
import twitter4j.*;

/**
 *
 * @author SeH
 */
public class TwitterChannel {
 
    public static List<Detail> getTweets(double lat, double lng, double kilometersRadius) throws Exception {
        List<Detail> l = new LinkedList();
        
        // The factory instance is re-useable and thread safe.
        Twitter twitter = new TwitterFactory().getInstance();
        Query query = new Query();
        query.setGeoCode(new GeoLocation(lat, lng), kilometersRadius, Query.KILOMETERS);        
        QueryResult result = twitter.search(query);        
        for (Tweet tweet : result.getTweets()) {
            //System.out.println(tweet.getFromUser() + ":" + tweet.getText());
            
            Detail d = new Detail(tweet.getText(), Mode.Real, NMessage.StatusPattern, "Event").setID("twitter/" + tweet.getId());
            d.setWhenCreated(tweet.getCreatedAt());            
            d.add(NMessage.from, new StringIs("twitter.com/" + tweet.getFromUser()));
            
            GeoLocation geoloc = tweet.getGeoLocation();
            if (geoloc!=null) {
                d.addPattern("Located");
                d.add("currentLocation", new GeoPointIs(geoloc.getLatitude(), geoloc.getLongitude()));                
            }
            l.add(d);
        }        
        return l;
    }
    
    public static void main(String[] args) throws Exception {
    }
}
