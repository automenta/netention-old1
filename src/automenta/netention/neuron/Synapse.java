/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.neuron;

import java.util.LinkedList;
import java.util.List;

abstract public class Synapse {

    public abstract static class SpikeResponder {

        /** Value. */
        protected double value = 0;
        /** Parent. */
        protected Synapse parent;

        /** Used for combo box. */
        /**
         * Update the synapse.
         */
        public abstract void update();

        /**
         * @return the name of the class of this synapse
         */
        public String getType() {
            return this.getClass().getName().substring(this.getClass().getName().lastIndexOf('.') + 1);
        }

        /**
         * @return Returns the value.
         */
        public double getValue() {
            return value;
        }

        /**
         * @param value The value to set.
         */
        public void setValue(final double value) {
            this.value = value;
        }

        /**
         * @return Returns the parent.
         */
        public Synapse getParent() {
            return parent;
        }

        /**
         * @param parent The parent to set.
         */
        public void setParent(final Synapse parent) {
            this.parent = parent;
        }
    }

    public static class JumpAndDecay extends SpikeResponder {

        /** Jump height value. */
        private double jumpHeight = 2;
        /** Base line value. */
        private double baseLine = 0;
        /** Rate at which synapse will decay. */
        private double decayRate = .001;

        /**
         * @return null
         */
        public SpikeResponder duplicate() {
            // TODO Auto-generated method stub
            return null;
        }

        /**
         * Update the synapse.
         */
        public void update() {
            if (((SpikingNeuron) parent.getSource()).hasSpiked()) {
                value = jumpHeight;
            } else {
                value += (decayRate * (baseLine - value));
            }
        }

        /**
         * @return Returns the baseLine.
         */
        public double getBaseLine() {
            return baseLine;
        }

        /**
         * @param baseLine The baseLine to set.
         */
        public void setBaseLine(final double baseLine) {
            this.baseLine = baseLine;
        }

        /**
         * @return Returns the decayRate.
         */
        public double getDecayRate() {
            return decayRate;
        }

        /**
         * @param decayRate The decayRate to set.
         */
        public void setDecayRate(final double decayRate) {
            this.decayRate = decayRate;
        }

        /**
         * @return Returns the jumpHeight.
         */
        public double getJumpHeight() {
            return jumpHeight;
        }

        /**
         * @param jumpHeight The jumpHeight to set.
         */
        public void setJumpHeight(final double jumpHeight) {
            this.jumpHeight = jumpHeight;
        }

        /**
         * @return Name of synapse type.
         */
        public static String getName() {
            return "Jump and decay";
        }
    }

    public static class ShortTermPlasticitySynapse extends Synapse {

        /** STD. */
        private static final int STD = 0;
        /** Plasticity type. */
        public static final int DEFAULT_PLASTICITY_TYPE = STD;
        /** Pseudo spike threshold. */
        public static final double DEFAULT_FIRING_THRESHOLD = 0;
        /** Base line strength. */
        public static final double DEFAULT_BASE_LINE_STRENGTH = 1;
        /** Input threshold. */
        public static final double DEFAULT_INPUT_THRESHOLD = 0;
        /** Bump rate. */
        public static final double DEFAULT_BUMP_RATE = .5;
        /** Rate at which the synapse will decay. */
        public static final double DEFAULT_DECAY_RATE = .2;
        /** Activated. */
        public static final boolean DEFAULT_ACTIVATED = false;
        /** STF. */
//    private static final int STF = 1;
        /** Plasticity type. */
        private int plasticityType = STD;
        /** Pseudo spike threshold. */
        private double firingThreshold = DEFAULT_FIRING_THRESHOLD;
        /** Base line strength. */
        private double baseLineStrength = DEFAULT_BASE_LINE_STRENGTH;
        /** Input threshold. */
        private double inputThreshold = DEFAULT_INPUT_THRESHOLD;
        /** Bump rate. */
        private double bumpRate = DEFAULT_BUMP_RATE;
        /** Rate at which the synapse will decay. */
        private double decayRate = DEFAULT_DECAY_RATE;
        /** Activated. */
        private boolean activated = DEFAULT_ACTIVATED;

