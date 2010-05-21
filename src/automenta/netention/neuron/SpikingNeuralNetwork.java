/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.neuron;

import com.syncleus.dann.graph.MutableDirectedAdjacencyGraph;
import com.syncleus.dann.neural.Neuron;
import com.syncleus.dann.neural.Synapse;

/**
 *
 * @author seh
 */
public class SpikingNeuralNetwork extends MutableDirectedAdjacencyGraph<RealtimeNeuron, SpikingSynapse> {

    //List<RealtimeNeuron> neuronList = new LinkedList();
    //List<SpikingSynapse> synapseList = new LinkedList();

    public SpikingNeuralNetwork() {
        super();
    }


    public void addNeuron(RealtimeNeuron n) {
        add(n);
    }
    public void addSynapse(SpikingSynapse s) {
        add(s);
    }

    public void update(double dt) {
        updateAllSynapses();
        updateAllNeurons(dt);
    }

        /**
     * Calls {@link Neuron#update} for each neuron.
     */
    protected void updateAllNeurons(double dt) {

        // First update the activation buffers
        for (RealtimeNeuron n : getNodes()) {
            n.update(getInEdges(n), dt); // update neuron buffers
        }

        // Then update the activations themselves
        for (RealtimeNeuron n : getNodes()) {
            n.setActivation(n.getBuffer());
        }
    }

    /**
     * Calls {@link Synapse#update} for each weight.
     */
    protected void updateAllSynapses() {

        // No Buffering necessary because the values of weights don't depend on one another
        for (SpikingSynapse s : getEdges()) {
            s.update();
        }
    }

    public void printNeurons() {
        for (RealtimeNeuron n : getNodes()) {
            System.out.println(n);
            //System.out.println("  totalInput: " + n.getTotalInput());
            System.out.println("  activation: " + n.getActivation());
            System.out.println();
        }
    }


}
