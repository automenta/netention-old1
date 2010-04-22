package automenta.netention.echo3;

import nextapp.echo.app.ApplicationInstance;
import nextapp.echo.app.Color;
import nextapp.echo.app.ContentPane;
import nextapp.echo.app.Label;
import nextapp.echo.app.Window;
import nextapp.echo.app.WindowPane;

public class MainApp extends ApplicationInstance {

    public final static String  VERSION = "Netention 0.1";

    public MainApp() {
        super();
    }


    public Window init() {
        Window window = new Window();
        window.setBackground(Color.ORANGE);
        ContentPane contentPane = new ContentPane();
        window.setContent(contentPane);
        Label label = new Label(VERSION);
        contentPane.add(label);

        WindowPane np = new WindowPane();
        np.setTitle("Network");
        np.add(new NetworkPanel());
        contentPane.add(np);

        WindowPane scp = new WindowPane();
        scp.setTitle("Schema");
        //scp.add(new SchemaPanel(network.getSchema()));
        contentPane.add(scp);

        WindowPane stp = new WindowPane();
        stp.setTitle("Me");
        stp.add(new StoryPanel());
        contentPane.add(stp);


        return window;
    }
}
