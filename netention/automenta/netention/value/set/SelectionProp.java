/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.value.set;

import automenta.netention.Mode;
import automenta.netention.Property;
import automenta.netention.PropertyValue;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author seh
 */
public class SelectionProp extends Property {

    private List<String> options;

    public SelectionProp() {
        super();
    }

    public SelectionProp(String id, String name) {
        super(id, name);
    }

    public SelectionProp(String id, String name, List<String> options) {
        this(id, name);
        this.options = options;
    }

    public SelectionProp(String id, String name, String... options) {
        this(id, name, Arrays.asList(options));
    }

    @Override
    public PropertyValue newDefaultValue(Mode mode) {
        String x = allowBlank() ? "" : options.get(getDefaultOption());
        if (mode == Mode.Imaginary) {            
            return new SelectionEquals(x);
        } else {
            return new SelectionIs(x);
        }
    }

    public int getDefaultOption() {
        return 0;
    }

    public List<String> getOptions() {
        return options;
    }
    
    public boolean allowBlank() {
        return true; //TODO make this parameterizable
    }
}
