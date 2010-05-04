package automenta.netention.dialog;

import automenta.netention.Node.AbstractNode;
import java.util.LinkedList;
import java.util.List;

public class AbstractStep extends AbstractNode implements Step {

    protected List<Step> nextSteps = null;
    protected List<StepWatcher> watchers = new LinkedList();

    public AbstractStep() {
        super("", "");
    }


//    public void setNextState(S nextState) {
//        this.state = nextState;
//        //notify stepwatchers that state changed
//        for (StepWatcher w : watchers) {
//            w.onStepChanged(this, true, false);
//        }
//    }

    public void setNextStep(Step s) {
        LinkedList<Step> l = new LinkedList();
        l.add(s);
        setNextSteps(l);

    }

    public void setNextSteps(List<Step> ls) {
        this.nextSteps = ls;
        //notify stepwatchers that steps changed
        for (StepWatcher w : watchers) {
            w.onStepChanged(this, false, true);
        }
    }

    @Override
    public List<Step> getNextSteps() {
        return nextSteps;
    }

    @Override
    public void addStepWatcher(StepWatcher w) {
        watchers.add(w);
    }

    @Override
    public void removeStepWatcher(StepWatcher w) {
        watchers.remove(w);
    }

    public List<StepWatcher> getWatchers() {
        return watchers;
    }

    
}
