/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package afxdeadcode;

import afxdeadcode.test.ClassifierTool;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.knallgrau.utils.textcat.FingerPrint;

/**
 *
 * @author SeH
 */
public class NGramClassifier implements Serializable {

    String name;
    String corpus = "";
    Category cat;

//    transient public Map<String, Double> avgBackground = new HashMap();
//    transient public Map<String, List<Double>> avgBackgroundSamples = new HashMap();
    static public String getContents(File aFile) {
        //...checks on aFile are elided
        StringBuilder contents = new StringBuilder();

        try {
            //use buffering, reading one line at a time
            //FileReader always assumes default encoding is OK!
            BufferedReader input = new BufferedReader(new FileReader(aFile));
            try {
                String line = null; //not declared within while loop
        /*
                 * readLine is a bit quirky : it returns the content of a line
                 * MINUS the newline. it returns null only for the END of the
                 * stream. it returns an empty String if two newlines appear in
                 * a row.
                 */
                while ((line = input.readLine()) != null) {
                    contents.append(line);
                    contents.append(System.getProperty("line.separator"));
                }
            } finally {
                input.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return contents.toString();
    }

    public static NGramClassifier load(String name) {
        String corp = getContents(new File("media/text/ngram." + name));
        NGramClassifier cc = new NGramClassifier(name);
        cc.setCorpus(corp);
        return cc;
    }

    public NGramClassifier(String name) {
        cat = new Category(name);
        this.name = name;
        update();
    }

    public void update() {
        cat.clear();
    }

//    public void calibrateNormal() throws Exception {
//        System.out.println("Calibrating normal levels");
//        
//        final int cycles = 8;
//        
//        for (String s : TwitterChannel.getPublicTweetStrings(cycles)) {
//            //addBackground(Agent.filterTweet(s));
//            addBackground(s);
//        }
//        
//        System.out.println("avg background distances: " + avgBackground);
//    }
//    
//    public void addBackground(String p) {
//                
//        if (avgBackgroundSamples == null) {
//            avgBackground = new HashMap();
//            avgBackgroundSamples = new HashMap();
//        }
//        
//        for (String c : corpii.keySet()) {
//            double dist = getDistance(p, c);
//            
////            if (c.equals("happy"))
//                //System.out.println(c + " " + p + " " + dist + " " + p.length() + " " + ( ((double)dist) / ((double)p.length()) ));
//
//            if (avgBackgroundSamples.get(c) == null)
//                avgBackgroundSamples.put(c, new LinkedList());
//            avgBackgroundSamples.get(c).add(dist);            
//        }
//        
//        //recompute avgBackground
//        for (String c : corpii.keySet()) {
//            double total = 0;
//            for (Double i : avgBackgroundSamples.get(c)) {
//                total += i;
//            }
//            double n = avgBackgroundSamples.size();
//            double v = total/n;
//            avgBackground.put(c, v);
//        }
//    }
//    public void addCategory(String x) {
//        if (!corpii.containsKey(x))
//            corpii.put(x, "");
//    }
//    public Category getCategory(String p) {
//        if (cats == null)
//            cats = new HashMap();
//        
//        if (cats.containsKey(p)) {
//            return cats.get(p);
//        }
//        Category c = new Category(p);
//        c.create(corpii.get(p));
//        cats.put(p, c);
//        return c;
//    }
    public void save(String path) throws Exception {
        //System.out.println("saving:\n" + corpii.toString());
        ObjectOutputStream ois = new ObjectOutputStream(new FileOutputStream(path));
        ois.writeObject(this);
        ois.close();
    }

    public void saveOnExit(final String path) {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    save(path);
                } catch (Exception ex) {
                    Logger.getLogger(ClassifierTool.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }));
    }

//    public Collection<String> categories() {
//        return corpii.keySet();
//    }
    public void setCorpus(String corpus) {
        this.corpus = corpus;
        cat.create(corpus);
    }

//    public Map<String, Double> analyzeC(String t, List<String> catCompared) {
//        Map<String, Integer> result = analyze(t, catCompared);
//        
//        Map<String, Double> d = new HashMap();
//        for (String x : result.keySet()) {
//            double nv = result.get(x) == 0 ? 1.0 : ((double)corpii.get(x).length()) / ((double)result.get(x));
//            d.put(x, nv);
//        }
//        
//        return d;
//        
//    }
    public double getDistance(String t) {
        FingerPrint fp = new FingerPrint();
        fp.create(t);

        //return ((double) fp.categorize(Arrays.asList(new FingerPrint[]{cat})).get(name)) / ((double) t.length());
        return ((double) fp.categorize(Arrays.asList(new FingerPrint[]{cat})).get(name)) / ((double) corpus.length());
    }
//    public double analyzeC(String t, String c) {
//        FingerPrint fp = new FingerPrint();
//        fp.create(t);
//
//        int d = fp.categorize(Arrays.asList(new FingerPrint[] { getCategory(c) }) ).get(c);
//        
//        return d == 0 ? 1.0 : ((double)corpii.get(c).length()) / ((double)d); // * ((double)t.length());
//    }
//
//    @Deprecated public Map<String, Double> analyzeNormalized(String t, List<String> catCompared) {
//        Map<String, Double> result = analyzeC(t, catCompared);
//        double maxDist = 0, minDist = -1;
//        for (Double ii : result.values()) {
//            if (maxDist < ii) maxDist = ii;
//            if (minDist == -1) minDist = ii;
//            else if (minDist > ii) minDist = ii;
//        }
//        
//        Map<String, Double> d = new HashMap();
//        if ((maxDist!=0) && (maxDist!=minDist)) {
//            for (String x : result.keySet()) {
//                double nv = 1.0 - ((double)(result.get(x) - minDist)) / ((double)maxDist - minDist);
//                d.put(x, nv);
//            }
//        }
//        
//        return d;
//        
//    }
//    public Map<String, Integer> analyze(String t, List<String> catCompared) {
//        FingerPrint fp = new FingerPrint();
//        fp.create(t);
//        
//        List<FingerPrint> ffp = new LinkedList();
//        for (Category cat : cats.values())
//            if (catCompared.contains(cat.getCategory()))
//                ffp.add(cat);
//        
//        return fp.categorize(ffp);
//    }
//
//    public double getAverageBackgroundDistance(String k) {
//        return avgBackground.get(k);
//    }

    public String getName() {
        return name;
    }
}
