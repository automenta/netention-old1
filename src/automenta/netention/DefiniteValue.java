package automenta.netention;


public interface DefiniteValue<X> extends Value {

	public X getValue();

	/** the degree to which this definite value satisfies an indefinite value, in [0..1.0] */
	double satisfies(IndefiniteValue i);
	
}
