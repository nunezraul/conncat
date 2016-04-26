package conncat.conncat;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by nunez on 4/3/2016.
 */
public class downloadXML {
    /**
     * Returns a string with the contents of the XML file in it.
     * The myurl argument must specify an absolute url.
     * The method tries to create a connection and download the
     * xml from the url. It than converts the inputstream into a string
     * and returns it.
     *
     * @param myurl a url pointing to the site with the xml
     * @return      the string with the contents of the xml

     */
    public String downloadXML(String... myurl) throws IOException {
        InputStream is = null;

        try {
            String xmlstring = "";
            for(String eventURL: myurl) {
                URL url = new URL(eventURL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // Starts the query
                conn.connect();
                int response = conn.getResponseCode();
                Log.d("XML Downloader", "The response is: " + response);
                is = conn.getInputStream();

                // Convert the InputStream into a string
                String contentAsString = readIt(is);
                Log.d("XML Downloader", "The response is: " + contentAsString);
                xmlstring += contentAsString;
                //InputStream content = is;
            }

            return xmlstring;


            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    /**
     * Converts an inputstream into a string
     *
     * @param stream    The input stream that is to be converted into a string
     * @return          The string with the content of the input stream
     * @throws IOException
     * @throws UnsupportedEncodingException
     */
    public String readIt(InputStream stream) throws IOException, UnsupportedEncodingException {
        /*Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);*/


        BufferedReader r = new BufferedReader(new InputStreamReader(stream));
        StringBuilder total = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) {
            total.append(line);
        }
        return total.toString();
    }

}
