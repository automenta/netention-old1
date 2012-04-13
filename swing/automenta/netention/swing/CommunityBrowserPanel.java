package automenta.netention.swing;

import automenta.netention.Detail;
import automenta.netention.Mode;
import automenta.netention.Self;
import automenta.netention.irc.XMPPChannel;
import automenta.netention.swing.widget.ItemTreePanel;
import java.awt.BorderLayout;
import java.util.Collection;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.tree.TreeModel;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;

/**
 *
 * @author me
 */
public class CommunityBrowserPanel extends JPanel implements RosterListener {
    private final JSplitPane split;
    private final JPanel sidebar;
    private final JPanel content;
    private final ItemTreePanel index;
    private final XMPPChannel xmpp;
    private final Self self;

    public CommunityBrowserPanel(final Self self, SelfSession session) throws XMPPException {
        super(new BorderLayout());
        
        this.self = self;
        
        split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        
        sidebar = new JPanel(new BorderLayout());
        content = new JPanel(new BorderLayout());
        
        split.setLeftComponent(sidebar);
        split.setRightComponent(content);

        index = new ItemTreePanel(self) {

            @Override
            public void onDoubleLeftClick(Object item) {
            }

            @Override
            public TreeModel getModel() {
                return new TypeTreeModel(self, true);
            }
            
        };
        
        sidebar.add(new JScrollPane(index), BorderLayout.CENTER);
        
        split.setDividerLocation(0.3);
        
        add(split, BorderLayout.CENTER);
        
        xmpp = new XMPPChannel();
        xmpp.getRoster().addRosterListener(this);

        refreshIndex();
    }

    public Detail getUserDetail(String userid) {
        return self.getDetail("xmpp:" + userid);
    }
    
    protected void refreshIndex() {
        for (RosterEntry r : xmpp.getRoster().getEntries()) {
            if (getUserDetail(r.getUser()) == null) {
                Detail md = new Detail("xmpp:" + r.getUser(), r.getName(), Mode.Real, "Person");
                self.addDetail(md);
            }
        }
        index.refresh();
    }
    
    @Override
    public void entriesAdded(Collection<String> clctn) {
        refreshIndex();
    }

    @Override
    public void entriesUpdated(Collection<String> clctn) {
        refreshIndex();
    }

    @Override
    public void entriesDeleted(Collection<String> clctn) {
        refreshIndex();
    }

    @Override
    public void presenceChanged(Presence prsnc) {
    }
 
    
}
