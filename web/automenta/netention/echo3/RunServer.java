/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.echo3;


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
