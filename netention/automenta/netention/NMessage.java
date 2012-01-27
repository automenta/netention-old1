/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention;

import automenta.netention.impl.MemoryDetail;
import automenta.netention.value.string.StringIs;
import flexjson.JSONSerializer;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author seh
 */
public class NMessage extends MemoryDetail /*implements Serializable*/ {
    public static final String to = "message.to";
    public static final String from = "message.from";
    public static final String subject = "message.subject";
    public static final String content = "message.content";
    
//    public String id;
//    public String from;
//    public String to;
//    public Date when;
//    public String subject;
//    public String content;
//    public Set<String> tags;
//    private String image;

    public NMessage() {
    }
    
    public NMessage(String name, String from, String to, Date when, String subject, String content) {
        super(name, Mode.Real, "message" );
        
        setFrom(from);
        setTo(to);
        setWhen(when);
        setSubject(subject);
        setContent(content);
    }

    public String getStringValue(String propID) {
        StringIs p = getValue(StringIs.class, propID);
        if (p!=null)
            return p.getString();
        else
            return "";
    }
    
    public synchronized void setStringValue(String propID, String v) {
        removeAllValues(propID);
        addValue(propID, new StringIs(v));
    }
    
    public String getContent() {
        return getStringValue(content);
    }

    public String getFrom() {
        return getStringValue(from);
    }

    public String getSubject() {
        return getStringValue(subject);
    }

    public String getTo() {
        return getStringValue(to);
    }

    @Override
    public Date getWhen() {
        return new Date(getStringValue("message.when"));
    }

    /** image url */
    public String getImage() {
        return getStringValue("message.image");
    }

    @Override
    public String toString() {
        return new JSONSerializer().include("tags").serialize(this).toString();
    }

    public Set<String> getTags() {
        return decodeTagString(getStringValue("message.tags"));
    }

    public void setFrom(String from) {
        setStringValue(this.from, from);
    }

    @Override
    public int hashCode() {
        return getID().hashCode();
    }

    public void addTag(String p) {
        Set<String> s = getTags();
        s.add(p);
        setStringValue("message.tags", encodeTagString(s));
    }

    public void setTo(String to) {
        setStringValue(this.to, to);
    }

    public void setContent(String content) {
        setStringValue(this.content, content);
    }

    public void setSubject(String subject) {
        setStringValue(this.subject, subject);
    }

    public void setWhen(Date when) {
        setStringValue("message.when", when.toString());
    }

    public void setImage(String image) {
        setStringValue("message.image", image);
    }

    public static Set<String> decodeTagString(String stringValue) {
        String[] s = stringValue.split(",");
        Set<String> a = new HashSet();
        for (String x : s)
            a.add(x);
        return a;
    }

    public static String encodeTagString(Set<String> s) {
        String x = "";
        for (String p : s)
            x += p + ",";
        return x;
    }
     
    
}
