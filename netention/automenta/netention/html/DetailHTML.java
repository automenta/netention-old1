/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.html;

import automenta.netention.Detail;
import automenta.netention.Self;

/**
 *
 * @author me
 */
public interface DetailHTML {

    public String getHTML(Self s, Detail d, boolean includeJSON);
    
}