        /**
         * Creates a weight of some value connecting two neurons.
         *
         * @param src source neuron
         * @param tar target neuron
         * @param val initial weight value
         * @param theId Id of the synapse
         */
        public ShortTermPlasticitySynapse(final Neuron src, final Neuron tar, final double val, final String theId) {
            super(src, tar);
//        setSource(src);
//        setTarget(tar);
            strength = val;
            id = theId;
        }

        

            /**
         * Creates a weight connecting source and target neurons.
         *
         * @param source source neuron
         * @param target target neuron
         */
        public ShortTermPlasticitySynapse(final Neuron source, final Neuron target) {
            super(source, target);
        }

        /**
         * Updates the synapse.
         */
        public void update() {
            // Determine whether to activate short term dynamics
            if (this.getSource() instanceof SpikingNeuron) {
                if (((SpikingNeuron) this.getSource()).hasSpiked()) {
                    activated = true;
                } else {
                    activated = false;
                }
            } else {
                if (this.getSource().getActivation() > firingThreshold) {
                    activated = true;
                } else {
                    activated = false;
                }
            }

            if (activated) {
                if (plasticityType == STD) {
                    strength -= (bumpRate * (strength - lowerBound));
                } else {
                    strength += (bumpRate * (upperBound - strength));
                }
            } else {
                strength -= (decayRate * (strength - baseLineStrength));
            }

            strength = clip(strength);
        }

        /**
         * @return Returns the baseLineStrength.
         */
        public double getBaseLineStrength() {
            return baseLineStrength;
        }

        /**
         * @param baseLineStrength The baseLineStrength to set.
         */
        public void setBaseLineStrength(final double baseLineStrength) {
            this.baseLineStrength = baseLineStrength;
        }

        /**
         * @return Returns the decayRate.
         */
        public double getDecayRate() {
            return decayRate;
        }

        /**
         * @param decayRate The decayRate to set.
         */
        public void setDecayRate(final double decayRate) {
            this.decayRate = decayRate;
        }

        /**
         * @return Returns the growthRate.
         */
        public double getBumpRate() {
            return bumpRate;
        }

        /**
         * @param growthRate The growthRate to set.
         */
        public void setBumpRate(final double growthRate) {
            this.bumpRate = growthRate;
        }

        /**
         * @return Returns the inputThreshold.
         */
        public double getInputThreshold() {
            return inputThreshold;
        }

        /**
         * @param inputThreshold The inputThreshold to set.
         */
        public void setInputThreshold(final double inputThreshold) {
            this.inputThreshold = inputThreshold;
        }

        /**
         * @return Returns the plasticityType.
         */
        public int getPlasticityType() {
            return plasticityType;
        }

        /**
         * @param plasticityType The plasticityType to set.
         */
        public void setPlasticityType(final int plasticityType) {
            this.plasticityType = plasticityType;
        }

        /**
         * @return the firing threshold.
         */
        public double getFiringThreshold() {
            return firingThreshold;
        }

        /**
         * @param firingThreshold The firingThreshold to set.
         */
        public void setFiringThreshold(final double firingThreshold) {
            this.firingThreshold = firingThreshold;
        }
    }

    /** Neuron activation will come from. */
    private Neuron source;
    /** Neuron to which the synapse is attached. */
    private Neuron target;
    /**  Only used of source neuron is a spiking neuron. */
    protected SpikeResponder spikeResponder = null;
    /** Synapse id. */
    protected String id = null;
    /** The maximum number of digits to display in the tool tip. */
    private static final int MAX_DIGITS = 2;
    /** Number of parameters. */
    public static final int NUM_PARAMETERS = 8;
    /** Strength of synapse. */
    protected double strength = 1;
    /** Amount to increment the neuron. */
    protected double increment = 1;
    /** Upper limit of synapse. */
    protected double upperBound = 10;
    /** Lower limit of synapse. */
    protected double lowerBound = -10;
    /** Time to delay sending activation to target neuron. */
    private int delay = 0;
    /**
     *  Boolean flag, indicating whether this type of synapse
     *  participates in the computation of weighted input
     *  Set to a default value of true.
     */
    private boolean sendWeightedInput = true;
    /** Manages delays of synapses. */
    private LinkedList<Double> delayManager = null;

