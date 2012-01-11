/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention;

import flexjson.JSONSerializer;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author seh
 */
public class NMessage implements Serializable {
    
    public String id;
    public String from;
    public String to;
    public Date when;
    public String subject;
    public String content;
    public Set<String> tags;
    private String image;

    public NMessage() {
    }
    
    public NMessage(String id, String from, String to, Date when, String subject, String content) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.when = when;
        this.subject = subject;
        this.content = content;
        this.tags = new HashSet();
    }

    public String getContent() {
        return content;
    }

    public String getFrom() {
        return from;
    }

    public String getId() {
        return id;
    }

    public String getSubject() {
        return subject;
    }

    public String getTo() {
        return to;
    }

    public Date getWhen() {
        return when;
    }

    @Override
    public String toString() {
        return new JSONSerializer().include("tags").serialize(this).toString();
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public void addTag(String p) {
        tags.add(p);
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setWhen(Date when) {
        this.when = when;
    }

    /** image url */
    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    
    
    
}
