/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.swing;

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

    static Map<String, ImageIcon> icons = new HashMap();
    
    public static Icon getIcon(String path) {
        try {
            ImageIcon i = icons.get(path);
            if (i == null) {
                i = new ImageIcon(new File(".").getAbsolutePath() + "/" + path);
                i.setImage( i.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH) );
                icons.put(path, i);
            }
            return i;
        } catch (Exception ex) {
            Logger.getLogger(Icons.class.getName()).log(Level.SEVERE, null, ex);
        }
            return null;
    }
}
