/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package afxdeadcode;

import org.knallgrau.utils.textcat.FingerPrint;

/**
 *
 * @author SeH
 */
public class Category extends FingerPrint {
    private final String name;

    public Category(String name) {
        super();
        this.name = name;
    }

    @Override
    public String getCategory() {
        return name;
    }

//    void setName(String name) {
//        this.name = name;
//    }
//    
}
