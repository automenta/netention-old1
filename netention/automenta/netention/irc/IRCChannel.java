/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package automenta.netention.irc;

/**
 *
 * @author seh
 */
public abstract class IRCChannel {
    private IRCServerConnection connection;
    private String channel;

    public void onConnect(IRCServerConnection s, String channel) {
        this.connection = s;
        this.channel = channel;
    }

    public abstract void onMessage(String sender, String login, String hostname, String message);

    public void leave() {
        connection.partChannel(channel);
    }

    public void rejoin() {
        connection.joinChannel(channel);
    }

    public void stop() {
        leave();
        connection.leave(channel);
    }

}
