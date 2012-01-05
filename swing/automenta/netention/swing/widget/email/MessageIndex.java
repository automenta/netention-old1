/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.swing.widget.email;

import automenta.netention.NMessage;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 *
 * @author seh
 */
public class MessageIndex implements Serializable {

    
    public List<NMessage> messages = new LinkedList();
    
    transient Map<String, NMessage> byId = new HashMap();
    
    
    public MessageIndex() {
    }
    
    public void addShutdownHook(final String filename) {

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    save(filename);
                } catch (Exception ex) {
                    System.err.println(ex);
                    ex.printStackTrace();
                }
            }
        }));
        
    }

    public static MessageIndex load(String filename) throws IOException {
        MessageIndex ec;
        try {
            FileReader fr = new FileReader(filename);
            ec = new JSONDeserializer<MessageIndex>().use(MessageIndex.class.toString(), MessageIndex.class).deserialize(fr);
            fr.close();
        }
        catch (IOException e) {
            ec = new MessageIndex();
        }
            
        ec.reindex();
        
        ec.addShutdownHook(filename);
        return ec;
    }

    public void save(String filename) throws IOException {
        FileWriter fw = new FileWriter(new File(filename));
        new JSONSerializer().include("messages").deepSerialize(this, fw);
        fw.close();
    }

    protected void index(final NMessage m) {
        byId.put(m.getId(), m);        
    }
    
    public void add(final NMessage m) {
        messages.add(m);
        index(m);
    }
    
    public void reindex() {
        byId.clear();
        for (final NMessage m : messages)
            index(m);
    }
    
    public NMessage id(String id) {
        return byId.get(id);
    }
    
    public Iterator<NMessage> iterate(Predicate<NMessage> p) {
        return Iterators.filter(iterateAll().iterator(), p);
    }
    
    public Iterable<NMessage> iterateAll() {
        return byId.values();
    }

    void addAll(Iterable<NMessage> m) {
        for (NMessage n : m) {
            add(n);
        }
    }
    
}
