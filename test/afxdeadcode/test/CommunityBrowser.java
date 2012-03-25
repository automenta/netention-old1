/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package afxdeadcode.test;

import automenta.netention.Detail;
import java.awt.BorderLayout;
import java.text.DecimalFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author SeH
 */
public class CommunityBrowser {
    
}
//public class CommunityBrowser extends JPanel implements ListSelectionListener {
//
//    private final JSplitPane split;
//    private final JTable table;
//    private final JPanel userView;
//    private final DefaultTableModel tableModel;
//    private final Community community;
//    private boolean updating;
//
//    public CommunityBrowser(Community c) {
//        super(new BorderLayout());
//        
//        this.community = c;
//        
//        tableModel = new DefaultTableModel() {
//            @Override public boolean isCellEditable(int row, int column) {
//                return false;
//            }
//        };
//        tableModel.addColumn("ID");
//        tableModel.addColumn("Last Updated");
//        tableModel.addColumn("Tweets Read");
//        tableModel.addColumn("Happy|Sad");
//        tableModel.addColumn("Rich|Poor");
//
//        split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
//        
//        
//        split.setLeftComponent(new JScrollPane(table = new JTable(tableModel)));
//        split.setRightComponent(new JScrollPane(userView = new JPanel()));
//        
//        table.getSelectionModel().addListSelectionListener(this);
//        table.setAutoCreateRowSorter(true);
//        table.setCellEditor(null);
//        
//        add(split, BorderLayout.CENTER);
//
//        new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//                while (true) {
//                    update();
//                    try {
//                        Thread.sleep(3000);
//                    } catch (InterruptedException ex) {
//                        Logger.getLogger(CommunityBrowser.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                }
//            }
//            
//        }).start();
//    }
//
//    public int getRow(String username) {
//        for (int i = 0; i < tableModel.getRowCount(); i++) {
//            if (tableModel.getValueAt(i, 0).toString().equals(username))
//                return i;
//        }
//        return -1;
//    }
//    
//    static String doubleToString(double n) {
//        int x = (int)(n * 10000.0);
//        return intToString(x, 10);
//    }
//    
//    static String intToString(int num, int digits) {
//        assert digits > 0 : "Invalid number of digits";
//
//        // create variable length array of zeros
//        char[] zeros = new char[digits];
//        Arrays.fill(zeros, '0');
//        // format number as String
//        DecimalFormat df = new DecimalFormat(String.valueOf(zeros));
//
//        return df.format(num);
//    }
//    
//    protected void update() {
//        int selected = table.getSelectedRow();
//        updating = true;
//        
//        table.setVisible(false);
//        for (Agent a : community.agents.values()) {
//            if (a.details.size() < 2) {
//                continue;
//            }
//            
//            final Date d = a.lastUpdated;
//            int existingRow = getRow(a.name);
//            if (existingRow!=-1)
//                tableModel.removeRow(existingRow);
//            
//            String happy = doubleToString(a.getScore(community.classifier, d, "happy")/a.getScore(community.classifier, d, "sad"));
//            String rich = doubleToString(a.getScore(community.classifier, d, "rich")/a.getScore(community.classifier, d, "poor"));
//            
//            
//            tableModel.addRow(new Object[] { a.name, a.lastContacted, a.details.size(), happy, rich } );
//        }
//        table.setVisible(true);
//        updating = false;
//        
//        SwingUtilities.invokeLater(new Runnable() {
//            @Override public void run() {
//                updateUI();
//            }            
//        });
//        
//        
//    }
//
//    @Override
//    public synchronized void valueChanged(ListSelectionEvent e) {
//        
//        int selected = table.getSelectedRow();
//        
//        if (selected!=-1) {
//            userView.removeAll();
//            
//            String user = table.getValueAt(selected, 0).toString();
//            
//            List<Detail> l = new LinkedList(community.getAgent(user).details);
//            Collections.sort(l, new Comparator<Detail>() {
//                @Override public int compare(Detail o1, Detail o2) {
//                    return o2.getWhen().compareTo(o1.getWhen());
//                }                
//            });
//            
//            String p = "";
//            for (Detail d : l) {
//                p += d.getName() + " (" + d.getWhen() + ")" + "\n\n";
//            }
//
//            JTextArea t = new JTextArea(p, 80, 50);
//            t.setEditable(false);
//            t.setLineWrap(true);
//            t.setWrapStyleWord(true);
//            
//            
//            userView.add(t, BorderLayout.CENTER);
//            
//            updateUI();
//        }
//    
//    }
//
//    
//    
//}
