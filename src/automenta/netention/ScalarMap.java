package automenta.netention;

import com.syncleus.dann.graph.Graph;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class ScalarMap<X> extends HashMap<X, Double> {

    public void highpass(final double threshold) {
        List<X> toRemove = new LinkedList();
        for (X x : keySet()) {
            if (get(x).doubleValue() < threshold) {
                toRemove.add(x);
            }
        }
        for (X x : toRemove) {
            unset(x);
        }
    }

    public void set(X x, double value) {
        put(x, new Double(value));
    }

    public void unset(X x) {
        remove(x);
    }

    public void multiply(double factor) {
        List<X> items = new ArrayList(keySet());
        for (X x : items) {
            set(x, get(x).doubleValue() * factor);
        }
    }

    public void zero() {
        List<X> items = new ArrayList(keySet());
        for (X x : items) {
            set(x, 0);
        }
    }

    public void spikeRandomly(double spike) {
        if (size() == 0) {
            return;
        }
        List<X> items = new ArrayList(keySet());
        int random = (int) (Math.random() * ((items.size())));
        X x = items.get(random);
        set(x, get(x).doubleValue() + spike);
    }

    public double getAverage() {
        double total = 0;
        int count = 0;
        for (X x : keySet()) {
            total += get(x).doubleValue();
            count++;
        }
        return total / count;
    }

    public void dissipate(Graph<X, ?> g) {
    }
}