    public Synapse(Neuron source, Neuron target) {
        setSource(source);
        setTarget(target);
        initSpikeResponder();
    }

    /**
     * Set a default spike responder if the source neuron is a  spiking neuron, else set the spikeResponder to null.
     */
    public void initSpikeResponder() {
        if (source instanceof SpikingNeuron) {
            setSpikeResponder(new JumpAndDecay());
        } else {
            setSpikeResponder(null);
        }
    }

    /**
     * Update synapse.
     */
    public abstract void update();


    /**
     * For spiking source neurons, returns the spike-responder's value times the synapse strength.
     * For non-spiking neurons, returns the pre-synaptic activation times the synapse strength.
     *
     * @return Value
     */
    public double getValue() {
        double val;

        if (source instanceof SpikingNeuron) {
            spikeResponder.update();
            val = strength * spikeResponder.getValue();
        } else {
            val = source.getActivation() * strength;
        }

        if (delayManager == null) {
            return val;
        } else {
            enqueu(val);

            return dequeu();
        }
    }

    /**
     * @return the name of the class of this synapse
     */
    public String getType() {
        return this.getClass().getName().substring(this.getClass().getName().lastIndexOf('.') + 1);
    }

    /**
     * @return Strength of synapse.
     */
    public double getStrength() {
        return strength;
    }

    /** @see GaugeSource */
    public double getGaugeValue() {
        return getStrength();
    }

    /**
     * Cleans up this Synapse that has been deleted.
     */
    void delete() {
        if (source != null) {
            source.removeTarget(this);
        }
        if (target != null) {
            target.removeSource(this);
        }
    }

    /**
     * @return Source neuron to which the synapse is attached.
     */
    public Neuron getSource() {
        return source;
    }

    /**
     * New source neuron to attach the synapse.
     * @param n Neuron to attach synapse
     */
    public void setSource(final Neuron n) {
        if (this.source != null) {
            this.source.removeTarget(this);
        }

        if (n != null) {
            this.source = n;
            n.addTarget(this);
        }
    }

    /**
     * @return Target neuron to which the synapse is attached.
     */
    public Neuron getTarget() {
        return target;
    }

    /**
     * New target neuron to attach the synapse.
     * @param n Neuron to attach synapse
     */
    public void setTarget(final Neuron n) {
        if (this.target != null) {
            this.target.removeSource(this);
        }

        if (n != null) {
            this.target = n;
            n.addSource(this);
        }
    }

    /**
     * Sets the strength of the synapse.
     * @param wt Strength value
     */
    public void setStrength(final double wt) {
        strength = wt;
    }

    /**
     * @return Upper synapse bound.
     */
    public double getUpperBound() {
        return upperBound;
    }

    /**
     * Sets the upper synapse bound.
     * @param d bound
     */
    public void setUpperBound(final double d) {
        upperBound = d;
    }

    /**
     * @return Lower synapse boundy.
     */
    public double getLowerBound() {
        return lowerBound;
    }

    /**
     * Sets the lower synapse bound.
     * @param d bound
     */
    public void setLowerBound(final double d) {
        lowerBound = d;
    }

    /**
     * @return Amount to increment neuron.
     */
    public double getIncrement() {
        return increment;
    }

    /**
     * Sets the amount to increment neuron.
     * @param d Increment amount
     */
    public void setIncrement(final double d) {
        increment = d;
    }

    /**
     * Increment this weight by increment.
     */
    public void incrementWeight() {
        if (strength < upperBound) {
            strength += increment;
        }
    }

