/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package afxdeadcode.test;

import java.util.Arrays;
import org.knallgrau.utils.textcat.FingerPrint;

/**
 *
 * @author SeH
 */
public class ClassifierTest {
    
    public static void main(String[] args) {
        
        FingerPrint happy = new FingerPrint() {
            @Override public String getCategory() {
                return "happy";
            }            
        };
        happy.create("i am so happy right now.\ni love everything. i am happy\n");
        System.out.println(happy);
        
        FingerPrint sad = new FingerPrint() {
            @Override public String getCategory() {
                return "sad";
            }                        
        };
        sad.create("i hate everything. i'm sad. this sucks");
        //System.out.println(sad);
        
        FingerPrint x = new FingerPrint();
        x.create("i am so happy");
        System.out.println(x.categorize(Arrays.asList(new FingerPrint[] { happy, sad } )));
    }
}
