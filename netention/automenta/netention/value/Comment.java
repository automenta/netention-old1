/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.value;

import automenta.netention.PropertyValue;

/**
 *
 */
public class Comment extends PropertyValue {

    private String text;
    
    public Comment() {
        super();
        setProperty("");
        text = "";
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
    

    
}
