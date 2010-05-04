/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.dialog.step;

import automenta.netention.dialog.AbstractStep;
import automenta.netention.dialog.Step;
import automenta.netention.dialog.StepWatcher;
import java.util.LinkedList;
import java.util.List;

/**
 * basically displays a content object, ex: text, image, video, HTML
 */
public class MessageStep extends AbstractStep implements Step {

    private final String message;
    protected List<Choice> choices;
    protected int chosen;

    public static enum MessagePerceptionState {

        Unobserved, Observed /*, ActivatedBy */

    }

    public static class Choice {

        public final String label;
        public final Step step;

        Choice(String label, Step step) {
            this.label = label;
            this.step = step;
        }
    }

    public MessageStep(String message) {
        super();
        this.message = message;
        this.choices = new LinkedList();
        this.chosen = -1;
    }

    public MessageStep(String message, List<Choice> choices) {
        super();
        this.message = message;

        this.choices = choices;
        this.chosen = -1;
    }

    public MessageStep(String message, String choice, Step step) {
        super();
        this.message = message;

        this.choices = new LinkedList();
        choices.add(new Choice(choice, step));

        this.chosen = -1;
    }

    public MessageStep(String message, String choiceA, Step stepA, String choiceB, Step stepB) {
        super();
        this.message = message;

        this.choices = new LinkedList();
        choices.add(new Choice(choiceA, stepA));
        choices.add(new Choice(choiceB, stepB));

        this.chosen = -1;
    }

    public void setChoice(int c) {
        this.chosen = c;
        for (StepWatcher w : getWatchers()) {
            w.onStepChanged(this, false, true);
        }
    }

    public String getMessage() {
        return message;
    }

    @Override
    public List<Step> getNextSteps() {
        if (chosen == -1) {
            return null;
        } else {
            LinkedList<Step> ll = new LinkedList();
            ll.add(choices.get(chosen).step);
            return ll;
        }
    }

    public List<Choice> getChoices() {
        if (chosen == -1)
            return choices;
        else
            return null;
    }
    
    public void setChoices(List<Choice> c) {
        this.choices = c;
        for (StepWatcher w : getWatchers()) {
            w.onStepChanged(this, true, false);
        }
    }
    
}
