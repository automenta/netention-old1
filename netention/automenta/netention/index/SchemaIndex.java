/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.index;

import automenta.netention.Pattern;
import automenta.netention.Property;
import automenta.netention.Self;
import automenta.netention.app.RunSelfBrowser;
import automenta.netention.value.set.SelectionProp;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

/**
 *
 * @author me
 */
public class SchemaIndex {
    //http://www.lucenetutorial.com/lucene-in-5-minutes.html

    private final Self self;
    final StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);
    final Directory index = new RAMDirectory();
    final IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_36, analyzer);


    public enum SchemaComponent {
        Pattern, Property
    }
    
    public static class SchemaResult {
        public final SchemaComponent type;
        public final String id;
        public final double score;

        public SchemaResult(SchemaComponent type, String id, double score) {
            this.type = type;
            this.id = id;
            this.score = score;
        }

        @Override
        public String toString() {
            return (type == SchemaComponent.Pattern ? "Is" : "Has") + " " + id /*+ ":" + score*/;
        }
        
        public String toString(Self s) {
            return (type == SchemaComponent.Pattern ? "Is" : "Has") + " " + 
                    (type == SchemaComponent.Pattern ? s.getPattern(id).getName() : s.getProperty(id).getName() ) /*+ ":" + score*/;
        }
        
        
    }
    
    public SchemaIndex(Self s) {
        this.self = s;

        update();
    }

    public synchronized void update() {
        try {
            IndexWriter w = new IndexWriter(index, config);

            for (String p : self.getPatterns()) {
                addPattern(w, self.getPattern(p));
            }
            for (String p : self.getProperties()) {
                addProperty(w, self.getProperty(p));
            }
            w.close();
        } catch (CorruptIndexException ex) {
            Logger.getLogger(SchemaIndex.class.getName()).log(Level.SEVERE, null, ex);
        } catch (LockObtainFailedException ex) {
            Logger.getLogger(SchemaIndex.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SchemaIndex.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    protected void addPattern(IndexWriter w, Pattern p) throws CorruptIndexException, IOException {
        Document doc = new Document();
        doc.add(new Field("patternID", p.getID(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field("name", p.getName(), Field.Store.YES, Field.Index.ANALYZED));
        if (p.getDescription() != null) {
            doc.add(new Field("desc", p.getDescription(), Field.Store.YES, Field.Index.ANALYZED));
        }
        w.addDocument(doc);
    }

    protected void addProperty(IndexWriter w, Property p) throws CorruptIndexException, IOException {
        Document doc = new Document();
        doc.add(new Field("propertyID", p.getID(), Field.Store.YES, Field.Index.NOT_ANALYZED));
        
        Field nf = new Field("name", p.getName(), Field.Store.YES, Field.Index.ANALYZED);
        nf.setBoost(2.0f); //name more important than desc
        doc.add(nf);
        
        String desc = p.getDescription();
        if (desc == null) desc = "";
        if (p instanceof SelectionProp) {
            SelectionProp sp = (SelectionProp)p;
            for (String o : sp.getOptions())
                desc += " " + o;
        }
        
        doc.add(new Field("desc", desc, Field.Store.YES, Field.Index.ANALYZED));
        w.addDocument(doc);
    }

    public List<SchemaResult> find(String queryStr, int maxHits) throws IOException, ParseException {

        // http://today.java.net/pub/a/today/2003/11/07/QueryParserRules.html
        Query q = new MultiFieldQueryParser(Version.LUCENE_36, new String[] { "name", "desc" }, analyzer).parse(queryStr);

        // 3. search
        IndexReader reader = IndexReader.open(index);
        IndexSearcher searcher = new IndexSearcher(reader);
        TopScoreDocCollector collector = TopScoreDocCollector.create(maxHits, true);
        searcher.search(q, collector);
        ScoreDoc[] hits = collector.topDocs().scoreDocs;

        List<SchemaResult> results = new LinkedList();
        
        // 4. display results
        //System.out.println("Found " + hits.length + " hits.");
        for (int i = 0; i < hits.length; ++i) {
            int docId = hits[i].doc;
            Document d = searcher.doc(docId);
            String propID = d.get("propertyID");
            String patternID = d.get("patternID");
            double score = hits[i].score;
            if (patternID != null) {
                results.add(new SchemaResult(SchemaComponent.Pattern, patternID, score));
            }
            else {
                results.add(new SchemaResult(SchemaComponent.Property, propID, score));
            }
        }

        // searcher can only be closed when there
        // is no need to access the documents any more. 
        searcher.close();
        
        return results;
        
    }

    public List<SchemaResult> mergeResults(List<SchemaResult> raw) {
        Map<String, Double> patterns = new HashMap();
        Map<String, Double> properties = new HashMap();
        
        for (SchemaResult s : raw) {
            if (s.type == SchemaComponent.Pattern) {
                double newScore = s.score;
                if (patterns.containsKey(s.id)) {
                    double existingScore = patterns.get(s.id);
                    newScore += existingScore;
                }
                patterns.put(s.id, newScore);
            }
            else if (s.type == SchemaComponent.Property) {
                double newScore = s.score;
                if (properties.containsKey(s.id)) {
                    double existingScore = properties.get(s.id);
                    newScore += existingScore;
                }
                properties.put(s.id, newScore);
            }
        }
        
        
        //TODO
        List<SchemaResult> lr = new ArrayList(patterns.size() + properties.size());
        for (Entry<String, Double> e : patterns.entrySet())
            lr.add(new SchemaResult(SchemaComponent.Pattern, e.getKey(), e.getValue()));
        for (Entry<String, Double> e : properties.entrySet())
            lr.add(new SchemaResult(SchemaComponent.Property, e.getKey(), e.getValue()));
        
        Collections.sort(lr, new Comparator<SchemaResult>() {

            @Override
            public int compare(SchemaResult a, SchemaResult b) {
                return Double.compare(b.score, a.score);
            }
            
        });
        return lr;
    }

    public List<SchemaResult> getSuggestions(String i) {
        String s = "";
        for (int x = 0; x < i.length(); x++) {
            char c = i.charAt(x);
            if (c == '\n')
                s += " ";
            else if (Character.isAlphabetic(c) || (c == ' '))
                s += c;
        }
        
        List<SchemaResult> l = new LinkedList();

        if (s.length() == 0)
            return l;
                    
        try {
            l.addAll(find(s, 16));
            
            String[] ss = s.split(" ");
            for (String x : ss) {
                x = x.trim();
                if (x.length() > 1)
                    l.addAll(find(x, 4));
            }
            
        } catch (IOException ex) {
            Logger.getLogger(SchemaIndex.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(SchemaIndex.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mergeResults(l);
    }
    
    public static void main(String[] args) throws Exception {
        Self s = RunSelfBrowser.newDefaultSelf();
        SchemaIndex i = new SchemaIndex(s);
        System.out.println(i.find("male", 32));
    }
}
