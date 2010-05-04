/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.plugin.finance;

import automenta.netention.Node.AbstractNode;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author seh
 */
public class PublicBusiness extends AbstractNode {

    private final String stockTicker;
    //http://ichart.finance.yahoo.com/table.csv?s=YHOO&a=03&b=12&c=1996&d=04&e=4&f=2010&g=m&ignore=.csv
    //http://www.gummy-stuff.org/Yahoo-data.htm
    //http://www.etraderzone.com/free-scripts/47-historical-quotes-yahoo.html

    public class BusinessPerformance extends AbstractNode implements Comparable<BusinessPerformance> {

        public final Date start;
        public final Date end;
        public final float open;
        public final float high;
        public final float low;
        public final float close;
        public final float volume;

        public BusinessPerformance(Date start, float open, float high, float low, float close, float volume) {
            super(PublicBusiness.this.getStockTicker() + ".BusinessPerformance@" + start.getTime());
            this.start = start;
            this.end = start;
            this.open = open;
            this.high = high;
            this.low = low;
            this.close = close;
            this.volume = volume;
        }

        @Override
        public int compareTo(BusinessPerformance o) {
            return start.compareTo(o.start);
        }

        @Override
        public int hashCode() {
            return start.hashCode() + getStockTicker().hashCode();
        }

        public String getStockTicker() {
            return getBusiness().stockTicker;
        }

        public PublicBusiness getBusiness() {
            return PublicBusiness.this;
        }
        
        @Override
        public boolean equals(Object obj) {
            if (obj instanceof BusinessPerformance) {
                BusinessPerformance b = (BusinessPerformance) obj;
                return (b.start.equals(start)) && (getStockTicker().equals(b.getStockTicker()));
            }
            return false;
        }

        @Override
        public String toString() {
            return open + ", " + high + ", " + low + ", " + close + ", " + volume;
        }

    }

    SortedSet<BusinessPerformance> performance = new TreeSet();

    public PublicBusiness(String stockticker) {
        super(stockticker);
        this.stockTicker = stockticker;
    }


    public String getStockTicker() {
        return stockTicker;
    }

    //public void refreshDailyPerformance(Date start, Date end) {
//        String u = "http://ichart.finance.yahoo.com/table.csv?s=";
//        u += getStockTicker();

//        System.out.println(start + " -> " + end);

//        int startMonth = start.getMonth() + 1;
//        int startDay = start.getDate();
//        int startYear = start.getYear() + 1900;
//        int endMonth = end.getMonth() + 1;
//        int endDay = end.getDate();
//        int endYear = end.getYear() + 1900;
//        u += "&a=0" + startMonth + "&b=" + startDay + "&c=" + startYear;
//        u += "&d=0" + endMonth + "&e=" + endDay + "&f=" + endYear + "&g=d&ignore=.csv";

    //}

    public static enum IntervalType {
        Daily, Monthly
    }

    /** daily performance */
    public void refreshLatestPerformance(IntervalType interval) {
        String u = "http://ichart.finance.yahoo.com/table.csv?s=";
        u += getStockTicker();

        if (interval == IntervalType.Monthly)
            u += "&g=m";

        try {
            URL url = new URL(u);
            BufferedReader in = new BufferedReader(
                new InputStreamReader(
                url.openStream()));

            String inputLine;

            String fieldsLine = in.readLine();

            //Date previous = null;
            //Date current;

            performance.clear();

            while ((inputLine = in.readLine()) != null) {
                //System.out.println(inputLine);
                String[] p = inputLine.split(",");
                String dateString = p[0];
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString, new ParsePosition(0));
                float open = Float.parseFloat(p[1]);
                float high = Float.parseFloat(p[2]);
                float low = Float.parseFloat(p[3]);
                float close = Float.parseFloat(p[4]);
                float volume = Float.parseFloat(p[5]);
                BusinessPerformance bp = new BusinessPerformance(date, open, high, low, close, volume);
                performance.add(bp);
                //System.out.println("  " + bp);
            }

            in.close();

        } catch (Exception ex) {
            Logger.getLogger(PublicBusiness.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public SortedSet<BusinessPerformance> getPerformance() {
        return performance;
    }

    public float getLow() {
        float lowest = Float.MAX_VALUE;
        for (BusinessPerformance bp : getPerformance()) {
            if (bp.low < lowest)
                lowest = bp.low;
        }
        return lowest;
    }
    public float getHigh() {
        float highest = Float.MIN_VALUE;
        for (BusinessPerformance bp : getPerformance()) {
            if (bp.high > highest)
                highest = bp.low;
        }
        return highest;
    }

    public static void main(String[] args) {
        PublicBusiness p = new PublicBusiness("GOOG");
        p.refreshLatestPerformance(IntervalType.Monthly);
        System.out.println(p.getPerformance());
    }

}
