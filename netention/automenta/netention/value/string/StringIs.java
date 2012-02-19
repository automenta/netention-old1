package automenta.netention.value.string;

import automenta.netention.*;
import automenta.netention.html.DetailHTML;


public class StringIs extends PropertyValue implements DefiniteValue<String> {

	private String string;

	public StringIs() { }
	
	public StringIs(String string) {
		super();
		this.string = string;
	}
	
	@Override public String getValue() {	
		return string;
	}

	public void setValue(String s) {
		this.string = s;		
	}

	@Override public double satisfies(IndefiniteValue i) {
		if (i.getClass().equals( StringEquals.class )) {
			//TODO implement string-difference-distance fall-off (ex: 0.05 * # of chars different)
			return ((StringEquals)i).getString().equalsIgnoreCase(getValue()) ? 1.0 : 0.0;
		}
		else if (i.getClass().equals( StringContains.class) ) {
			return getValue().toLowerCase().contains( ((StringContains)i).getString().toLowerCase() ) ? 1.0 : 0.0;
		}
		else if (i.getClass().equals( StringNotContains.class ) ) {
			return !(getValue().toLowerCase().contains( ((StringNotContains)i).getString().toLowerCase() )) ? 1.0 : 0.0;
		}
		// TODO impl remaining String satisfy's
		return 0;
	}
        
    public static String toHTML(String p, String operative, String value, Self s) {
        if (p!=null) {
            Property pp = s.getProperty(p);
            if (pp!=null) {
                if (pp instanceof StringProp) {
                    StringProp sp = (StringProp)pp;
                    if (sp.isRich()) {
                        return pp.getName() + " " + operative + ":<br/><ul>" + value + "</ul>";
                    }
                }
                return pp.getName() + " is: " + value;                
            }
        }
        return value;
        
    }

    @Override
    public String toHTML(Self s, DetailHTML h) {
        return toHTML(getProperty(), "is", string, s);
    }

        
}
