/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention;

import flexjson.JSONSerializer;
import java.io.Serializable;
import java.util.Date;

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
    public String tags;

    public NMessage() {
    }
    
    public NMessage(String id, String from, String to, Date when, String subject, String content, String tags) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.when = when;
        this.subject = subject;
        this.content = content;
        this.tags = tags;
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
        return new JSONSerializer().serialize(this).toString();
    }

    public String getTags() {
        return tags;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }


    

    
    
    
    
}
