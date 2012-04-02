package afxdeadcode;

import afxdeadcode.bots.HappySad;
import afxdeadcode.bots.RichPoor;
import automenta.netention.Detail;
import automenta.netention.NMessage;
import automenta.netention.email.EMailChannel;
import automenta.netention.feed.TwitterChannel;
import automenta.netention.impl.MemorySelf;
import automenta.netention.value.string.StringIs;
import java.awt.Color;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import redstone.xmlrpc.XmlRpcFault;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


public class Community extends MemorySelf {

    Map<String, Agent> agents = new ConcurrentHashMap();
    public List<String> agentsToInvestigate = Collections.synchronizedList(new LinkedList());
            
    Map<String, Set<String>> queries = new ConcurrentHashMap<>();

    private TwitterChannel tc = null;
    private WordpressChannel blog = null;
    public EMailChannel email = null;

    public static long ms(double h, double m, double s, double ms) {
        return (long)(ms + s * 1000.0 + m * (1000.0*60.0) + h * (1000.0*60.0*60.0));
    }
    
    private String postSummariesToEmail = null;
    
    boolean emitToTwitter = true;

    @Deprecated boolean includeReportURL = false;

    boolean isGeo = false;
    double geoLat, geoLng, geoRad;
    

    
    @Deprecated public static int getKeywordCount(String haystack, String needle) {
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
    
    
//    public String getScore(int num, final Date when, long minRepeatAgentTime, final String keys) {
//        List<String> a = new ArrayList(agents.keySet());
//        Collections.sort(a, new Comparator<String>() {
//            @Override public int compare(String a, String b) {                                
//                return Double.compare(getAgent(b).getScore(classifier, when, keys), getAgent(a).getScore(when, keys));
//            }                
//        });
//
//        String p = "";
//        for (String x : a) {
//
//            Agent ag = getAgent(x);
//            Date lc = ag.lastContacted;
//            if (lc!=null) {
//                if (when.getTime() - lc.getTime() < minRepeatAgentTime * 1000) {
//                    continue;
//                }
//            }
//            
//            ag.lastContacted = when;
//
//            System.out.println(Arrays.asList(keys) + ": SCORE=" + x + " " + getAgent(x).getScore(when, keys) + " in  " + getAgent(x).details.size() + " messages\n  " + getAgent(x).details.toString());
//            p += "@" + x.split("/")[1] + " ";
//            
//            num--;
//            if (num == 0)
//                break;
//        }
//
//        return p.trim();
//        
//    }
    

    
    public static String oneOf(String... x) {
        int p = (int)Math.floor(Math.random() * x.length);
        return x[p];
    }
    
    
    
    protected void emitTweet(String message) {
        message = message.trim();
        
        System.out.println("TWEETING: " + message);

        if (emitToTwitter)
            tc.updateStatus(message);        
    }
    
    protected void emitReport(String Title, String Content) {
        //Blogger
        if (postSummariesToEmail!=null) {
            try {
                email.sendMessage(new NMessage(Title, email.getFrom(), postSummariesToEmail, new Date(), Content));
            } catch (Exception ex) {
                Logger.getLogger(Community.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (blog!=null) {
            //Wordpress
            String reportURL = "";
            try {
                reportURL = blog.newPost(Title, Content);
            } catch (XmlRpcFault ex) {
                Logger.getLogger(Community.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

   public void runAnalyzeUsers(long analysisPeriod, long dontReinvestigate, long waitForNextQueries, int refreshAfterAnalysisCycles) {
        int k = 1;                     
        
        System.out.println("Resetting after every: " + ((float)(refreshAfterAnalysisCycles * analysisPeriod)/((float)Community.ms(1,0,0,0)) ) + " hours" ) ;
        
        Date now = new Date();
        while (true) {

            if (agentsToInvestigate.size()  == 0) {
                for (String p : queries.keySet()) {
                    final Set<String> al = queries.get(p);
                    try {
                        //System.out.println("Keyword search: " + p);
                        
                        List<Detail> tw = isGeo ? TwitterChannel.getTweets(p, geoLat, geoLng, geoRad) : TwitterChannel.getTweets(p);
                        
                        for (Detail d : tw) {
                            String a = d.getValue(StringIs.class, NMessage.from).getValue();
                            //addMentions(d);
                            boolean existing = agents.containsKey(a);
                            getAgent(a).add(d);
                            al.add(a);
                            if (existing) {
                                if (getAgent(a).lastUpdated.getTime() - now.getTime() > dontReinvestigate)
                                    if (!agentsToInvestigate.contains(a))
                                        agentsToInvestigate.add(a);
                            }
                            else {
                                if (!agentsToInvestigate.contains(a))
                                    agentsToInvestigate.add(a);                                
                            }
                        }


                    } catch (Exception ex) {
                        Logger.getLogger(Community.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }

            }

            if (agentsToInvestigate.size() > 0) {
                Collections.sort(agentsToInvestigate, new Comparator<String>() {
                    @Override public int compare(String o1, String o2) {
                        return Integer.compare(o1.hashCode(), o2.hashCode());
                    }                    
                }); 
                
                final String s = agentsToInvestigate.get(0);
                //System.out.println("Investigating: " + s);
                getAgent(s).update(tc);
                //getAgent(s).print(classifier);
                agentsToInvestigate.remove(s);
            }
            else {
                System.out.println("No more agents to investigate... pausing before querying again");
                try {
                    Thread.sleep(waitForNextQueries);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Community.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            try {
                Thread.sleep(analysisPeriod);
            } catch (InterruptedException ex) {
                Logger.getLogger(Community.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            if ((k % refreshAfterAnalysisCycles) == 0) {
                System.out.println("RESTARTING with FRESH DATA");
                agents.clear();
                agentsToInvestigate.clear();                
            }
            k++;
        }
    }

    public static String getUserString(Collection<String> c) {
        String p = "";
        for (String s : c)
            p += s + " ";
        return p.trim();
    }

    public static String getColor(float r, float g, float b) {
        r = Math.max(0, Math.min(r, 1.0f));
        g = Math.max(0, Math.min(g, 1.0f));
        b = Math.max(0, Math.min(b, 1.0f));
        
        Color c = new Color(r, g, b);
        String rgb = Integer.toHexString(c.getRGB());
        return "#" + rgb.substring(2, rgb.length());        
    }
    
    
    
    public Community() throws Exception {
        super();
        
        
    }
    
    public void setTwitter(String key) {
        this.tc = new TwitterChannel();
        this.tc.setKey(key);        
    }
    
    public void setPostEMail(String address, String server, String smtpServer, String username, String password, String postTo) {
        this.email = new EMailChannel(address, server, smtpServer, username, password);
        this.postSummariesToEmail = postTo;
    }
    
    public void setPostWordpress(String server, String username, String password) {
        try {
            this.blog = new WordpressChannel(server, username, password);
        } catch (Exception ex) {
            Logger.getLogger(Community.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void start(final long analysisPeriod, final long dontReinvestigate, final long waitForNextQueries, final int refreshAfterAnalysisCycles) {

        queue(new Runnable() {
            @Override public void run() {
                runAnalyzeUsers(analysisPeriod, dontReinvestigate, waitForNextQueries, refreshAfterAnalysisCycles);
            }            
        });
        
        //new SwingWindow(new CommunityBrowser(this), 800, 600, true);
        
    }
    
    public void setGeo(double lat, double lng, double rad) {
        this.isGeo = true;
        this.geoLat = lat;
        this.geoLng = lng;
        this.geoRad = rad;
    }
    public void addHappySad(double minTweetPeriod, double delay) {
        new HappySad(this, (long)minTweetPeriod, (long)delay);       
    }
    public void addRichPoor(long minTweetPeriod, long delay) {
        new RichPoor(this, minTweetPeriod, delay);       
    }
    
        
}
