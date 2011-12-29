/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.swing.widget.net;

import com.dan.jtella.ConnectedHostsListener;
import com.dan.jtella.HostsChangedEvent;
import com.kenmccrary.jtella.ConnectionData;
import com.kenmccrary.jtella.GNUTellaConnection;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

/**
 *
 * @author seh
 */
public class GnutellaStatusBar extends JPanel implements ConnectedHostsListener {

    final int incomingPort = 10001;
    private GNUTellaConnection connection;
    private final JLabel numConnectionsLabel;
    
    public GnutellaStatusBar() {
        super(new FlowLayout(FlowLayout.LEFT));
        
        final JToggleButton jtb = new JToggleButton("Gnutella Connect");
        jtb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (jtb.isSelected()) {
                    connect();
                }
                else {
                    disconnect();
                }
            }            
        });
        add(jtb);
        
        numConnectionsLabel = new JLabel();
        add(numConnectionsLabel);

        disconnect();        

    }
    
    public void connect() {
        ConnectionData cd = new ConnectionData();
        cd.setIncomingPort(incomingPort);
        
        try {
            connection = new GNUTellaConnection(cd);
        } catch (IOException ex) {
        }
      
        
        connection.addListener(this);
        //connection.getSearchMonitorSession(new TestReceiver());
        connection.start();

        //TODO this is temporary until we can add hosts via GUI
        connection.addConnection("localhost", 30056);
        
    }
    
    public void disconnect() {
        if (connection!=null) {
            connection.stop();
            connection = null;
        }
        numConnectionsLabel.setText("");
    }

    @Override
    public void hostsChanged(HostsChangedEvent hce) {
        
        numConnectionsLabel.setText(connection.getConnectionList().size() + " Gnutella connections");
        
    }
    
}
