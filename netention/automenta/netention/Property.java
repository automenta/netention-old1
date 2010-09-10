package automenta.netention;

import java.io.Serializable;



abstract public class Property implements Serializable {

	private String desc;
	private String id;
	private String name;
	private int cardinalityMax = 1;
	private int cardinalityMin = 0;

	public Property() {
		super();
		this.desc = "";
	}
	
	public Property(String id, String name) {
		this();
		this.id = id;
		this.name = name;
	}
	
	public String getDescription() {
		return desc;
	}
	
	public String getID() {
		return id;
	}

	public String getName() {
		if (name == null)
			return getID();
		return name;
	}
		
	@Override public String toString() {
		return "(" + getID() + ": " + getClass().getSimpleName() + ")";
	}

	public abstract PropertyValue newDefaultValue(Mode mode);

	/** max cardinality, -1 if unlimited */
	public int getCardinalityMax() {
		return cardinalityMax;
	}
	/** min cardinality, >= 0 */
	public int getCardinalityMin() {
		return cardinalityMin;
	}

	public void setCardinalityMax(int cardinalityMax) {
		this.cardinalityMax = cardinalityMax;
	}

	public void setCardinalityMin(int cardinalityMin) {
		this.cardinalityMin = cardinalityMin;
	}
	
	public Property setDescription(String description) {
		this.desc = description;
        return this;
	}


}
