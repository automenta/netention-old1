/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.swing;

import java.awt.Image;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 *
 * @author seh
 */
public class Icons {
//    public static enum PatternIcon {
//        THING("/thing");
//        EVENT("/thing");
//        EVENT("/thing");
//
//        public String iconURL;
//
//        PatternIcon(String iconURL) {
//            this.iconURL = iconURL;
//        }
//
//    }

    static Map<String, String> objectToIconPath = new HashMap();
    static Map<String, ImageIcon> icons = new HashMap();

    static {
        objectToIconPath.put("default", "media/tango32/actions/document-new.png");
        objectToIconPath.put("pattern", "media/tango32/categories/applications-system.png");
        objectToIconPath.put("Built", "media/tango32/categories/preferences-system.png");
        objectToIconPath.put("Event", "media/tango32/mimetypes/x-office-calendar.png");
        objectToIconPath.put("Media", "media/tango32/categories/applications-multimedia.png");
        objectToIconPath.put("Message", "media/tango32/apps/internet-mail.png");
        objectToIconPath.put("Mobile", "media/tango32/places/start-here.png");
        objectToIconPath.put("Person", "media/tango32/apps/system-users.png");
        objectToIconPath.put("Project", "media/tango32/mimetypes/x-office-presentation.png");
        //objectToIconPath.put("Event", "media/tango32/mimetypes/x-office-calendar.png");
    }

    public static Icon getIcon(String path) {
        try {
            //System.out.println("loading icon " + path);
            ImageIcon i = icons.get(path);
            if (i == null) {
                i = new ImageIcon(new File(".").getAbsolutePath() + "/" + path);
                i.setImage(i.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH));
                icons.put(path, i);
            }
            return i;
        } catch (Exception ex) {
            Logger.getLogger(Icons.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static Icon getObjectIcon(String type) {
        String path = objectToIconPath.get(type);
        if (path != null) {
            return getIcon(path);
        }
        return getIcon(objectToIconPath.get("default"));
    }

    public static Icon getObjectIcon(List<String> patterns) {
        for (String s : patterns) {
            String path = objectToIconPath.get(s);
            if (path != null) {
                return getIcon(path);
            }
        }

        return getIcon(objectToIconPath.get("default"));
    }
}
