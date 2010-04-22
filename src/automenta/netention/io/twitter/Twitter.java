/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.io.twitter;

import automenta.netention.Detail;
import automenta.netention.Mode;
import automenta.netention.Self;
import automenta.netention.impl.MemoryDetail;
import automenta.netention.io.DetailSource;
import automenta.netention.io.SelfPlugin;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import twitter4j.Status;
import twitter4j.TwitterException;

/**
 *
 * @author seh
 */
public class Twitter implements SelfPlugin, DetailSource {
    /** detailID -> details */
    private Map<String, Detail> details = new HashMap();

    twitter4j.Twitter t = new twitter4j.Twitter();
    
    public Twitter() {
        super();
        addPublicTimeline();
    }
    
    @Override
    public void start(Self self) throws Exception {
    }

    @Override
    public void stop() {
    }

    @Override
    public Iterator<Detail> iterateDetails() {
        return details.values().iterator();
    }

    @Override
    public Detail getDetail(String id) {
        return details.get(id);
    }

    public void addDetail(Detail d) {
        details.put(d.getID(), d);
    }

//    public Detail getUser(User u) {
//
//    }

    public Detail getStatusDetail(Status s) {
        MemoryDetail md = new MemoryDetail(s.getText(), Mode.Real, "Message");
        md.setIconURL(s.getUser().getProfileImageURL().toString());
        return md;
    }

    private void addPublicTimeline() {
        try {
            for (Status s : t.getPublicTimeline()) {
                addDetail(getStatusDetail(s));
            }
        }
        catch (Exception te) {
            System.err.println(te);
        }

    }


}
