/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.swing;

import automenta.netention.impl.MemoryDetail;
import automenta.netention.impl.MemorySelf;
import automenta.netention.swing.util.SwingWindow;
import automenta.netention.swing.widget.DetailEditPanel;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 *
 * @author seh
 */
public class RunDetailEdit {

    public static class EncodingPanel extends JPanel {

        private final JTextArea textArea;
        private final MemoryDetail detail;

        public EncodingPanel(MemoryDetail d) {
            super(new BorderLayout());

            this.detail = d;

            textArea = new JTextArea();
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            add(textArea, BorderLayout.CENTER);

            refresh();
        }

        public String getJSON(){
            return textArea.getText();
        }

        public void refresh() {
            JSONSerializer serializer = new JSONSerializer();
            //String output = serializer.include("patterns", "properties", "whenCreated", "whenModified").prettyPrint(detail);
            String output = serializer.include("patterns", "properties", "whenCreated", "whenModified").serialize(detail);
            textArea.setText(output);
        }
    }

    public static MemoryDetail getDetail(String json) {
        MemoryDetail md = new JSONDeserializer<MemoryDetail>().deserialize(json);
        System.out.println(md.getName());
        System.out.println(md.getPatterns());
        System.out.println(md.getProperties());
        return md;
    }

    public static void main(String[] args) {
        final Logger logger = Logger.getLogger(SelfBrowserPanel.class.getName());

        MemorySelf self = new MemorySelf("me", "Me");
        new SeedSelfBuilder().build(self);
        logger.log(Level.INFO, "Loaded Seed Self");

        final MemorySelf mSelf = self;

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception ex) {
                    System.err.println(ex);
                }

                final MemoryDetail d = new MemoryDetail("X");
                mSelf.addDetail(d);

                final EncodingPanel ep = new EncodingPanel(d);

                final DetailEditPanel views = new DetailEditPanel(mSelf, new MemoryDetail(), false, false) {

                    @Override
                    protected void deleteThis() {
                    }

                    @Override
                    protected void patternChanged() {
                    }
                };

                DetailEditPanel edits = new DetailEditPanel(mSelf, d, true, false) {

                    @Override protected void deleteThis() {
                    }

                    @Override
                    protected void patternChanged() {
                    }

                    @Override
                    protected synchronized void updateDetail() {
                        super.updateDetail();
                        ep.refresh();
                        views.setDetail(getDetail(ep.getJSON()));
                    }
                };

                JPanel p = new JPanel(new GridLayout(1, 2));
                JPanel s = new JPanel(new GridLayout(2, 1));
                {
                    s.add(ep);
                    s.add(views);
                }

                p.add(edits);
                p.add(s);

                SwingWindow window = new SwingWindow(p, 900, 800, true);
            }
        });

    }
}
