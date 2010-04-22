package automenta.netention;

import java.util.List;


public interface Detail extends RODetail {

//	private String creator;	//agentID of the creator of this node
//
//	private List<String> patterns = new LinkedList();
//	private List<PropertyValue> properties = new LinkedList();
//
//	public Detail(String id, String name, String... patterns) {
//		super(id, name);
//
//		for (String p : patterns) {
//			getPatterns().add(p);
//		}
//
//	}


    public void setName(String newName);

    public void setMode(Mode mode);


//	/** analogous to the set of rdf:type statements */
//	public List<String> getPatterns() {
//		return patterns;
//	}
//
//	public void add(String property, PropertyValue value) {
//		synchronized (getProperties()) {
//			value.setProperty(property);
//			getProperties().add(value);
//		}
//	}
//
//	/** equivalent to set, but useful for currying */
//	public Node with(String property, PropertyValue value) {
//		add(property, value);
//		return this;
//	}
//
//	public class InvalidPropertyException extends Exception { }
//
//	//TODO implement setValidated(String property, PropertyValue value)
//
////	public void setValidated(String property, Serializable value) throws InvalidPropertyException {
////		if (isValidPropertyType(property, value)) {
////			set(property, value);
////		}
////		else {
////			throw new InvalidPropertyException();
////		}
////	}
//
//	public boolean isValidPropertyType(String property, Object value) {
//		//TODO implement isValidPropertyType, and test property type checking
//		return true;
//	}
//
//	/** the creator's agent ID */
//	public String getCreator() {
//		return creator;
//	}
//
//	public void setCreator(String creator) {
//		this.creator = creator;
//	}
//
//	@Override public String toString() {
//		return getID() + " (" + getName() + ")" + "=<" + getPatterns() + ">";
//	}
//
//	/** get all properties */
//	public List<PropertyValue> getProperties() {
//		return properties;
//	}
//
//	/** get all properties with a certain property ID */
//	public Collection<PropertyValue> getProperties(String property) {
//		List<PropertyValue> lpv = new LinkedList();
//		for (PropertyValue pv : getProperties()) {
//			if (pv.getProperty().equals(property))
//				lpv.add(pv);
//		}
//		return lpv;
//	}
//
//
//    public List<Pattern> getOtherPossiblePatterns(Schema s, Detail d) {
//        return null;
//    }
//
//    public List<Property> getOtherPossibleProperties(Schema s, Detail d) {
//        return null;
//    }
//
//	public int getNumPropertiesDefined(String property) {
//		int count = 0;
//		for (PropertyValue pv : getProperties()) {
//			if (pv.getProperty().equals(property))
//				count++;
//		}
//		return count;
//	}

}
