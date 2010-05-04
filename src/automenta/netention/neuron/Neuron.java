/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.neuron;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author seh
 */
public abstract class Neuron  {


    /** Activation value of the neuron.  The main state variable. */
    protected double activation = 0;

    /** Minimum value this neuron can take. */
    protected double lowerBound = -1;

    /** Maximum value  this neuron can take. */
    protected double upperBound = 1;

    /** Amount by which to increment or decrement neuron. */
    private double increment = .1;

    /** Temporary activation value. */
    private double buffer = 0;

    /** Value of any external inputs to neuron. */
    private double inputValue = 0;

//    /** List of synapses this neuron attaches to. */
    private ArrayList<Synapse> fanOut = new ArrayList<Synapse>();
//
//    /** List of synapses attaching to this neuron. */
    private ArrayList<Synapse> fanIn = new ArrayList<Synapse>();

    /** If true then do not update this neuron. */
    private boolean clamped = false;

    /** Target value. */
    private double targetValue = 0;


    /**
     * Default constructor needed for external calls which create neurons then
     * set their parameters.
     */
    protected Neuron() {
    }



    /**
     * Override if initialization is needed.
     */
    public void init() {}


    /**
     * Sets the activation of the neuron.
     * @param act Activation
     */
    public void setActivation(final double act) {
        if (!clamped) {
            activation = act;
        }
    }

    /**
     * @return the level of activation.
     */
    public double getActivation() {
        return activation;
    }



    /**
     * @return upper bound of the neuron.
     */
    public double getUpperBound() {
        return upperBound;
    }

    /**
     * Sets the upper bound of the neuron.
     * @param d Value to set upper bound
     */
    public void setUpperBound(final double d) {
        upperBound = d;
    }

    /**
     * @return lower bound of the neuron.
     */
    public double getLowerBound() {
        return lowerBound;
    }

    /**
     * Sets the lower bound of the neuron.
     * @param d Value to set lower bound
     */
    public void setLowerBound(final double d) {
        lowerBound = d;
    }

    /**
     * @return the neuron increment.
     */
    public double getIncrement() {
        return increment;
    }

    /**
     * Sets the neuron increment.
     * @param d Value to set increment
     */
    public void setIncrement(final double d) {
        increment = d;
    }

    /**
     * @return the fan in array list.
     */
    public List<Synapse> getFanIn() {
        return fanIn;
    }

    /**
     * @return the fan out array list.
     */
    public List<Synapse> getFanOut() {
        return fanOut;
    }

    /**
     * Increment this neuron by increment.
     */
    public void incrementActivation() {
        if (activation < upperBound) {
            activation += increment;
        }
    }

    /**
     * Decrement this neuron by increment.
     */
    public void decrementActivation() {
        if (activation > lowerBound) {
            activation -= increment;
        }
    }

    /**
     * Connect this neuron to target neuron via a weight.
     *
     * @param target the connnection between this neuron and a target neuron
     */
    void addTarget(final Synapse target) {
        fanOut.add(target);
    }

    /**
     * Remove this neuron from target neuron via a weight.
     *
     * @param target the connnection between this neuron and a target neuron
     */
    void removeTarget(final Synapse target) {
        fanOut.remove(target);
    }

    /**
     * Connect this neuron to source neuron via a weight.
     *
     * @param source the connnection between this neuron and a source neuron
     */
    void addSource(final Synapse source) {
        fanIn.add(source);
    }

    /**
     * Remove this neuron from source neuron via a weight.
     *
     * @param source the connnection between this neuron and a source neuron
     */
    void removeSource(final Synapse source) {
        fanIn.remove(source);
    }
// not used.  Consider deleting?
//    /**
//     * Add specified amount of activation to this neuron.
//     *
//     * @param amount amount to add to this neuron
//     */
//    public void addActivation(final double amount) {
//        activation += amount;
//    }

    /**
     * Sums the weighted signals that are sent to this node.
     *
     * @return weighted input to this node
     */
    public double getWeightedInputs() {
        double wtdSum = inputValue;
        if (fanIn.size() > 0) {
            for (int j = 0; j < fanIn.size(); j++) {
                Synapse w = (Synapse) fanIn.get(j);
                if (w.isSendWeightedInput()) {
                    wtdSum += w.getValue();
                }
            }
        }

        return wtdSum;
    }

    /**
     * Randomize this neuron to a value between upperBound and lowerBound.
     */
    public void randomize() {
        setActivation(getRandomValue());
    }

    /**
     * Returns a random value between the upper and lower bounds of this neuron.
     * @return the random value.
     */
    public double getRandomValue() {
        return (upperBound - lowerBound) * Math.random() + lowerBound;
    }

    /**
     * Randomize this neuron to a value between upperBound and lowerBound.
     */
    public void randomizeBuffer() {
        setBuffer(getRandomValue());
    }

    /*
     * Update all neurons n this neuron is connected to, by adding current activation
     * times the connection-weight  NOT CURRENTLY USED.
     */
//    public void updateConnectedOutward() {
//        // Update connected weights
//        if (fanOut.size() > 0) {
//            for (int j = 0; j < fanOut.size(); j++) {
//                Synapse w = (Synapse) fanOut.get(j);
//                Neuron target = w.getTarget();
//                target.setActivation(w.getStrength() * activation);
//                target.checkBounds();
//            }
//        }
//    }

