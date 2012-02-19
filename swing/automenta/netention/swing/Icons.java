/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.swing;

import automenta.netention.Detail;
import automenta.netention.Pattern;
import automenta.netention.Self;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import sun.awt.image.ToolkitImage;

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
        objectToIconPath.put("addPattern", "media/tango32/actions/list-add.png");
        objectToIconPath.put("network", "media/tango32/status/network-transmit-receive.png");

        objectToIconPath.put("thought", "media/tango32/categories/applications-system.png");

        objectToIconPath.put("what", "media/tango32/places/folder.png");
        objectToIconPath.put("who", "media/tango32/apps/system-users.png");
        objectToIconPath.put("where", "media/tango32/categories/applications-internet.png");
        objectToIconPath.put("when", "media/tango32/actions/appointment-new.png");
        objectToIconPath.put("recent", "media/tango32/places/user-home.png");
        objectToIconPath.put("frequent", "media/tango32/status/dialog-warning.png");
        
        objectToIconPath.put("home", "media/tango32/actions/go-home.png");
        objectToIconPath.put("map", "media/tango32/categories/applications-internet.png");
        
        objectToIconPath.put("person", "media/tango32/emotes/face-smile.png");
    }

    public static ImageIcon getFileIcon(String origPath) {
        String path = origPath;
        if (icons.containsKey(origPath)) {
            return icons.get(origPath);
        }

        ImageIcon i = null;
        if (path.startsWith("http://")) {
            try {
                i = new ImageIcon(new URL(path));
                Logger.getLogger(Icons.class.getName()).log(Level.INFO, "Loaded image " + path);
            } catch (Exception ex) {
                Logger.getLogger(Icons.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else {
            if (path.startsWith("media://")) {
                path = path.replace("media://", "media/");
            }

            try {
                i = new ImageIcon(new File(".").getAbsolutePath() + "/" + path);
            } catch (Exception ex) {
                Logger.getLogger(Icons.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        
        if (i != null) {
            i.setImage(i.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH));
            icons.put(origPath, i);
        }

        return i;

    }

    public static ImageIcon getIcon(String type) {
        String path = objectToIconPath.get(type);
        if (path != null) {
            return getFileIcon(path);
        }
        return getFileIcon(objectToIconPath.get("default"));
    }
    
    public static BufferedImage convertToGrayscale(BufferedImage source) { 
        BufferedImageOp op = new ColorConvertOp(
            ColorSpace.getInstance(ColorSpace.CS_GRAY), null); 

        return op.filter(source, null);
    }

    public static BufferedImage getBufferedImage(ToolkitImage image) {  
        // Create a buffered image in which to draw  
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(), image.getHeight(),  
                                            BufferedImage.TYPE_INT_RGB);  
        // Draw image into bufferedImage.  
        Graphics2D g2 = bufferedImage.createGraphics();  
        g2.drawImage(image, 0, 0, null);
        g2.dispose();  
        return bufferedImage;  
    }  
    
    public static ImageIcon getIconGrayscale(String t) {
        ImageIcon ii = getIcon(t);
        ii.setImage(convertToGrayscale(getBufferedImage((ToolkitImage)ii.getImage())));
        return ii;
    }

    public static ImageIcon getDetailIcon(Self s, Detail d) {
        String p = d.getIconURL();
        if (p == null) {
            for (String pat : d.getPatterns()) {
                ImageIcon i = getPatternIcon(s.getPattern(pat));
                if (i != null) {
                    return i;
                }
            }
            return getIcon("thought");
        } else {
            return getFileIcon(p);
        }
    }

    public static ImageIcon getPatternIcon(Pattern p) {
        if (p == null) {
            return null;
        }
        String u = p.getIconURL();
        if (u != null) {
            return getFileIcon(p.getIconURL());
        } else {
            return null;
        }
        
    }

            public static ImageIcon getObjectIcon(Self self, Object o) {
                if (o instanceof Detail) {
                    Detail d = (Detail) o;
                    return Icons.getDetailIcon(self, d);
                } else if (o instanceof Pattern) {
                    Pattern p = (Pattern) o;
                    return Icons.getPatternIcon(p);
                }
                return Icons.getIcon("thought");
            }

}
