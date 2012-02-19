/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.gnutella;

 
import com.dan.jtella.ConnectedHostsListener;
import com.dan.jtella.HostsChangedEvent;
import com.kenmccrary.jtella.*;


/**
 *  An example class for showing how to execute search queries on the
 *  GNUTella network using JTella. The application expects two command
 *  line parameters, the host name, and the port the servant is listening on
 *
 */
public class SearchExample
{
    
  public static int incomingPort = 8998;
    
  private GNUTellaConnection conn;
  
  /**
   *  Constructs the example given a started network connection
   *
   */
  public SearchExample(GNUTellaConnection networkConnection)
  {
    this.conn = networkConnection;
  }

  /**
   *  Entry point for the search example application, host and port number are
   *  expected arguments
   *
   */
  public static void main(String[] args)
  {
    System.out.println("<--- JTella Search Example running --->\n");


    try
    {
      System.out.println("Connecting to Gnutella Network...");
      
      //-------------------------------------------------------------
      // Start a network connection, wait, then execute searches
      // A proper application would check if Node connections
      // exist prior to searching
      //-------------------------------------------------------------
      
      ConnectionData cd = new ConnectionData();
      cd.setIncomingPort(incomingPort);
      GNUTellaConnection c = new GNUTellaConnection(cd);
      
      c.start();
      
      c.addListener(new ConnectedHostsListener() {

                @Override
                public void hostsChanged(HostsChangedEvent hce) {
                    System.out.println("HCE: " + hce.getSource());
                    Object o = hce.getSource();
                    if (o instanceof NodeConnection) {
                        NodeConnection nc = (NodeConnection)o;
                        System.out.println("  " + nc.getConnectedServant()); 
                    }
                }
          
      });
      c.addConnection("localhost", 30056);
            
      System.out.println("Sending search requests");
      
      c.getSearchMonitorSession(new MonitorExample.TestReceiver());
      SearchSession search1 = c.createSearchSession("elvis", 
                                                    SearchMessage.GET_BY_NAME,
                                                     100, 
                                                     0, 
                                                     new TestReceiver());

//      SearchSession search2 = c.createSearchSession("madonna", 
//                                                     100, 
//                                                     0, 
//                                                     new TestReceiver());
//
//      SearchSession search3 = c.createSearchSession("santana", 
//                                                     100, 
//                                                     0, 
//                                                     new TestReceiver());
                                                     
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }

  }

  /**
   *  Test class for receiving search responses
   *
   */
  static class TestReceiver extends MessageReceiverAdapter
  {

    /**
     *  For the example, just print the search response info to the console
     *
     */
    public void receiveSearchReply(SearchReplyMessage searchReply)
    {
      
      System.out.println("****Test receiver session received reply****");
      System.out.println("Port: " + searchReply.getPort());
      System.out.println("IP Address:" + searchReply.getIPAddress());
      System.out.println("Host Speed: " + searchReply.getDownloadSpeed());
      System.out.println("FileCount:" + searchReply.getFileCount());
      System.out.println("Vendor Code: " + searchReply.getVendorCode());
      System.out.println("ID: " + searchReply.getClientIdentifier().toString());
      
      for (int i = 0 ; i <  searchReply.getFileCount(); i++) 
      {
        SearchReplyMessage.FileRecord fileRecord = searchReply.getFileRecord(i);
        System.out.println("FileRecord: " +
                            i +
                            ", name: " +
                            fileRecord.getName() +
                            ", size: " +
                            fileRecord.getSize() +
                            ", index: " + 
                           fileRecord.getIndex());
      }

      System.out.println("****END Search session received reply****");
  }

  }
}