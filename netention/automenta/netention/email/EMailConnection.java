/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.email;

import automenta.netention.swing.widget.email.MessageDialog;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.Properties;
import javax.mail.Address;
import javax.mail.FetchProfile;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author seh
 */
public class EMailConnection implements Serializable {

    public String server;
    public String smtpServer;
    public String username;
    public String password;
    int GMAIL_SMTP_PORT = 465;
    private Session session;
    private Store store;

    public EMailConnection() {
        setDefaults();
    }

    protected void setDefaults() {
        server = "imap.gmail.com";
        smtpServer = "smtp.gmail.com";
        username = "@gmail.com";
        password = "";
    }

    public EMailConnection(final String filename) {

        try {
            EMailConnection ec = load(filename);
            copyFrom(ec);
        } catch (Exception e) {
            setDefaults();
        }

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    save(filename);
                } catch (Exception ex) {
                    System.err.println(ex);
                    ex.printStackTrace();
                }
            }
        }));
    }

    public void copyFrom(EMailConnection e) {
        setUsername(e.username);
        setPassword(e.password);
        setSmtpServer(e.smtpServer);
        setServer(e.server);
    }

    public static EMailConnection load(String filename) throws IOException {
        FileReader fr = new FileReader(filename);
        EMailConnection ec = new JSONDeserializer<EMailConnection>().use(EMailConnection.class.toString(), EMailConnection.class).deserialize(fr);
        fr.close();
        return ec;
    }

    public void save(String filename) throws IOException {
        FileWriter fw = new FileWriter(new File(filename));
        new JSONSerializer().serialize(this, fw);
        fw.close();
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
        folder.fetch(messages, profile);
        return messages;

    }
}
