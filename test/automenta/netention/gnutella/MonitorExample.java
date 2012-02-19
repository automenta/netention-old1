/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.gnutella;

/*
 * JTella MonitorExample
 *
 * Copyright (c) 2000 Ken McCrary, All Rights Reserved.
 *
 * Permission to use, copy, modify, and distribute this software
 * and its documentation for NON-COMMERCIAL purposes and without
 * fee is hereby granted provided that this copyright notice
 * appears in all copies.
 *
 * KEN MCCRARY MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE
 * SUITABILITY OF THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE, OR NON-INFRINGEMENT. KEN MCCRARY
 * SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT
 * OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */
 
import com.kenmccrary.jtella.ConnectionData;
import com.kenmccrary.jtella.GNUTellaConnection;
import com.kenmccrary.jtella.MessageReceiverAdapter;
import com.kenmccrary.jtella.SearchMessage;

/**
 *  An example class for showing how to monitor search queries on the
 *  GNUTella network using JTella. The application expects two command
 *  ine parameters, the host name, and the port the servant is listening on
 *
 */
public class MonitorExample {
    
  static int incomingPort = 8999;
    
  private GNUTellaConnection conn;

  /**
   *  Constructs the example given a started network connection
   *
   */
  public MonitorExample(GNUTellaConnection networkConnection)
  {
    this.conn = networkConnection;
  }

  /**
   *  Main entrypoint for the example
   *
   */
  public static void main(String[] args)
  {
    System.out.println("<--- JTella MonitorExample running --->\n");

    try
    {
      System.out.println("Connecting to Gnutella Network...");
      
      //-------------------------------------------------------------
      // Start a network connection and listen for succesful connection
      //-------------------------------------------------------------
      ConnectionData cd = new ConnectionData();
      cd.setIncomingPort(incomingPort);
      GNUTellaConnection c = new GNUTellaConnection(cd);
      
      c.getSearchMonitorSession(new TestReceiver());
      c.start();

    }
    catch (Exception e)
    {
      e.printStackTrace();
    }

  }

  /**
   *  Test class for monitoring query messages, prints out queries to the console
   *
   */
  static class TestReceiver extends MessageReceiverAdapter
  {
    /**
     *  Receives Search messages from the network, this example just
     *  prints the search criteria to the console
     *
     *  @param searchMessage a search message received on the network
     */
    public void receiveSearch(SearchMessage searchMessage)
    {
      System.out.println("Search Session received: " +
                         searchMessage.getSearchCriteria());  
    }
  }
}
