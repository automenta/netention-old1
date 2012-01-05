/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.email;

import automenta.netention.NMessage;
import java.util.Arrays;
import javax.mail.Message;

/**
 *
 * @author seh
 */
public class NEMailMessage extends NMessage {

    public NEMailMessage(Message m) throws Exception  {
        super(Arrays.asList(m.getFrom()).toString() + " " + m.getMessageNumber(),                 
                Arrays.asList(m.getFrom()).toString(), 
                Arrays.asList(m.getAllRecipients()).toString(), 
                m.getSentDate(), 
                m.getSubject(), 
                m.getContent().toString(), "");
    }
    
    
}
