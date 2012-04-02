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
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import twitter4j.*;
import twitter4j.Tweet;
import twitter4j.conf.ConfigurationBuilder;

/**
 *
 * @author SeH
 */
public class TwitterChannel implements Serializable {

    String key;
    transient private Twitter twitter;

    public TwitterChannel() {
    }

    public void setKey(String key) {
        this.key = key;
        String[] k = key.split(",");
        
        if (k.length > 3) {
            ConfigurationBuilder cb = new ConfigurationBuilder();
            cb.setDebugEnabled(true)
            .setOAuthConsumerKey(k[0])
            .setOAuthConsumerSecret(k[1])
            .setOAuthAccessToken(k[2])
            .setOAuthAccessTokenSecret(k[3]);

            TwitterFactory tf = new TwitterFactory(cb.build());
            this.twitter = tf.getInstance();    
        }
    }

    public Twitter getTwitter() {
        return twitter;
    }
    
    public String getKey() {
        return key;
    }
    
    public static String getTwitterAgent(String twitterID) {
        return "twitter.com/" + twitterID;
    }

    public void updateStatus(String message) {
        try {
            getTwitter().updateStatus(message);
        } catch (TwitterException ex) {
            Logger.getLogger(TwitterChannel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    @Deprecated public static List<String> getPublicTweetStrings(int count) throws Exception {
        Twitter twitter = new TwitterFactory().getInstance();
        List<String> ll = new LinkedList();
        for (int i = 0; i < count; i++) {
            for (Object s : twitter.getPublicTimeline()) {
                ll.add(((Status)s).getText());
            }
        }
        return ll;
    }

    public static List<Detail> getTweets(Query query) throws Exception {
        List<Detail> l = new LinkedList();
        
        // The factory instance is re-useable and thread safe.
        Twitter twitter = new TwitterFactory().getInstance();
        QueryResult result = twitter.search(query);        
        for (Object o : result.getTweets()) {
            //System.out.println(tweet.getFromUser() + ":" + tweet.getText());
            Tweet tweet = (Tweet)o;
            Detail d = new Detail(tweet.getText(), Mode.Real, NMessage.StatusPattern, "Event").withID("twitter/" + tweet.getId());
            d.setWhen(tweet.getCreatedAt());            
            d.add(NMessage.from, new StringIs(getTwitterAgent(tweet.getFromUser())));
            if (tweet.getUserMentionEntities() != null)
                for (UserMentionEntity ume : tweet.getUserMentionEntities()) {
                    d.add(NMessage.mentions, new StringIs(getTwitterAgent(ume.getScreenName())));
                }
            
            GeoLocation geoloc = tweet.getGeoLocation();
            if (geoloc!=null) {
                d.addPattern("Located");
                d.add("currentLocation", new GeoPointIs(geoloc.getLatitude(), geoloc.getLongitude()));                
            }
            l.add(d);
        }        
        return l;
        
    }

    public static List<Detail> getTweets(double lat, double lng, double kilometersRadius) throws Exception {
        Query query = new Query();
        query.setGeoCode(new GeoLocation(lat, lng), kilometersRadius, Query.KILOMETERS);        

        return getTweets(query);        
    }

    public static List<Detail> getTweets(String q) throws Exception {
        return getTweets(new Query(q));
    }

    public static List<Detail> getTweets(String q, double lat, double lng, double radius) throws Exception {
        Query qi = new Query(q);
        qi.setGeoCode(new GeoLocation(lat, lng), radius, Query.KILOMETERS);
        return getTweets(qi);
    }
    
}
