/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.swing.widget.email;

import java.awt.Frame;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

/* This class displays a simple dialog instructing the user
   that messages are being downloaded. */
public class DownloadingDialog extends JDialog {
    
    // Constructor for dialog.
    public DownloadingDialog(Frame parent) {
        // Call super constructor, specifying that dialog is modal.
        super(parent, true);
        
        // Set dialog title.
        setTitle("E-mail Client");
        
        // Instruct window not to close when the "X" is clicked.
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        
        // Put a message with a nice border in this dialog.
        JPanel contentPane = new JPanel();
        contentPane.setBorder(
                BorderFactory.createEmptyBorder(5, 5, 5, 5));
        contentPane.add(new JLabel("Downloading messages..."));
        setContentPane(contentPane);
        
        // Size dialog to components.
        pack();
        
        // Center dialog over application.
        setLocationRelativeTo(parent);
    }
}