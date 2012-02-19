/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.value;

import automenta.netention.PropertyValue;
import automenta.netention.Self;
import automenta.netention.html.DetailHTML;

/**
 *
 */
public class Comment extends PropertyValue {

    private String text;
    
    public Comment() {
        this("");
    }
    
    public Comment(String p) {
        super();
        setProperty("");
        text = p;        
    }

    public Comment setText(String text) {
        this.text = text;
        return this;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toHTML(Self s, DetailHTML h) {
        return "<p>" + text + "</p>";
    }
    

    
    
}
