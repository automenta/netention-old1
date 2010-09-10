package automenta.netention.linker.hueristic;

import automenta.netention.DefiniteValue;
import automenta.netention.Detail;
import automenta.netention.IndefiniteValue;
import automenta.netention.Link;
import automenta.netention.PropertyValue;
import automenta.netention.link.Satisfies;
import java.util.List;
import java.util.logging.Logger;

import java.util.Collection;
import java.util.LinkedList;

/**
Link Strength Heuristic
	1.Inherited Pattern Similarity
	2.Definite → Indefinite Satisfaction
	3.Definite → Indefinite Dissatisfaction
	4.Indefinite → Indefinite Similarity
	5.
*
*/
public class DefaultHeuristicLinker extends HueristicLinker {
	private static final Logger logger = Logger.getLogger(DefaultHeuristicLinker.class.getName());


    @Override public Link compareSatisfying(Detail a, Detail b) {
		double strength = 0;
		
		//System.out.println(" comparing: " + a.getProperties() + " -> " + b.getProperties());
        
		strength += getPatternStrength(a, b);
		if (strength > 0.0) {
			strength += getIndefiniteDefiniteComparison(a, b);
			strength += getIndefiniteDefiniteComparison(b, a);
			strength += getIndefiniteIndefiniteComparison(a, b);
		}
			
		return new Satisfies(strength);
	}
	

	/** strength of declared pattern correlation, a -> b, in [0..1.0] */
	public double getPatternStrength(Detail a, Detail b) {
		if (a.getPatterns().size() == 0)
			return 0.0;
		
		List<String> bPatterns = b.getPatterns();
		
		double s = 0;
		
		for (String aP : a.getPatterns()) {
			if (bPatterns.contains(aP)) {
				s += 1.0;
			}			
			//TODO accumulate matched super-patterns			
		}
					
		return s / ((double)a.getPatterns().size());
	}

	/**  compare A.Indefinite's -> B.Definite's satisfaction or dissatisfaction*/
	public double getIndefiniteDefiniteComparison(Detail a, Detail b) {
		//TODO use numIndefiniteProperties rather than numTotalProperties ??
		double numProperties = a.getProperties().size();

		if (numProperties == 0.0)
			return 0.0;
		
		double s = 0;  
		for (PropertyValue aP : a.getProperties()) {
			if (aP instanceof IndefiniteValue) {
				IndefiniteValue iap = (IndefiniteValue) aP;
				
				double maxSat = 0;
				
				for (PropertyValue bP : getProperties(b, aP.getProperty())) {
					if (bP instanceof DefiniteValue) {
						DefiniteValue dbp = (DefiniteValue)bP;
						double sat = satisifies(iap, dbp);
						if (sat > maxSat)
							maxSat = sat;
					}
				}
				
				s += maxSat;
			}
		}
		
		return s / numProperties;
	}
    
	public Collection<PropertyValue> getProperties(Detail d, String property) {
		List<PropertyValue> lpv = new LinkedList();
		for (PropertyValue pv : d.getProperties()) {
			if (pv.getProperty().equals(property))
				lpv.add(pv);
		}
		return lpv;
	}

	public double satisifies(IndefiniteValue i, DefiniteValue d) {
		return d.satisfies(i);
//		if (d instanceof IntegerIs) {
//			IntegerIs dI = (IntegerIs) d;
//			return dI.satisfies(i);
//		}
//		else if (d instanceof RealIs) {
//			RealIs dR = (RealIs) d;
//			return dR.satisfies(i);			
//		}
//		else if (d instanceof StringIs) {
//			return ((StringIs)d).satisfies(i);
//		}
//		else if (d instanceof GeoPointIs) {
//			return ((GeoPointIs)d).satisfies(i);			
//		}
//		else if (d instanceof NodeIs) {
//			
//		}
//		else {
//			logger.severe("unrecognized definite value type: " + d.getClass() + " for definiteValue " + d);
//		}
//		return false;
	}


	/**  compare indefinite <-> indefinite satisfaction or dissatisfaction*/
	public double getIndefiniteIndefiniteComparison(Detail a, Detail b) {
		//TODO calculate indefinite -> indefinite detail mutual satisfaction / overlap */
		return 0.0;
	}

}
