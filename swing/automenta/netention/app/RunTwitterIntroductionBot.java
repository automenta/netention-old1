/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.app;

import automenta.netention.Detail;
import automenta.netention.NMessage;
import automenta.netention.Self;
import automenta.netention.Session;
import automenta.netention.feed.TwitterChannel;
import automenta.netention.impl.MemorySelf;
import automenta.netention.value.string.StringIs;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import twitter4j.Query;
import twitter4j.TwitterException;

/**
 *
 * @author SeH
 */
public class RunTwitterIntroductionBot {

    Map<String, Agent> agents = new ConcurrentHashMap();
    public List<String> agentList = new LinkedList();

    private final TwitterChannel tc;

    int refreshAfterCycles = 164;
    final long minTweetPeriod = 2 * 60 * 1000; //in ms
    final long analysisPeriod = 5 * 1000; //in ms
    long dontReuseAgentUntil = 60 * 60 * 2; //in seconds
    Set<String> queries = new ConcurrentSkipListSet<>();
    
    //heuristics: avg age of tweets since request = level of freshness / activity
    
    public static int getKeywordCount(String haystack, String needle) {
        haystack = haystack.toLowerCase();
        needle = needle.toLowerCase();
        if (haystack.contains(needle)) {
            return haystack.split(needle).length;            
        }
        return 0;
    }
    
    public static int getWordCount(String p) {
        if (!p.contains(" "))
            return 0;
        return p.split(" ").length;
    }
    
//    public static double getKeywordDensity(String haystack, String needle) {
//        double num = getKeywordCount(haystack, needle);
//        double den = getNumWords(haystack);
//        if (den == 0)
//            return 0;
//        return num/den;
//    }
    
    public class Agent {
        public final Set<Detail> details = new HashSet();
        private final String name;
        private Date lastContacted;

        public Agent(String name) {
            this.name = name;
            lastContacted = null;
        }
        
        public void add(Detail d) { details.add(d); }

        public double getAgeFactor(Date detailDate, Date now, double minutesFalloff) {
            double distMinutes = now.getTime() - detailDate.getTime();
            distMinutes /= 60.0 * 1000.0;
            return Math.exp( - (distMinutes/minutesFalloff) );
            
        }
        
        public double getMeanKeywordDensity(Date when, String... k) {
            double c = 0, n = 0;
            if (details.size() ==0)
                return 0;
            for (Detail d : details) {
                String p = d.getName();
                double num = 0;
                for (String r : k)
                    num += getKeywordCount(p, r);
                //double den = getWordCount(p);
                double den = 1;
                if (den!=0) {
                    double ageFactor = getAgeFactor(d.getWhen(), when, 60*2);
                    c += (num/den) * ageFactor;
                }
                n++;
            }
            return c / n;
        }
        
