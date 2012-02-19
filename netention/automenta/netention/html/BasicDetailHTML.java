/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.html;

import automenta.netention.Detail;
import automenta.netention.Pattern;
import automenta.netention.PropertyValue;
import automenta.netention.Self;
import java.util.List;

/**
 *
 * @author seh
 */
public class BasicDetailHTML implements DetailHTML {

    
    @Override
    public String getHTML(final Self s, final Detail d) {
        StringBuffer x = new StringBuffer("");
        x.append("<h1>" + d.getName() + "</h1><br/>");
        
        List<String> pat = d.getPatterns();
        if (!pat.isEmpty()) {
            x.append("<b>Patterns</b>");
            x.append("<ul>");
            for (String t : pat) {
                x.append("<li>");
                Pattern pt = s.getPattern(t);
                x.append(pt.getName());
                x.append("</li>");
            }
            x.append("</ul><br/>");
        }
        
        for (PropertyValue pv : d.getValues()) {
            x.append(pv.toHTML(s, this) + "<br/>");
        }
        x.append("<br/><pre>" + Self.toJSON(d) + "</pre>");
        x.append("");
        return x.toString();
    }

}
