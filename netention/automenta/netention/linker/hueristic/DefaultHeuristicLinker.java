package automenta.netention.linker.hueristic;

import automenta.netention.*;
import automenta.netention.link.Satisfies;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import java.util.logging.Logger;

import java.util.Collection;

/**
 * Link Strength Heuristic 
 * 1.Inherited Pattern Similarity 
 * 2.Definite → Indefinite Satisfaction 
 * 3.Definite → Indefinite Dissatisfaction 
 * 4.Indefinite → Indefinite Similarity 
 * 5. ??
 * 
*/
public class DefaultHeuristicLinker extends HueristicLinker {

    private static final Logger logger = Logger.getLogger(DefaultHeuristicLinker.class.getName());
    private final double patternImportance;
    private final double propertyImportance;

    public DefaultHeuristicLinker(Self self) {
        super(self);
        this.patternImportance = 0.5;
        this.propertyImportance = 1.0;
    }

    public double getPatternImportance() {
        return patternImportance;
    }

    public double getPropertyImportance() {
        return propertyImportance;
    }

    
    
    
    @Override
    public Link compareSatisfying(Detail real, Detail imaginary) {
        double strength = 0;

        strength += getPatternCorrelation(real, imaginary) * getPatternImportance();
        
        //System.out.println(strength + " comparing: " + real.getValues() + " -> " + imaginary.getValues());
        
        if (strength > 0.0) {
            strength += getIndefiniteDefiniteComparison(real, imaginary) * getPropertyImportance();
        }

        if (strength == 0.0)
            return null;
        
        return new Satisfies(strength);
    }

    /**
     * strength of declared pattern correlation, a -> b, in [0..1.0]
     */
    public double getPatternCorrelation(Detail a, Detail b) {
        double na = a.getPatterns().size();
        double nb = b.getPatterns().size();
        if (na == 0) return 0.0;
        if (nb == 0) return 0.0;
        

        double s = 0;

        for (String aP : a.getPatterns()) {
            double ms = 0;
            for (String bP : b.getPatterns()) {
                if (aP.equals(bP))
                    ms = Math.max(ms, 1.0);
                else {
                    if (getSelf().isSuperPattern(aP, bP))
                        ms = Math.max(ms, 0.5);
                    else if (getSelf().isSuperPattern(bP, aP))
                        ms = Math.max(ms, 0.5);
                }
            }
            s += ms;
        }

        return s / (na + nb);
    }

    /**
     * compare A. Definite's -> B. Indefinite's satisfaction or dissatisfaction
     */
    public double getIndefiniteDefiniteComparison(Detail definite, Detail indefinite) {
        
        if (definite.getValues().isEmpty())             return 0;
        if (indefinite.getValues().isEmpty())           return 0;

        
        double s = 0;
        for (PropertyValue aP : indefinite.getValues()) {
            
            if (aP instanceof IndefiniteValue) {
                final IndefiniteValue iap = (IndefiniteValue) aP;                

                double maxSat = 0;

                for (PropertyValue bP : getProperties(definite, aP.getProperty())) {

                    if (bP instanceof DefiniteValue) {
                        DefiniteValue dbp = (DefiniteValue) bP;
                        double sat = satisifies(iap, dbp);
                        if (sat > maxSat) {
                            maxSat = sat;
                        }
                    }
                }

                s += maxSat;
            }
        }

        return s;
    }

    public Collection<PropertyValue> getProperties(final Detail d, final String property) {
        return Collections2.filter(d.getValues(), new Predicate<PropertyValue>() {
            @Override
            public boolean apply(PropertyValue pv) {
                return pv.getProperty().equals(property);
            }            
        });
    }

    public static double satisifies(final IndefiniteValue i, final DefiniteValue d) {
        final double f = d.satisfies(i);
        return f;
    }

    @Override
    public Link compareSimilarity(Detail a, Detail b) {
        return null;
    }

    
    /**
     * compare indefinite <-> indefinite satisfaction or dissatisfaction
     */
    @Deprecated public double getIndefiniteIndefiniteComparison(Detail a, Detail b) {
        //TODO calculate indefinite -> indefinite detail mutual satisfaction / overlap */
        return 0.0;
    }
}
