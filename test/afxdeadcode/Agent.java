/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package afxdeadcode;

import automenta.netention.Detail;
import automenta.netention.feed.TwitterChannel;
import com.twitter.Extractor;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author SeH
 */
//    public static double getKeywordDensity(String haystack, String needle) {
public class Agent {

    public final static Extractor ext = new Extractor();
    
    public static String filterTweet(String p) {
        List<String> blacklist = new LinkedList();
        for (String m : ext.extractMentionedScreennames(p)) {
            blacklist.add("@" + m);
        }
       
        blacklist.addAll(ext.extractURLs(p));
        if (ext.extractReplyScreenname(p)!=null)
            blacklist.add("@" + ext.extractReplyScreenname(p));
        for (String b : blacklist) {
            p = p.replace(b, "");
        }        
        if (p.startsWith("RT ")) {
            p = p.replaceFirst("RT ", "");
        }
        
        return p.trim();
    }
    
    public final Set<Detail> details = Collections.synchronizedSet(new HashSet<Detail>());
    final transient Map<String, Double> scores = new ConcurrentHashMap();
    public final String name;
    public Date lastContacted, lastUpdated = new Date();

    public Agent(String name) {
        this.name = name;
        lastContacted = null;
    }

    public void add(Detail d) {
        details.add(d);
    }

    public static double getAgeFactor(Date detailDate, Date now, double minutesFalloff) {
        double distMinutes = now.getTime() - detailDate.getTime();
        distMinutes /= 60.0 * 1000.0;
        return Math.exp(-(distMinutes / minutesFalloff));
    }

//    public void print(Classifier classifier) {
//        System.out.println(name);
//        System.out.println("  Happy=" + getScore(classifier, new Date(), "happy"));
//        System.out.println("  Sad=" + getScore(classifier, new Date(), "sad"));
//        System.out.println("  Rich=" + getScore(classifier, new Date(), "rich"));
//        System.out.println("  Poor=" + getScore(classifier, new Date(), "poor"));
//    }
//    
    
    public double getScore(NGramClassifier classifier, Date lastUpdated, Detail d) {
            String p = filterTweet(d.getName());            
            //final double bg = classifier.getAverageBackgroundDistance(k);
            double distance = classifier.getDistance(p);        
            //return (bg-distance) / bg;
            return distance;
    }
    
    public double getAgeFactor(Detail d, double focusMinutes) {
        return getAgeFactor(d.getWhen(), lastUpdated, focusMinutes);
    }

    public double getScore(NGramClassifier classifier, Date when, double focusMinutes) {
        if ((scores.containsKey(classifier.getName()) && when.equals(lastUpdated))) {
            return scores.get(classifier.getName());
        }
        double c = 0;
        double n = 0;
        if (details.size() == 0) {
            return 0;
        }
        
        //final double bg = classifier.getAverageBackgroundDistance(k);
        
        for (Detail d : details) {
            String p = filterTweet(d.getName());
            
            double distance = classifier.getDistance(p);
            
            //METHOD 1: ratio
            //double num = bg / distance;
            
            //METHOD 2: difference
//            double num = (bg - distance)/bg;
//            if (num < 0)
//                num = 0;
            
            //METHOD 3: no bg involved
            double num = distance;
            
            double ageFactor;
            if (when!=null)
                 ageFactor = getAgeFactor(d.getWhen(), when, focusMinutes);
            else
                 ageFactor = 1.0;
            
            c += num * ageFactor;
        }
        if (when.equals(lastUpdated)) {
            scores.put(classifier.getName(), c);
        }
        return c;
    }

    //        public double getScore(Date when, String... k) {
    //            double c = 0, n = 0;
    //            if (details.size() ==0)
    //                return 0;
    //            for (Detail d : details) {
    //                String p = d.getName();
    //                double num = 0;
    //                for (String r : k) {
    //                    num += classifier.analyzeC(p, r);
    //                }
    //                double den = 1;
    //                if (den!=0) {
    //                    double ageFactor = getAgeFactor(d.getWhen(), when, focusMinutes);
    //                    c += (num/den) * ageFactor;
    //                }
    //            }
    //            return c;
    //        }
    //        public double getMeanKeywordDensity(Date when, String... k) {
    //            double c = 0, n = 0;
    //            if (details.size() ==0)
    //                return 0;
    //            for (Detail d : details) {
    //                String p = d.getName();
    //                double num = 0;
    //                for (String r : k)
    //                    num += getKeywordCount(p, r);
    //                //double den = getWordCount(p);
    //                double den = 1;
    //                if (den!=0) {
    //                    double ageFactor = getAgeFactor(d.getWhen(), when, focusMinutes);
    //                    c += (num/den) * ageFactor;
    //                }
    //                n++;
    //            }
    //            return c / n;
    //        }
    public void update(TwitterChannel t) {
        try {
            List<Detail> tw = TwitterChannel.getTweets("@" + name.split("/")[1]);
            details.addAll(tw);
            lastUpdated = new Date();
            //                for (Detail d : tw)
            //                    addMentions(d);
        }catch (Exception ex) {
            Logger.getLogger(Community.class.getName()).log(Level.SEVERE, null, ex);
        }
        scores.clear();
    }

    public List<Detail> getDetailsByTime2() {
        
        List<Detail> s = new LinkedList(details);
        Collections.sort(s, new Comparator<Detail>() {
            @Override public int compare(Detail o1, Detail o2) {
                return o2.getWhen().compareTo(o1.getWhen());
            }            
        });
        
        return s;
        
    }
    public List<Detail> getDetailsByTime() {
        
        List<Detail> s = new LinkedList(details);
        Collections.sort(s, new Comparator<Detail>() {
            @Override public int compare(Detail o1, Detail o2) {
                return o2.getWhen().compareTo(o1.getWhen());
            }            
        });
        
        return s;
        
    }

    public static void main(String[] args) {

     
    }
    
}
