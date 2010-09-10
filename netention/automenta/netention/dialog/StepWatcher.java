package automenta.netention.dialog;

public interface StepWatcher {

    public void onStepChanged(Step step, boolean stateChanged, boolean nextStepsChanged);
}
