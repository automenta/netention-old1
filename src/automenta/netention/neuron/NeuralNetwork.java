/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.neuron;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author seh
 */
public class NeuralNetwork {

    List<Neuron> neuronList = new LinkedList();
    List<Synapse> synapseList = new LinkedList();

    public void addNeuron(Neuron n) {
        neuronList.add(n);
    }
    public void addSynapse(Synapse s) {
        synapseList.add(s);
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
        for (Neuron n : neuronList) {
            n.update(dt); // update neuron buffers
        }

        // Then update the activations themselves
        for (Neuron n : neuronList) {
            n.setActivation(n.getBuffer());
        }
    }

    /**
     * Calls {@link Synapse#update} for each weight.
     */
    protected void updateAllSynapses() {

        // No Buffering necessary because the values of weights don't depend on one another
        for (Synapse s : synapseList) {
            s.update();
        }
    }

    public Neuron getNeuron(int i) {
        return neuronList.get(i);
    }

    public void printNeurons() {
        for (Neuron n : neuronList) {
            System.out.println(n);
            System.out.println("  totalInput: " + n.getTotalInput());
            System.out.println("  activation: " + n.getActivation());
            System.out.println();
        }
    }


}
