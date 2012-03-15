/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.impl;

import automenta.netention.*;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.gson.Gson;
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
    public Collection<Detail> detailList;
    public Collection<Pattern> patternList;
    public Collection<Property> propertyList;
    public Date saved;

    public MemorySelfData(String author, Collection<Detail> details, Collection<Pattern> patterns, Collection<Property> properties) {
        this.author = author;
        this.detailList = details;
        this.patternList = patterns;
        this.propertyList = properties;
        this.saved = new Date();
    }
    
    public MemorySelfData(final Self s, boolean includeDetails, boolean includePatterns, boolean includeProperties) {
        this(s.getID(), 
                includeDetails ? s.getDetails() : null, 
                includePatterns ? Collections2.transform(s.getPatterns(), new Function<String, Pattern>() {

                    @Override
                    public Pattern apply(String f) {
                        return s.getPattern(f);
                    }
                    
                }) : null, 
                includeProperties ? Collections2.transform(s.getProperties(), new Function<String, Property>() {
                    @Override
                    public Property apply(String f) {
                        return s.getProperty(f);
                    }                    
                }) : null
         );
        
    }

    public MemorySelfData(final Self s) {
        this(s, true, true, true);
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

    public void setDetailList(Collection<Detail> detailList) {
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

    public Collection<Detail> getDetailList() {
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
                //System.out.println("merging: " + d + " " + d.getClass() + " " + d.getID());
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
        return new Gson().fromJson(new FileReader(path), MemorySelfData.class);
    }
    
    
}
