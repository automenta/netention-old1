package afxdeadcode;

import org.python.core.PyInteger;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

/**
 *
 * @author SeH
 */
public class RunTwitterBotPython {

//      http://docs.oracle.com/javase/6/docs/technotes/guides/scripting/programmer_guide/index.html

    
    public static void main(String[] args) throws Exception {
        System.out.println("Free memory: " + Runtime.getRuntime().freeMemory());
        System.out.println("Total memory: " + Runtime.getRuntime().totalMemory());
        System.out.println("Max memory: " + Runtime.getRuntime().maxMemory());
        
        for (final String scriptFile : args) {
            new Thread(new Runnable() {
                @Override public void run() {
                    final PythonInterpreter interp = new PythonInterpreter();
                    System.out.println("Running " + scriptFile);
                    interp.execfile(scriptFile);
                }                
            }).start();
        }
                        
                
    }
    
}
