/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention;

import java.util.List;

/**
 * Read-only Detail
 * @author seh
 */
public interface RODetail extends Node {
    
    /** creator's URI */
    public String getCreator();

    public Mode getMode();
	public List<String> getPatterns();
	public List<PropertyValue> getProperties();

    public String getIconURL();

}