    /**
     * Decrement this weight by increment.
     */
    public void decrementWeight() {
        if (strength > lowerBound) {
            strength -= increment;
        }
    }

    /**
     * Increase the absolute value of this weight by increment amount.
     */
    public void reinforce() {
        if (strength > 0) {
            incrementWeight();
        } else if (strength < 0) {
            decrementWeight();
        } else if (strength == 0) {
            strength = 0;
        }
    }

    /**
     * Decrease the absolute value of this weight by increment amount.
     */
    public void weaken() {
        if (strength > 0) {
            decrementWeight();
        } else if (strength < 0) {
            incrementWeight();
        } else if (strength == 0) {
            strength = 0;
        }
    }

    /**
     * Randomizes this synapse and sets the symmetric analogue to the same value.
     * A bit of a hack, since it it is used on a collection a bunch of redundancy could
     * happen.
     */
    public void randomizeSymmetric() {
        randomize();
        Synapse symmetric = getSymmetricSynapse();
        if (symmetric != null) {
            symmetric.setStrength(strength);
        }
    }

    /**
     * Returns symmetric synapse if there is one, null otherwise.
     * @return the symmetric synapse, if any.
     */
    public Synapse getSymmetricSynapse() {
        List<Synapse> targetsOut = this.getTarget().getFanOut();
        int index = targetsOut.indexOf(this.getSource());

        return (index < 0) ? null : targetsOut.get(index);

        //        for (Synapse synapse : this.getTarget().getFanOut()) {
//            if (synapse.getTarget() == this.getSource()) {
//                return synapse;
//            }
//        }
//        return null;
    }

    /**
     * Randomize this weight to a value between its upper and lower bounds.
     */
    public void randomize() {
        strength = getRandomValue();
    }

    /**
     * Returns a random value between the upper and lower bounds of this synapse.
     * @return the random value.
     */
    public double getRandomValue() {
        return (upperBound - lowerBound) * Math.random() + lowerBound;
    }

    /**
     * If weight  value is above or below its bounds set it to those bounds.
     */
    public void checkBounds() {
        if (strength > upperBound) {
            strength = upperBound;
        }

        if (strength < lowerBound) {
            strength = lowerBound;
        }
    }

    /**
     * If value is above or below its bounds set it to those bounds.
     * @param value Value to be checked
     * @return Evaluated value
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

    /**
     * @return Returns the id.
     */
    public String getId() {
        return id;
    }

    /**
     * @param id The id to set.
     */
    public void setId(final String id) {
        this.id = id;
    }

    /**
     * @return Returns the spikeResponder.
     */
    public SpikeResponder getSpikeResponder() {
        return spikeResponder;
    }

    /**
     * @param sr The spikeResponder to set.
     */
    public void setSpikeResponder(final SpikeResponder sr) {
        this.spikeResponder = sr;

        if (sr == null) {
            return;
        }

        spikeResponder.setParent(this);
    }

    ////////////////////
    //  Delay manager //
    ////////////////////
    /**
     * Delay manager.
     * @param dly Amound of delay
     */
    public void setDelay(final int dly) {
        delay = dly;

        if (delay == 0) {
            delayManager = null;

            return;
        }

        delayManager = new LinkedList<Double>();
        delayManager.clear();

        for (int i = 0; i < delay; i++) {
            delayManager.add(new Double(0));
        }
    }

    /**
     * @return Current amount of delay.
     */
    public int getDelay() {
        return delay;
    }

    /**
     * @return the deque.
     */
    private double dequeu() {
        return delayManager.removeFirst().doubleValue();
    }

    /**
     * Enqueeu.
     * @param val Value to enqueu
     */
    private void enqueu(final double val) {
        delayManager.add(new Double(val));
    }

    /**
     * @return sendWeightedInput for the synapse
     */
    public boolean isSendWeightedInput() {
        return sendWeightedInput;
    }

    /**
     * @param sendWeightedInput to set.
     */
    public void setSendWeightedInput(boolean sendWeightedInput) {
        this.sendWeightedInput = sendWeightedInput;
    }
}
