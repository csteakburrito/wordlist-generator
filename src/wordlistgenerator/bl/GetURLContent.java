/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wordlistgenerator.bl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 *
 * @author Abdullah
 */
public class GetURLContent {

    public static String getURLContent(String strUrl) {
        URL url;

        try {
            // get URL content
            url = new URL(strUrl);
            URLConnection conn = url.openConnection();

            // open the stream and put it into BufferedReader
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));

            //BufferedReader br1 = br.
            deTag d = new deTag(br);
            d.parse();
            System.out.println(d.toString());

            br.close();

            System.out.println("Done");
            return d.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
}
