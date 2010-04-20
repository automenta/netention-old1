package automenta.netention.echo3;

import automenta.netention.example.ExampleSelf;
import nextapp.echo.app.ApplicationInstance;
import nextapp.echo.webcontainer.WebContainerServlet;

public class MainServlet extends WebContainerServlet {

    public ApplicationInstance newApplicationInstance() {
        return new MainApp(new ExampleSelf());
    }
}
