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
    public String getHTML(final Self s, final Detail d, boolean includeJSON) {
        StringBuffer x = new StringBuffer("");
        x.append("<div>");
        x.append("<h1>" + d.getName().replaceAll("\n", "<br/>") + "</h1>");
        
        List<String> pat = d.getPatterns();
        if (!pat.isEmpty()) {
            x.append("<p><b>Patterns: </b>");
            int c = 1;
            for (String t : pat) {
                Pattern pt = s.getPattern(t);
                x.append(pt.getName());
                if (c!=pat.size())
                    x.append(", ");
                c++;
            }
            x.append("</p>");
        }
        
        for (PropertyValue pv : d.getValues()) {
            x.append("<p>" + pv.toHTML(s, this) + "</p>");
        }
        if (includeJSON)
            x.append("<br/><pre>" + Self.toJSON(d) + "</pre>");
        
        x.append("</div>");
        return x.toString();
    }

}
