/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.io;

import automenta.netention.Self;

/**
 *
 * @author seh
 */
public interface SelfPlugin {

    public void start(Self self) throws Exception;
    public void stop();

}
