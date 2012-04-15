/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.index;

import automenta.netention.Pattern;
import automenta.netention.Property;
import automenta.netention.Self;
import automenta.netention.app.RunSelfBrowser;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
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

    public SchemaIndex(Self s) {
        this.self = s;

        update();
    }

    public synchronized void update() {
        try {
            IndexWriter w = new IndexWriter(index, config);

            //        addDoc(w, "Lucene in Action");
            //        addDoc(w, "Lucene for Dummies");
            //        addDoc(w, "Managing Gigabytes");
            //        addDoc(w, "The Art of Computer Science");
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
        doc.add(new Field("name", p.getName(), Field.Store.YES, Field.Index.ANALYZED));
        if (p.getDescription() != null) {
            doc.add(new Field("desc", p.getDescription(), Field.Store.YES, Field.Index.ANALYZED));
        }
        w.addDocument(doc);
    }

    public List<String> find(String queryStr, int maxHits) throws IOException, ParseException {

        // the "title" arg specifies the default field to use
        // when no field is explicitly specified in the query.
        Query q = new QueryParser(Version.LUCENE_36, "name", analyzer).parse(queryStr);

        // 3. search
        IndexReader reader = IndexReader.open(index);
        IndexSearcher searcher = new IndexSearcher(reader);
        TopScoreDocCollector collector = TopScoreDocCollector.create(maxHits, true);
        searcher.search(q, collector);
        ScoreDoc[] hits = collector.topDocs().scoreDocs;

        List<String> results = new LinkedList();
        
        // 4. display results
        //System.out.println("Found " + hits.length + " hits.");
        for (int i = 0; i < hits.length; ++i) {
            int docId = hits[i].doc;
            Document d = searcher.doc(docId);
            String propID = d.get("propertyID");
            String patternID = d.get("patternID");
            if (patternID != null) {
                results.add("pattern:" + patternID);
            }
            else {
                results.add("property:" + propID);                
            }
        }

        // searcher can only be closed when there
        // is no need to access the documents any more. 
        searcher.close();
        
        return results;
        
    }

    public static void main(String[] args) throws Exception {
        Self s = RunSelfBrowser.newDefaultSelf();
        SchemaIndex i = new SchemaIndex(s);
        System.out.println(i.find("built", 32));
    }
}
