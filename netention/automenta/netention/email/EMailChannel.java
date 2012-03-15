/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.email;

import automenta.netention.NMessage;
import automenta.netention.swing.widget.email.MessageDialog;
import java.io.Serializable;
import java.util.Date;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author seh
 */
public class EMailChannel implements Serializable {

    public String server;
    public String smtpServer;
    public String username;
    public String password;
    
    static final int GMAIL_SMTP_PORT = 465;
    transient private Session session;
    transient private Store store;
    private String from;

    public EMailChannel() {
        setDefaults();
    }

    protected void setDefaults() {
        server = "imap.gmail.com";
        smtpServer = "smtp.gmail.com";
        username = "@gmail.com";
        password = "";
        from = "";
    }


    public void setServer(String s) {
        this.server = s;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSmtpServer(String smtpServer) {
        this.smtpServer = smtpServer;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public String getServer() {
        return server;
    }

    public String getSmtpServer() {
        return smtpServer;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return getServer() + ", " + getUsername() + ", " + getSmtpServer();
    }

    public void sendMessage(NMessage m) throws Exception {
        ensureConnected();
        
        // Create a new message with values from dialog.
        MimeMessage newMessage = new MimeMessage(session);
        newMessage.setFrom(new InternetAddress(from /* m.getFrom() */));

        newMessage.setSubject(m.getName());
        newMessage.setSentDate(m.getWhen());
        newMessage.setContent(m.getContent(), "text/html");
        
        final Address[] recipientAddresses = InternetAddress.parse(m.getTo());
        newMessage.setRecipients(Message.RecipientType.TO, recipientAddresses);

        Transport transport = session.getTransport("smtps");
        transport.connect(getSmtpServer(), GMAIL_SMTP_PORT, getUsername(), getPassword());
        transport.sendMessage(newMessage, recipientAddresses);
        transport.close();
    }
    
    // Send the specified message.
    public void sendMessage(int type, Message message) throws Exception {
        // Display message dialog to get message values.
        MessageDialog dialog;
        dialog = new MessageDialog(this, null, type, message);
        if (!dialog.display()) {
            // Return if dialog was cancelled.
            return;
        }


        // Create a new message with values from dialog.
        Message newMessage = new MimeMessage(session);
        newMessage.setFrom(new InternetAddress(dialog.getFrom()));

        newMessage.setSubject(dialog.getSubject());
        newMessage.setSentDate(new Date());
        newMessage.setText(dialog.getContent());

        final Address[] recipientAddresses = InternetAddress.parse(dialog.getTo());
        newMessage.setRecipients(Message.RecipientType.TO, recipientAddresses);

        Transport transport = session.getTransport("smtps");
        transport.connect(getSmtpServer(), GMAIL_SMTP_PORT, getUsername(), getPassword());
        transport.sendMessage(newMessage, recipientAddresses);
        transport.close();
    }
    
    public void ensureConnected() throws Exception {
        if (session == null) {
            Properties props = System.getProperties();
            props.setProperty("mail.store.protocol", "imaps");
            props.setProperty("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.setProperty("mail.imap.socketFactory.fallback", "false");
            props.put("mail.smtp.host", getSmtpServer());
            session = Session.getDefaultInstance(props, null);
            store = session.getStore("imaps");
            try {
                store.connect(getServer(), getUsername(), getPassword());
            } catch (MessagingException e) {
                e.printStackTrace();
                System.exit(2);
            }
        }
    }


    public synchronized void connect(Properties props) throws Exception {
        String username = getUsername();
        String password = getPassword();
        String server = getServer();

        props.put("mail.smtp.host", getSmtpServer());

        session = Session.getDefaultInstance(props, null);
        store = session.getStore("imaps");

        //smtps://username%40gmail.com@smtp.gmail.com/        

        try {
            store.connect(server, username, password);
        } catch (MessagingException e) {
            e.printStackTrace();
            System.exit(2);
        }
    }

    public Folder getFolder(String f) throws MessagingException {
        return store.getFolder(f);
    }

    public Message[] getLatestMessages(String theFolder, int num) throws MessagingException {
        // Open main "INBOX" folder.
        Folder folder = getFolder(theFolder);
        folder.open(Folder.READ_WRITE);


        int totalMessages = folder.getMessageCount();

        // Get folder's list of messages.
        Message[] messages = folder.getMessages(totalMessages - num, totalMessages);

        // Retrieve message headers for each message in folder.
        FetchProfile profile = new FetchProfile();
        profile.add(FetchProfile.Item.ENVELOPE);
        profile.add(FetchProfile.Item.CONTENT_INFO);
        folder.fetch(messages, profile);
        return messages;

    }

    public void setFrom(String f) {
        this.from = f;
    }
}
