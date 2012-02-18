/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package automenta.netention.rss;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

/**
 *
 * @author seh
 */
public class HTTP {

    public static String getURL(String url) {
        try {
            URL oracle = new URL(url);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                    oracle.openStream()));

            StringBuffer s = new StringBuffer();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                s.append(inputLine);
                s.append("\n");
            }

            in.close();
            return s.toString();
        }
        catch (IOException i) {
            return "";
        }
    }

    public static void saveURL(String url, String outputPath) throws IOException {
        URL u = new URL(url);
        ReadableByteChannel rbc = Channels.newChannel(u.openStream());
        FileOutputStream fos = new FileOutputStream(outputPath);
        fos.getChannel().transferFrom(rbc, 0, 1 << 24);
    }
}
