package rafagonp.com.marvelcomics.logic;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by Rafa on 15/07/2016.
 */
public class BaseLogic {

    public static final String DEBUG_TAG = "DEBUG";
    private static final int BUFFER_SIZE = 4096;
    InputStream is = null;

    // Given a URL, establishes an HttpUrlConnection and retrieves
    // the web page content as a InputStream, which it returns as
    // a string.
    public String downloadUrl(String myurl, JSONObject postParameters) throws IOException {
        String result = "ok";
        HttpURLConnection urlConnection = null;

        HttpsURLConnection urlHttpsConnection = null;

        URL url = new URL(myurl);

        if (url.getProtocol().toLowerCase().equals("https")) {

            trustAllHosts();

            urlHttpsConnection = (HttpsURLConnection) url.openConnection();

            urlHttpsConnection.setHostnameVerifier(new NullHostNameVerifier());

            urlConnection = urlHttpsConnection;

        } else {
            urlConnection = (HttpURLConnection) url.openConnection();
        }


        try {
            urlConnection.setReadTimeout(5000 /* milliseconds */);
            urlConnection.setConnectTimeout(10000 /* milliseconds */);
            urlConnection.setDoInput(true);

            if(postParameters != null && postParameters.length() >= 1) {
                urlConnection.setDoOutput(true);
                //urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                OutputStreamWriter wr = new OutputStreamWriter((urlConnection.getOutputStream()));
                wr.write(postParameters.toString());
                wr.flush();
            }else{
                urlConnection.setRequestMethod("GET");
            }

            Log.d("REQUEST: ", myurl);
            // Starts the query
            urlConnection.connect();

            Log.d("CONNECTED", "BEFORE GETINPUTSTREAM");
            is = urlConnection.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = convertStreamToString(is);
            Log.d("CONTENTASSTRING: ", contentAsString);
            return contentAsString;



            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } catch (Exception e) {
            if(urlConnection != null && urlConnection.getErrorStream() != null) {
                BufferedReader rd = new BufferedReader(new InputStreamReader(urlConnection.getErrorStream()));
                StringBuilder data = new StringBuilder();
                String line;
                while ((line = rd.readLine()) != null) {
                    data.append(line);
                    data.append('\n');
                }
                result = data.toString();
            }else if(e != null && e.getMessage() != null && e.getMessage().equalsIgnoreCase("406")){
                result = "The maximum number of tickets per bids is 6 tickets";
            }
            e.printStackTrace();
        } finally {
            if (is != null) {
                is.close();
            }
        }

        return result;
    }

    public void downloadImage(String fileURL, String saveDir, String name)
            throws IOException {
        URL url = new URL(fileURL);
        HttpURLConnection urlConnection = null;


        trustAllHosts();

        urlConnection = (HttpURLConnection) url.openConnection();

        urlConnection.connect();
        int responseCode = urlConnection.getResponseCode();

        // always check HTTP response code first
        if (responseCode == HttpURLConnection.HTTP_OK) {
            String fileName = "";
            String disposition = urlConnection.getHeaderField("Content-Disposition");
            String contentType = urlConnection.getContentType();
            int contentLength = urlConnection.getContentLength();

            if (disposition != null) {
                // extracts file name from header field
                int index = disposition.indexOf("filename=");
                if (index > 0) {
                    fileName = disposition.substring(index + 10,
                            disposition.length() - 1);
                }
            } else {
                // extracts file name from URL
                fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1,
                        fileURL.length());
            }

            System.out.println("Content-Type = " + contentType);
            System.out.println("Content-Disposition = " + disposition);
            System.out.println("Content-Length = " + contentLength);
            System.out.println("fileName = " + name + ".png");

            // opens input stream from the HTTP connection
            InputStream inputStream = urlConnection.getInputStream();
            String saveFilePath = saveDir + File.separator + name + ".png";

            // opens an output stream to save into file
            FileOutputStream outputStream = new FileOutputStream(saveFilePath);

            int bytesRead = -1;
            byte[] buffer = new byte[BUFFER_SIZE];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.close();
            inputStream.close();

            System.out.println("File downloaded");
        } else {
            System.out.println("No file to download. Server replied HTTP code: " + responseCode);
        }
        urlConnection.disconnect();
    }

    public class NullHostNameVerifier implements HostnameVerifier {

        @Override
        public boolean verify(String hostname, SSLSession session) {
            Log.i("RestUtilImpl", "Approving certificate for " + hostname);
            return true;
        }

    }

    private static void trustAllHosts() {

        X509TrustManager easyTrustManager = new X509TrustManager() {

            public void checkClientTrusted(
                    X509Certificate[] chain,
                    String authType) throws CertificateException {
                // Oh, I am easy!
            }

            public void checkServerTrusted(
                    X509Certificate[] chain,
                    String authType) throws CertificateException {
                // Oh, I am easy!
            }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

        };

        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{easyTrustManager};

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");

            sc.init(null, trustAllCerts, new java.security.SecureRandom());

            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String convertStreamToString(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
        }
        return sb.toString();
    }

}
