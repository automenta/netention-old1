package automenta.netention.value.string;

import automenta.netention.Self;
import automenta.netention.html.DetailHTML;

public class StringContains extends StringEquals {

	public StringContains() {
		super();
	}
	
	public StringContains(String string) {
		super(string);
	}
	
    @Override
    public String toHTML(Self s, DetailHTML h) {
        return StringIs.toHTML(getProperty(), "equals", getString(), s);
    }
	
}
