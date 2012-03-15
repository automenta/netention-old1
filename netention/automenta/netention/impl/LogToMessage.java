/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.impl;

import automenta.netention.NMessage;
import automenta.netention.Self;
import java.util.Date;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * Adds a logger handler that converts log messages into NMessage's
 * @author seh
 */
public class LogToMessage {
    final static Logger logger = Logger.getLogger(LogToMessage.class.toString());
    
    private Handler handler;
    private final Self self;

    public LogToMessage(Self self) {
        super();
        
        this.self = self;
        
        start();
    }

    public void start() {
        if (handler!=null)
            stop();
        
        logger.getParent().addHandler(handler = new Handler() {

            @Override
            public synchronized void publish(LogRecord record) {
                final String name = record.getLoggerName();
                final String message = record.getMessage();
                final String title = name;
                
                NMessage m = new NMessage(record.getLoggerName() +": " + message, name, self.getID(), new Date(record.getMillis()));
                m.setFrom(record.getLoggerName());
                self.addDetail(m);
            }

            @Override
            public void flush() {
            }

            @Override
            public void close() throws SecurityException {
            }
            
        }); 
        handler.setLevel(Level.ALL);
        handler.setFilter(null);
        
        logger.info("started");
    }
    
    public void stop() {
        if (handler!=null) {
           Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).removeHandler(handler);
           handler = null; 
        }
    }
    
}
