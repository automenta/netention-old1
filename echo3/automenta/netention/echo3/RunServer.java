/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.echo3;

import automenta.netention.webui.NetworkPanel;
import nextapp.echo.app.ApplicationInstance;
import nextapp.echo.app.Color;
import nextapp.echo.app.ContentPane;
import nextapp.echo.app.Label;
import nextapp.echo.app.Window;
import nextapp.echo.app.WindowPane;
import nextapp.echo.webcontainer.WebContainerServlet;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;


/**
 *
 * @author seh
 */
public class RunServer {

    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);

        Context context = new Context(server,"/",Context.SESSIONS);
        context.addServlet(new ServletHolder(new MainServlet()), "/*");

        
        server.start();
        server.join();
    }
    
}
