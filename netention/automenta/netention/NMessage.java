/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention;

import automenta.netention.value.Comment;
import automenta.netention.value.string.StringIs;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author seh
 */
public class NMessage extends Detail /*implements Serializable*/ {
    public static final String MessagePattern = "message";
    public static final String StatusPattern = "status";
    
    public static final String to = "message.to";
    public static final String from = "message.from";
    //public static final String content = "message.content";
    public static String mentions = "message.mentions";

    public static boolean hasRecipient(Detail d, String id) {
        StringIs s = d.getValue(StringIs.class, to);
        if (s!=null) {
            if (s.getValue().equals(id)) {
                return true;
            }
        }
        return false;
    }
    
//    public String id;
//    public String from;
//    public String to;
//    public Date when;
//    public String subject;
//    public String content;
//    public Set<String> tags;
//    private String image;

    public NMessage() {
        super();
    }
    
    public NMessage(String subject, String from, String to, Date when, String... content) {
        super(subject, Mode.Real, MessagePattern, "Event" );
        setID(subject + "@" + when);
        setFrom(from);
        setTo(to);
        setWhen(when);
        setContent(content);
    }

    public String getStringValue(String propID) {
        StringIs p = getValue(StringIs.class, propID);
        if (p!=null)
            return p.getValue();
        else
            return "";
    }
    
    public synchronized void setStringValue(String propID, String... v) {
        removeAllValues(propID);
        for (String x : v)
            add(propID, new StringIs(x));
    }
    
    public String getContent() {
        StringBuilder sb = new StringBuilder();
        for (PropertyValue n : getValues()) {
            if (n instanceof Comment) {
                sb.append("<p>" + ((Comment)n ).getText() + "</p>" );
            }
        }
        return sb.toString();
    }

    public String getFrom() {
        return getStringValue(from);
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

//    @Override
//    public String toString() {
//        return new JSONSerializer().include("tags").serialize(this).toString();
//    }

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

    public void setContent(String... content) {
        //setStringValue(this.content, content);
        for (String c : content)
            add(new Comment(c));
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
