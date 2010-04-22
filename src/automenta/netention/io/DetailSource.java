/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.io;

import automenta.netention.Detail;
import java.util.Iterator;

/**
 *
 * @author seh
 */
public interface DetailSource {

    //TODO use RODetail
    public Iterator<Detail> iterateDetails();

    /** returns null if non-existent in this source */
    public Detail getDetail(String id);
    
}
