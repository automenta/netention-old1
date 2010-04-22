/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.io.twitter;

import automenta.netention.RODetail;
import automenta.netention.Self;
import automenta.netention.io.DetailSource;
import automenta.netention.io.SelfPlugin;
import java.util.Iterator;

/**
 *
 * @author seh
 */
public class Twitter implements SelfPlugin, DetailSource {

    public Twitter() {
    }
    
    @Override
    public void start(Self self) throws Exception {
    }

    @Override
    public void stop() {
    }

    @Override
    public Iterator<RODetail> iterateDetails() {
        return null;
    }

}
