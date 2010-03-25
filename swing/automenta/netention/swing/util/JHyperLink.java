package automenta.netention.swing.util;

import java.awt.Cursor;
import java.awt.Font;
import javax.swing.JButton;

public class JHyperLink extends JButton {

    public JHyperLink(String toString, String tooltip) {
        super("<html><u>" + toString + "</u></html>");
        setOpaque(false);
        setBorderPainted(false);
        setFont(getFont().deriveFont(Font.BOLD));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setToolTipText(tooltip);
    }
}
