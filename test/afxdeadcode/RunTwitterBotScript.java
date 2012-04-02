package afxdeadcode;

import org.python.core.PyInteger;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

/**
 *
 * @author SeH
 */
public class RunTwitterBotScript {

//      http://docs.oracle.com/javase/6/docs/technotes/guides/scripting/programmer_guide/index.html

    
    public static void main(String[] args) throws Exception {
        String scriptFile = args[1];
                
        PythonInterpreter interp = new PythonInterpreter();
        interp.execfile(scriptFile);
                
    }
    
}
