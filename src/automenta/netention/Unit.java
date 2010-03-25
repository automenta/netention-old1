/**
 * 
 */
package automenta.netention;

public enum Unit {
	Number,			//unitless
	Distance,		//ex: m
	Speed,			//distance/time, ex: m/s
	Mass,			//ex: kg
	Area,
	Volume,			//ex: meters^3
	TimePoint,		//absolute time, ie. the current date
	TimeDuration,	//a duration of time, ex: an amount of seconds
	Currency 
}