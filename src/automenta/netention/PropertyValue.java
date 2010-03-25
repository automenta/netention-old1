/**
 * 
 */
package automenta.netention;

public abstract class PropertyValue implements Value {
	
	private String property;


	/** property ID or URI */
	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

}