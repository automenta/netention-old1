/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.survive;

/**
 * Useful as a placeholder for unimplemented data sources
 * @author seh
 */
public class NullDataSource extends DataSource {
    //private static final List<Event> emptyList = Collections.unmodifiableList(new ArrayList());

    public NullDataSource(String id, String name, String category, String iconURL, String unit) {
        super(id, name, category, iconURL, unit);
    }

    @Override
    public double getMaxMeasurement() {
        return 0;
    }

    @Override
    public double getMinMeasurement() {
        return 0;
    }

//    @Override
//    public Iterator<Event> iterateEvents() {
//        return emptyList.iterator();
//    }
    
}
