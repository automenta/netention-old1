/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.io;

import automenta.netention.RODetail;
import java.util.Iterator;

/**
 *
 * @author seh
 */
public interface DetailSource {

    public Iterator<RODetail> iterateDetails();
    
}
