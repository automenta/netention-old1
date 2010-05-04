/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.swing;

import automenta.netention.neuron.IzhikevichNeuron;
import automenta.netention.neuron.NeuralNetwork;
import automenta.netention.neuron.Neuron;
import automenta.netention.neuron.Synapse.ShortTermPlasticitySynapse;
import automenta.netention.swing.util.SwingWindow;
import automenta.spacegraph.SGCanvas;
import automenta.spacegraph.SGPanel;
import automenta.spacegraph.gleem.linalg.Vec3f;
import automenta.spacegraph.shape.Curve;
import automenta.spacegraph.shape.Rect;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javolution.context.ConcurrentContext;

/**
 *
 * @author seh
 */
public class RunIzhiDemo {

    public static void main(String[] args) {
        ConcurrentContext.setConcurrency(Runtime.getRuntime().availableProcessors());

        final NeuralNetwork nn = new NeuralNetwork();

        final int numNeurons = 4;
        //int numSynapses = 4;
        {
            for (int n = 0; n < numNeurons; n++) {
                nn.addNeuron(new IzhikevichNeuron());
            }
            for (int a = 0; a < numNeurons; a++) {
                for (int b = 0; b < numNeurons; b++) {
                    if (a != b) {
                        Neuron source = nn.getNeuron(a);
                        Neuron target = nn.getNeuron(b);
                        nn.addSynapse(new ShortTermPlasticitySynapse(source, target));
                    }
                }
            }
        }

        final SGCanvas sc = new SGCanvas();
        JPanel j = new SGPanel(sc);



        JPanel panel = new JPanel(new BorderLayout());
        panel.add(j, BorderLayout.CENTER);
        panel.add(new JButton("X"), BorderLayout.SOUTH);

        SwingWindow sw = new SwingWindow(panel, 900, 400, true);

        new Thread(new Runnable() {

            float x = -1.0f;
            float lastY = 0;
            private Rect lastP;

            @Override
            public void run() {
                while (true) {
                    nn.getNeuron((int)(Math.random() * numNeurons)).setActivation(2 * Math.random() - 1.0);

                    //nn.printNeurons();

                    for (int i = 0; i < 50; i++)
                        nn.update(0.01);

                    float s = 0.005f;
                    float y = (float)nn.getNeuron(0).getActivation();
                    
                    Rect p = new Rect();
                    p.setBackgroundColor(new Vec3f((float)(nn.getNeuron(0).getTotalInput()/200.0f), (float)(nn.getNeuron(0).getTotalInput()/-200.0f), 0.5f));
                    p.getCenter().set(x, (y)/100.0f, 0);
                    p.getSize().set(s, s, s);
                    sc.add(p);


                    if (lastP!=null) {
                        Curve c = new Curve(p, lastP, 2);
                        sc.add(c);
                    }


                    x += 0.005f;
                    if (x > 5f)
                        x = -1.0f;
                    
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException ex) {
                    }

                    lastY = y;
                    lastP = p;
                }
            }

        }).start();
    }
}
