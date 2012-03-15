/**
 * 
 */
package automenta.netention;

import automenta.netention.html.DetailHTML;

public class PropertyValue implements Value {
	
	private String property;

        public PropertyValue() {
            property = null;
        }

	/** property ID or URI */
	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}
        
        public String toHTML(Self s, DetailHTML h) {
            return toString();
        }
}