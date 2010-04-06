/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.swing;

import automenta.netention.Detail;
import automenta.netention.Pattern;
import automenta.netention.Self;
import java.awt.Image;
import java.io.File;
import java.util.HashMap;
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
        objectToIconPath.put("property", "media/tango32/status/dialog-information.png");
        objectToIconPath.put("add", "media/tango32/actions/list-add.png");
        objectToIconPath.put("network", "media/tango32/status/network-transmit-receive.png");

        objectToIconPath.put("what", "media/tango32/places/folder.png");
        objectToIconPath.put("who", "media/tango32/apps/system-users.png");
        objectToIconPath.put("where", "media/tango32/categories/applications-internet.png");
        objectToIconPath.put("when", "media/tango32/actions/appointment-new.png");
        objectToIconPath.put("recent", "media/tango32/places/user-home.png");
        objectToIconPath.put("frequent", "media/tango32/status/dialog-warning.png");

    }

    public static Icon getFileIcon(String path) {
        if (path.startsWith("media://")) {
            path = path.replace("media://", "media/");            
        }

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

    public static Icon getIcon(String type) {
        String path = objectToIconPath.get(type);
        if (path != null) {
            return getFileIcon(path);
        }
        return getFileIcon(objectToIconPath.get("default"));
    }

    public static Icon getDetailIcon(Self s, Detail d) {
        String p = d.getIconURL();
        if (p == null) {
            for (String pat : d.getPatterns()) {
                Icon i = getPatternIcon(s.getPatterns().get(pat));
                if (i!=null)
                    return i;
            }
            return null;
        }
        else
            return getFileIcon(p);
    }

    public static Icon getPatternIcon(Pattern p) {
        if (p == null)
            return null;
        String u = p.getIconURL();
        if (u!=null)
            return getFileIcon(p.getIconURL());
        else
            return null;
    }
}
