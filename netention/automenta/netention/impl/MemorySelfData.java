/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.impl;

import automenta.netention.*;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import flexjson.JSONDeserializer;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Collection;
import java.util.Date;
import org.apache.commons.collections15.IteratorUtils;

/**
 *
 * @author seh
 */
public class MemorySelfData {
    public String author;
    public Collection<Node> detailList;
    public Collection<Pattern> patternList;
    public Collection<Property> propertyList;
    public Date saved;

    public MemorySelfData(String author, Collection<Node> details, Collection<Pattern> patterns, Collection<Property> properties) {
        this.author = author;
        this.detailList = details;
        this.patternList = patterns;
        this.propertyList = properties;
        this.saved = new Date();
    }

    public MemorySelfData(final Self s) {
        this(s.getID(), IteratorUtils.toList( s.iterateNodes() ), 
                Collections2.transform(s.getPatterns(), new Function<String, Pattern>() {

                    @Override
                    public Pattern apply(String f) {
                        return s.getPattern(f);
                    }
                    
                }), 
                Collections2.transform(s.getProperties(), new Function<String, Property>() {
                    @Override
                    public Property apply(String f) {
                        return s.getProperty(f);
                    }                    
                })
         );
    }

    public MemorySelfData() {
    }

    public Date getSaved() {
        return saved;
    }

    public void setSaved(Date saved) {
        this.saved = saved;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setDetailList(Collection<Node> detailList) {
        this.detailList = detailList;
    }

    public void setPatternList(Collection<Pattern> patternList) {
        this.patternList = patternList;
    }

    public void setPropertyList(Collection<Property> propertyList) {
        this.propertyList = propertyList;
    }

    public String getAuthor() {
        return author;
    }

    public Collection<Node> getDetailList() {
        return detailList;
    }

    public Collection<Pattern> getPatternList() {
        return patternList;
    }

    public Collection<Property> getPropertyList() {
        return propertyList;
    }

    public void mergeTo(Self s) {
        if (getDetailList()!=null)
            for (Node d : getDetailList()) {
                if (d instanceof Detail)
                    s.mergeFromDetail((Detail)d);
            }
        
        if (getPropertyList()!=null)       
            for (Property r : getPropertyList()) {
                s.mergeFromProperty(r);
            }
        
        if (getPatternList()!=null)
            for (Pattern p : getPatternList()) {
                s.mergeFromPattern(p);
            }
    }

    public static MemorySelfData loadJSON(String path) throws FileNotFoundException {
        JSONDeserializer<MemorySelfData> msd = new JSONDeserializer<MemorySelfData>();
        MemorySelfData m = msd.deserialize(new FileReader(path));
        return m;
    }
    
    
}