        public void update(TwitterChannel t) {
            Query q = new Query();
            q.setQuery("@" + name.split("/")[1]);
            
            try {
                List<Detail> tw = TwitterChannel.getTweets(q);
                details.addAll(tw);
                for (Detail d : tw)
                    addMentions(d);
            } catch (Exception ex) {
                Logger.getLogger(RunTwitterIntroductionBot.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    

    public Agent getAgent(String user) {
        
        if (agents.containsKey(user))
            return agents.get(user);
        
        Agent a = new Agent(user);
        agents.put(user, a);
        return a;
    }
    

    
    public void addMentions(Detail d) {
//        agentList.add(d.getCreator());
//        for (StringIs s : d.getValues(StringIs.class, NMessage.mentions)) {
//            agentList.add(s.getValue());
//        }
    }
    
    
    public String getLeadingAuthors(int num, final Date when, long minRepeatAgentTime, final String... keys) {
        List<String> a = new ArrayList(agents.keySet());
        Collections.sort(a, new Comparator<String>() {
            @Override public int compare(String a, String b) {                                
                return Double.compare(getAgent(b).getMeanKeywordDensity(when, keys), getAgent(a).getMeanKeywordDensity(when, keys));
            }                
        });

        String p = "";
        for (String x : a) {

            Agent ag = getAgent(x);
            Date lc = ag.lastContacted;
            if (lc!=null) {
                if (when.getTime() - lc.getTime() < minRepeatAgentTime * 1000) {
                    continue;
                }
            }
            
            ag.lastContacted = when;

            System.out.println(Arrays.asList(keys) + ": " + x + " " + getAgent(x).getMeanKeywordDensity(when, keys) + " instances in  " + getAgent(x).details.size() + " messages\n  " + getAgent(x).details.toString());
            p += "@" + x.split("/")[1] + " ";
            
            num--;
            if (num == 0)
                break;
        }

        return p.trim();
        
    }
    
    public static String oneOf(String... x) {
        int p = (int)Math.floor(Math.random() * x.length);
        return x[p];
    }
    
    protected void runHappySad() {
        queries.add("i am happy");
        queries.add("i am sad");
        
        int cycles = 1;
        while (true) {
           
            Date now = new Date();
                        
            String happyAuthors = getLeadingAuthors(3, now, dontReuseAgentUntil, 
                    "happy", "ecstatic", "joy", "thankful", "excited", "beautiful", "awesome" );
            String sadAuthors = getLeadingAuthors(2, now, dontReuseAgentUntil, 
                    "sad", "crying", "hurt", "hate" );
            
            if (!((happyAuthors.length() == 0) || (sadAuthors.length() == 0))) {            
                emit(happyAuthors + " seem #happy. " + oneOf("So please help", "Will you help") +  " " + sadAuthors + " who seem #sad ? " + oneOf("#Kindness", "#Health", "#Wisdom", "#Happiness"));
            }
            
            try {
                Thread.sleep(minTweetPeriod);
            } catch (InterruptedException ex) {
                Logger.getLogger(RunTwitterIntroductionBot.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
        }
        
    }
    
    protected void runSmartStupid() {
        queries.add("i am smart");        
        queries.add("i am stupid");
        
        try {
            Thread.sleep(minTweetPeriod / 2);
        } catch (InterruptedException ex) {
            Logger.getLogger(RunTwitterIntroductionBot.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        int cycles = 1;
        while (true) {
           
            Date now = new Date();
                        
            String happyAuthors = getLeadingAuthors(3, now, dontReuseAgentUntil, 
                    "smart", "brilliant", "genius", "helpful", "thankful", "solve"  );
            String sadAuthors = getLeadingAuthors(2, now, dontReuseAgentUntil, 
                    "stupid", "frustrated", "give up", "bored", "problem", "confused" );
            
            if (!((happyAuthors.length() == 0) || (sadAuthors.length() == 0))) {            
                emit(happyAuthors + " seem #intelligent. " + oneOf("Can you assist", "Can you help") +  " " + sadAuthors + " with " + oneOf("#Genius", "#Help", "#Ideas", "#Creativity") + " ?");
            }
            
            try {
                Thread.sleep(minTweetPeriod*2);
            } catch (InterruptedException ex) {
                Logger.getLogger(RunTwitterIntroductionBot.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
        }
        
    }
    
    protected void emit(String message) {
        System.out.println("TWEETING: " + message);

        try {
            tc.getTwitter().updateStatus(message);
        } catch (TwitterException ex) {
            Logger.getLogger(RunTwitterIntroductionBot.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void runAnalyzeUsers() {
        int k = 1;                     
        
        while (true) {

            if (agentList.size()  == 0) {
                for (String p : queries) {
                    try {
                        System.out.println("Keyword search: " + p);
                        List<Detail> tw = TwitterChannel.getTweets(p);
                        for (Detail d : tw) {
                            String a = d.getValue(StringIs.class, NMessage.from).getValue();
                            addMentions(d);
                            getAgent(a).add(d);
                            agentList.add(a);
                        }


                    } catch (Exception ex) {
                        Logger.getLogger(RunTwitterIntroductionBot.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }

            }

            if (agentList.size() > 0) {
                final String s = agentList.get(0);
                System.out.println("Investigating: " + s + ", one of " + agentList.size() + " remaining unknown agents");
                getAgent(s).update(tc);
                agentList.remove(s);
            }
            
            try {
                Thread.sleep(analysisPeriod);
            } catch (InterruptedException ex) {
                Logger.getLogger(RunTwitterIntroductionBot.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            if ((k % refreshAfterCycles) == 0) {
                System.out.println("RESTARTING with FRESH DATA");
                agents.clear();
                agentList.clear();                
            }
            k++;
        }
    }
    
    public RunTwitterIntroductionBot() throws Exception {

        Self s = new MemorySelf();
        
        tc = new TwitterChannel();
        tc.setKey(Session.get("twitter.key"));

        s.queue(new Runnable() {
            @Override public void run() {
                runAnalyzeUsers();
            }            
        });
        s.queue(new Runnable() {
            @Override public void run() {
                runHappySad();
            }                        
        });
        s.queue(new Runnable() {
            @Override public void run() {
                runSmartStupid();
            }                        
        });
        
        
    }
    
    
    
    public static void main(String[] args) throws Exception {
        Session.init();
        new RunTwitterIntroductionBot();
    }
}