    /**
     * Check if this neuron is connected to a given weight.
     *
     * @param w weight to check
     *
     * @return true if this neuron has w in its fan_in or fan_out
     */
//    public boolean connectedToWeight(final Synapse w) {
//        if (fanOut.size() > 0) {
//            for (int j = 0; j < fanOut.size(); j++) {
//                Synapse outW = (Synapse) fanOut.get(j);
//
//                if (w.equals(outW)) {
//                    return true;
//                }
//            }
//        }
//
//        if (fanIn.size() > 0) {
//            for (int j = 0; j < fanIn.size(); j++) {
//                Synapse inW = (Synapse) fanIn.get(j);
//
//                if (w.equals(inW)) {
//                    return true;
//                }
//            }
//        }
//
//        return false;
//    }

//    /**
//     * Round the activation level of this neuron off to a specified precision.
//     *
//     * @param precision precision to round this neuron's activation off to
//     */
//    public void round(final int precision) {
//        setActivation(Network.round(getActivation(), precision));
//    }

    /**
     * If activation is above or below its bounds set it to those bounds.
     */
    public void checkBounds() {
        activation = clip(activation);
    }

    /**
     * If value is above or below its bounds set it to those bounds.
     * @param value Value to check
     * @return clip
     */
    public double clip(final double value) {
        double val = value;
        if (val > upperBound) {
            val = upperBound;
        }

        if (val < lowerBound) {
            val = lowerBound;
        }

        return val;
    }

//    /**
//     * Sends relevant information about the network to standard output. TODO: Change to toString()
//     */
//    public void debug() {
//        System.out.println("neuron " + id);
//        System.out.println("fan in");
//
//        for (int i = 0; i < fanIn.size(); i++) {
//            Synapse tempRef = (Synapse) fanIn.get(i);
//            System.out.println("fanIn [" + i + "]:" + tempRef);
//        }
//
//        System.out.println("fan out");
//
//        for (int i = 0; i < fanOut.size(); i++) {
//            Synapse tempRef = (Synapse) fanOut.get(i);
//            System.out.println("fanOut [" + i + "]:" + tempRef);
//        }
//    }


    /**
     * Temporary buffer which can be used for algorithms which should not depend on
     * the order in which  neurons are updated.
     *
     * @param d temporary value
     */
    public void setBuffer(final double d) {
        buffer = d;
    }

    /**
     * @return Returns the current value in the buffer.
     */
    public double getBuffer() {
        return buffer;
    }

    /**
     * @return Returns the inputValue.
     */
    public double getInputValue() {
        return inputValue;
    }

    /**
     * @param inputValue The inputValue to set.
     */
    public void setInputValue(final double inputValue) {
        this.inputValue = inputValue;
        // this.targetValue = inputValue; //TODO: This is temporary!
    }

    /**
     * @return the name of the class of this network.
     */
    public String getType() {
        return this.getClass().getName().substring(this.getClass().getName().lastIndexOf('.') + 1);
    }

    /**
     * Returns the sum of the strengths of the weights attaching to this neuron.
     *
     * @return the sum of the incoming weights to this neuron.
     */
    public double getSummedIncomingWeights() {
        double ret = 0;

        for (int i = 0; i < fanIn.size(); i++) {
            Synapse tempRef = (Synapse) fanIn.get(i);
            ret += tempRef.getStrength();
        }

        return ret;
    }

    /**
     * Returns the number of neurons attaching to this one which have activity above
     * a specified threshold.
     *
     * @param threshold value above which neurons are considered "active."
     * @return number of "active" neurons
     */
    public int getNumberOfActiveInputs(final int threshold) {
        int numActiveLines = 0;
        // Determine number of active (greater than 0) input lines
        for (Synapse incoming : fanIn) {
            if (incoming.getSource().getActivation() > threshold) {
                numActiveLines++;
            }
        }
        return numActiveLines;
    }

    /**
     * @return the average activation of neurons connecting to this neuron
     */
    public double getAverageInput() {
        return getTotalInput() / fanIn.size();
    }

    /**
     * @return the total activation of neurons connecting to this neuron
     */
    public double getTotalInput() {
        double ret = 0;

        for (int i = 0; i < fanIn.size(); i++) {
            ret += ((Synapse) fanIn.get(i)).getSource().getActivation();
        }

        return ret;
    }

//    /**
//     * TODO:
//     * Check if any couplings attach to this world and if there are no none, remove the listener.
//     * @param world
//     */
//    private void removeWorldListener(World world) {
//
//    }

    /**
     * Return true if this neuron has a motor coupling attached.
     *
     * @return true if this neuron has a motor coupling attached
     */
    public boolean isOutput() {
        return false;
//        return (motorCoupling != null);
    }

    /**
     * Return true if this neuron has a sensory coupling attached.
     *
     * @return true if this neuron has a sensory coupling attached
     */
    public boolean isInput() {
        return false;
      //  return (sensoryCoupling != null);
    }

    /**
     * True if the synapse is connected to this neuron, false otherwise.
     * @param s the synapse to check.
     * @return true if synapse is connected, false otherwise.
     */
    public boolean isConnected(final Synapse s) {
        return (fanIn.contains(s) || fanOut.contains(s));
     }


    /**
     * Set activation to 0; override for other "clearing" behavior.
     */
    public void clear() {
       activation = 0;
    }

    /**
     * @return the targetValue
     */
    public double getTargetValue() {
        return targetValue;
    }

    /**
     * Set target value.
     *
     * @param targetValue value to set.
     */
    public void setTargetValue(final double targetValue) {
        this.targetValue = targetValue;
    }


    /**
     * @return the clamped
     */
    public boolean isClamped() {
        return clamped;
    }

    /**
     * Toggles whether this neuron is clamped.
     *
     * @param clamped Whether this neuron is to be clamped.
     */
    public void setClamped(final boolean clamped) {
        this.clamped = clamped;
    }

    abstract public void update(double dt);

}