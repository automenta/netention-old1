/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.echo3;

import automenta.netention.Pattern;
import automenta.netention.Schema;
import automenta.netention.node.value.Property;
import nextapp.echo.app.Color;
import nextapp.echo.app.Column;
import nextapp.echo.app.Extent;
import nextapp.echo.app.Grid;
import nextapp.echo.app.Insets;
import nextapp.echo.app.Label;
import nextapp.echo.app.ListBox;
import nextapp.echo.app.Panel;
import nextapp.echo.app.SplitPane;
import nextapp.echo.app.event.ActionEvent;
import nextapp.echo.app.event.ActionListener;
import nextapp.echo.app.list.DefaultListModel;
import nextapp.echo.extras.app.MenuBarPane;
import nextapp.echo.extras.app.menu.DefaultMenuModel;
import nextapp.echo.extras.app.menu.DefaultOptionModel;
import nextapp.echo.extras.app.menu.MenuModel;
import nextapp.echo.extras.app.menu.SeparatorModel;

/**
 *
 * @author seh
 */
public class SchemaPanel extends Panel {

    private final Schema schema;

    public class PatternList extends ListBox {

        public PatternList(PatternListModel patternListModel) {
            super(patternListModel);
        }
    }

    public class PatternListModel extends DefaultListModel {

        public PatternListModel() {
            super();
            for (Pattern p : schema.getPatterns().values()) {
                add(p);
            }
        }
    }

    public class SchemaPropertyPanel extends Panel {

        public SchemaPropertyPanel(String propertyID) {
            super();

            Property p = schema.getProperty(propertyID);

            if (p == null) {
                add(new Label("missing property: " + propertyID));
                return;
            }

            Column c = new Column();
            c.add(new Label(p.getName() + " (" + p.getID() + ")"));
            c.add(new Label(p.getClass().toString()));
            c.add(new Label(p.getDescription()));

            add(c);

            setBackground(Color.LIGHTGRAY);
        }
    }

    public class PatternPanel extends Panel {

        public PatternPanel() {
            super();
        }

        public void setPattern(Pattern p) {
            removeAll();

            Column c = new Column();

            c.add(new Label(p.getName() + " (" + p.getID() + ")"));
            c.add(new Label(p.getDescription()));

            //add extends
            Grid g = new Grid();
            for (String e : p.getExtends()) {
                c.add(new Label(e));
            }
            g.setOrientation(Grid.ORIENTATION_HORIZONTAL);
            g.setInsets(new Insets(new Extent(1, Extent.EM)));
            c.add(g);


            //add defined properties
            Column dp = new Column();
            for (String pr : p.getDefinedProperties()) {
                dp.add(new SchemaPropertyPanel(pr));
            }
            c.add(dp);

            //add inherited properties
            Column ip = new Column();
            for (String pr : schema.getInheritedPatterns(p)) {
                ip.add(new SchemaPropertyPanel(pr));
            }
            c.add(ip);



            add(c);


        }
    }

    public static MenuModel newMenuModel(Schema schema, Pattern p) {
        DefaultMenuModel mm = new DefaultMenuModel(p.getID(), p.getName());
        
        mm.addItem(new DefaultOptionModel(p.getID(), p.getName(), null));

        mm.addItem(new SeparatorModel());

        for (Pattern c : schema.getChildren(p)) {
            mm.addItem(newMenuModel(schema, c));
        }

        return mm;
    }

    public static MenuModel newMenuModel(Schema schema) {

        DefaultMenuModel mm = new DefaultMenuModel();

        for (Pattern p : schema.getRootPatterns()) {
            mm.addItem(newMenuModel(schema, p));
        }

        return mm;
    }

//    public SchemaPanel(Schema schema) {
//        super();
//        this.schema = schema;
//
//        Column sp = new Column();
//
//        MenuBarPane mb = new MenuBarPane(newMenuModel(schema));
//
//        final PatternPanel pp = new PatternPanel();
//
////        final PatternList pl = new PatternList(new PatternListModel());
////        pl.addActionListener(new ActionListener() {
////            public void actionPerformed(ActionEvent arg0) {
////                Pattern p = (Pattern)pl.getSelectedValue();
////                pp.setPattern(p);
////            }
////        });
////        pl.setWidth(new Extent(100, Extent.PERCENT));
//        //pl.setHeight(new Extent(100, Extent.PERCENT));
//        add(mb, 0);
//
//        pp.setWidth(new Extent(100, Extent.PERCENT));
//        //pp.setHeight(new Extent(100, Extent.PERCENT));
//        sp.add(pp, 0);
//
//        add(sp);
//    }
    public SchemaPanel(final Schema schema) {
        super();
        this.schema = schema;

        SplitPane sp = new SplitPane(SplitPane.ORIENTATION_VERTICAL);
        //sp.setSeparatorPosition(new Extent(25, Extent.PERCENT));
        
        //sp.setSeparatorVisible(true);
        sp.setAutoPositioned(true);
        sp.setOrientation(SplitPane.ORIENTATION_VERTICAL_TOP_BOTTOM);


        final PatternPanel pp = new PatternPanel();

        MenuBarPane mb = new MenuBarPane(newMenuModel(schema));
        mb.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                pp.setPattern(schema.getPattern(e.getActionCommand()));
            }
        });

        sp.add(mb, 0);

        pp.setWidth(new Extent(100, Extent.PERCENT));
        pp.setHeight(new Extent(100, Extent.PERCENT));
        sp.add(pp, 1);

        add(sp);
              
    }
}
