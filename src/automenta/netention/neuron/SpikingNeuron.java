/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.neuron;

/**
 *
 * @author seh
 */
abstract public class SpikingNeuron extends RealtimeNeuron {

    /** Time of last spike.  */
    //public double lastSpikeTime;

    /** Whether a spike has occurred in the current time. */
    private boolean hasSpiked;

    /**
     * Default constructor.
     */
    public SpikingNeuron() {
    }




    @Override
    public void clear() {
        super.clear();
//        lastSpikeTime = 0;
    }

    /**
     * @param hasSpiked the hasSpiked to set
     */
    public void setHasSpiked(boolean hasSpiked) {
//        if (hasSpiked == true) {
//            lastSpikeTime = this.getParentNetwork().getRootNetwork().getTime();
//        }
        this.hasSpiked = hasSpiked;
    }

    /**
     * Whether the neuron has spiked in this instant or not.
     *
     * @return true if the neuron spiked.
     */
    public boolean hasSpiked() {
        return hasSpiked;
    }

//    /**
//     * @return the lastSpikeTime
//     */
//    public double getLastSpikeTime() {
//        return lastSpikeTime;
//    }
}