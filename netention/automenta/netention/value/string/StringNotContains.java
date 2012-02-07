package automenta.netention.value.string;

import automenta.netention.Self;
import automenta.netention.html.DetailHTML;

public class StringNotContains extends StringEquals {

	public StringNotContains() {
		super();
	}
	
	public StringNotContains(String string) {
		super(string);
	}
	
@Override
    public String toHTML(Self s, DetailHTML h) {
        return StringIs.toHTML(getProperty(), "does not contain", getString(), s);
    }
}
