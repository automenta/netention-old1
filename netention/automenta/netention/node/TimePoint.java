/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.node;

import automenta.netention.Node.AbstractNode;
import java.util.Date;

/**
 *
 * @author seh
 */
public class TimePoint extends AbstractNode {
    public final Date date;

    public TimePoint(Date d) {
        super("date:" + Long.toString(d.getTime()));
        this.date = d;
    }

    @Override
    public String toString() {
        return date.toString();
    }

}
