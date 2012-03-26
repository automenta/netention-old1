/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package afxdeadcode;

import automenta.netention.Detail;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author SeH
 */
public abstract class PolarTagMatcher implements Runnable {
    private final long phaseSeconds;
    private final long period;
    long focusMinutes = 3 * 60;
    NGramClassifier a;
    NGramClassifier b;
    private final ConcurrentSkipListSet<String> localAgents;
    private final String catA;
    private final String catB;
    protected int numAAgents = 2;
    protected int numBAgents = 2;
    private final Community community;
    long dontReuseAgentUntil = 60 * 60 * 6; //in seconds

    public PolarTagMatcher(Community c, String catA, String catB, long period, long phaseSeconds) {
        this.community = c;
        this.period = period;
        this.phaseSeconds = phaseSeconds;
        this.catA = catA;
        this.catB = catB;
        a = NGramClassifier.load(catA);
        b = NGramClassifier.load(catB);
        localAgents = new ConcurrentSkipListSet<String>();
        for (String q : getSeedQueries()) {
            community.queries.put(q, localAgents);
        }
        c.queue(this);
    }

    public abstract String[] getSeedQueries();

    public void run() {
        try {
            Thread.sleep(phaseSeconds);
        } catch (InterruptedException ex) {
            Logger.getLogger(Community.class.getName()).log(Level.SEVERE, null, ex);
        }
        while (true) {
            operate();
            try {
                Thread.sleep(period);
            } catch (InterruptedException ex) {
                Logger.getLogger(Community.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public String getTitle(Collection<String> sa, Collection<String> sb) {
        return catA + " (" + Community.getUserString(sa) + ") vs. " + catB + " (" + Community.getUserString(sb) + ")";
    }

    public abstract String getTweet(String sa, String sb);

    protected void operate() {
        Collection<String> sa = getMost(localAgents, numAAgents, dontReuseAgentUntil, a, b);
        Collection<String> sb = getMost(localAgents, numBAgents, dontReuseAgentUntil, b, a);
        if (!((sa.size() == 0) || (sb.size() == 0))) {
            String happyAuthorsStr = Community.getUserString(sa);
            String sadAuthorsStr = Community.getUserString(sb);
            //emit(happyAuthors + " seem #happy. " + oneOf("So please help", "Will you help") +  " " + sadAuthors + " who seem #sad ? " + oneOf("#Kindness", "#Health", "#Wisdom", "#Happiness"));
            String richReport = getReport(sa, a);
            String poorReport = getReport(sb, b);
            final String Title = getTitle(sa, sb);
            final String Content = richReport + "<br/>" + poorReport;
            community.emitReport(Title, Content);
            //TWEET
            community.emitTweet(getTweet(happyAuthorsStr, sadAuthorsStr));
        }
    }

    public String getReport(Collection<String> authors, NGramClassifier classifier) {
        StringBuilder s = new StringBuilder();
        final String key = classifier.getName();
        s.append("<center><h1>" + key + "</h1></center>");
        for (String a : authors) {
            s.append("<p><h1>" + a + " " + key + "?</h1></p>");
            Agent ax = community.getAgent("twitter.com/" + a.substring(1)); //TODO clumsy
            List<Detail> detailsByTime = ax.getDetailsByTime2();
            double minScore = -1;
            double maxScore = -1;
            for (Detail d : detailsByTime) {
                float age = (float) ax.getAgeFactor(d, focusMinutes);
                float score = (float) ax.getScore(classifier, ax.lastUpdated, d) * age;
                if (minScore == -1) {
                    minScore = score;
                    maxScore = score;
                }
                if (minScore > score) {
                    minScore = score;
                }
                if (maxScore < score) {
                    maxScore = score;
                }
            }
            for (Detail d : detailsByTime) {
                float score = 0.5F;
                float age = (float) ax.getAgeFactor(d, focusMinutes);
                if (minScore != maxScore) {
                    score = (float) ax.getScore(classifier, ax.lastUpdated, d) * age; //TODO repeats with above, use function
                    score = (float) ((score - minScore) / (maxScore - minScore));
                }
                float tc = (float) Math.min(1.0F - age, 0.3F);
                String style = "color: " + Community.getColor(tc, tc, tc) + ";background-color: " + Community.getColor(1.0F, (1.0F - score) / 2.0F + 0.5F, (1.0F - score) / 2.0F + 0.5F) + ";font-size:" + Math.max(100, (int) ((1.0 + (score / 2.0)) * 100.0)) + "%;";
                s.append("<div style='" + style + ";margin-bottom:0;' >" + d.getName() + " <i>(@" + d.getWhen().toString() + ")</i></div>");
            }
        }
        return s.toString();
    }

    public Collection<String> getMost(Collection<String> agents, int num, long minRepeatAgentTime, final NGramClassifier classifierA, final NGramClassifier classifierB) {
        List<String> sortedAgents = new ArrayList(agents);
        final Map<String, Double> scores = new HashMap();
        for (String s : agents) {
            Agent ss = community.getAgent(s);
            Date aW = ss.lastUpdated;
            double ak = ss.getScore(classifierA, aW, focusMinutes);
            double bk = ss.getScore(classifierB, aW, focusMinutes);
            scores.put(s, ak / bk);
        }
        Collections.sort(sortedAgents, new Comparator<String>() {

            @Override
            public int compare(String a, String b) {
                double A = scores.get(a);
                double B = scores.get(b);
                return Double.compare(A, B);
            }
        });
        final Date now = new Date();
        List<String> p = new LinkedList();
        for (String x : sortedAgents) {
            Agent ag = community.getAgent(x);
            Date lc = ag.lastContacted;
            if (lc != null) {
                if (now.getTime() - lc.getTime() < minRepeatAgentTime * 1000) {
                    continue;
                }
            }
            if (ag.details.size() == 0) {
                continue;
            }
            ag.lastContacted = now;
            Date when = ag.lastUpdated;
            System.out.println(classifierA.getName() + " : SCORE=" + x + " " + scores.get(x));
            p.add("@" + x.split("/")[1]);
            num--;
            if (num == 0) {
                break;
            }
        }
        return p;
    }
    
}
