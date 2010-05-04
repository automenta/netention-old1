/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.plugin.finance;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections15.SetUtils;

/**
 * tagged indexed set of businesses
 * maybe should extend an abstract 'tagset' class...
 * @author seh
 */
public class Portfolio {

    public final Map<String, PublicBusiness> taggedBusiness = new HashMap();

    public Portfolio() {
        super();
    }

    public void addBusiness(String tag, PublicBusiness... biz) {
        for (PublicBusiness pb : biz) {
            taggedBusiness.put(tag, pb);
        }
    }

    public Set<PublicBusiness> getBusinesses() {
        Set<PublicBusiness> s = new HashSet(taggedBusiness.values());
        return SetUtils.unmodifiableSet( s );
    }
    
}
