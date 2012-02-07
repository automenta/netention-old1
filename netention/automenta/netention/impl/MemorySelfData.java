/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.impl;

import automenta.netention.Detail;
import automenta.netention.Pattern;
import automenta.netention.Property;
import java.util.Collection;
import java.util.Date;

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

    public MemorySelfData(MemorySelf s) {
        this(s.getID(), s.details.values(), s.patterns.values(), s.properties.values());
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

}
