package helpers;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;

/**
 * Created by Jayanta on 8/7/2017.
 */

public class BuyRESTOperation {

    private String dataset;

    public String getSendMethods() {
        connection("POST", "getSendMethods", null);

        return dataset;
    }

    private void connection(final String method, final String segment, final String data) {

        try {
            dataset = new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... params) {
                    return getResponse(method, segment, data);
                }

                @Override
                protected void onPostExecute(String result) {
                    super.onPostExecute(result);
                }
            }.execute().get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    private String getResponse(String method, String segment, String data) {
        // initialize StringBuilder
        StringBuilder responseOutput    = new StringBuilder();

        // set default url
        String apiurl                   = "http://dbsewallet.com/api/buyapi/" + segment;

        // set default variables
        String line                     = null;
        String response                 = null;
        String result                   = null;
        String requestParameters        = null;

        try {
            URL url = new URL(apiurl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod(method);
            connection.setRequestProperty("USER-AGENT", "Android");
            connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");

            connection.setDoOutput(true);

            if(data != null) {
                requestParameters = "details="  + URLEncoder.encode(data.toString(), "UTF-8");
                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());

                dStream.writeBytes(requestParameters);
                dStream.flush();
                dStream.close();
            }

            int responseCode = connection.getResponseCode();
            response += "Response URL " + url;
            response += System.getProperty("line.separator") + "Request Parameters " + requestParameters;
            response += System.getProperty("line.separator") + "Response Code " + responseCode;

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            while ((line = br.readLine()) != null) {
                responseOutput.append(line);
            }
            br.close();

            response += System.getProperty("line.separator") + responseOutput.toString();
            result = responseOutput.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }


}