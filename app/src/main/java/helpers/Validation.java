package helpers;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

/**
 * Created by Jayanta on 8/2/2017.
 * Validation module
 * Terms: empty, alphabet, email, unique, numeric, alphanumeric, name
 *
 */

public class Validation {
    JSONObject root                 = new JSONObject();
    JSONObject error                = new JSONObject();

    final String alphabetRegex      = "[a-zA-Z]+";
    final String numericRegex       = "[0-9]+";
    final String nameRegex          = "^\\p{L}+(?: \\p{L}+)*$";
    final String alphaNumericRegex  = "[a-zA-Z0-9]+";
    final String emailRegex         = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    String apikey                   = "db5d8d6959ccb6288afffa1b018631f5";
    String urlstr                   = "http://dbsewallet.com/api/permission/isUnique";


    public Validation(String data) {
        try {
            JSONObject request = new JSONObject(data);

            if(request != null) {
                this.root = request;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void input(String name, String beauty, String expression) {
        String[] evolution = expression.split("\\|");

        for (int i = 0; i < evolution.length; i++) {
            check(name, beauty, evolution[i]);
        }
    }

    public boolean run() {
        if(error.length() > 0) {
            return false;
        }

        return true;
    }

    public String error() {
        return error.toString();
    }

    private void check(String field, String beauty, String expression) {
        String description  = getError(field);
        String term = expression;
        String table = null;
        String column = null;

        if (description.equals("")) {
            description = beauty;
        }

        if(expression.indexOf('.') > 0) {
            String[] termType   = expression.split("\\.");
            term                = termType[0];
            table               = termType[1];
            column              = termType[2];
        }

        try {
            String input = this.root.getString(field).trim();

            switch (term) {
                case "empty":
                    if(input.equals("")) {
                        description += " Field is Required!";

                        // set the errors
                        setError(field, description);
                    }

                    break;
                case "alphabet":
                    if(!input.matches(alphabetRegex)) {
                        description += " Alphabet only!";

                        // set the errors
                        setError(field, description);
                    }

                    break;
                case "alphanumeric":
                    if(!input.matches(alphaNumericRegex)) {
                        description += " Alphabet and Number only!";

                        // set the errors
                        setError(field, description);
                    }

                    break;
                case "name":
                    if(!input.matches(nameRegex)) {
                        description += " Please Enter a valid String!";

                        // set the errors
                        setError(field, description);
                    }

                    break;
                case "numeric":
                    if(!input.matches(numericRegex)) {
                        description += " Number only!";

                        // set the errors
                        setError(field, description);
                    }

                    break;
                case "email":
                    if(!(input.matches(emailRegex))) {
                        description += " Please Enter a valid Email!";

                        // set the errors
                        setError(field, description);
                    }

                    break;
                case "unique":
                    JSONObject responseObject = new JSONObject(sendDataToServer(table, column, input));
                    if(responseObject.getString("status").equals("1")) {
                        description += responseObject.getString("msg");

                        // set the errors
                        setError(field, description);
                    }

                    break;
                case "list":
                    if(input.equals("Select")) {
                        description += " Select any one from the list bellow!";

                        // set the errors
                        setError(field, description);
                    }
                    break;
                default:
                    // do anything else

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setError(String field, String description) {
        try {
            error.put(field, description);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String getError(String field) {
        String description = "";

        try {
            description = (error.getString(field).equals("")) ? "" : error.getString(field);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return description;
    }

    private String sendDataToServer(final String table, final String column, final String value) {
        String result = null;

        try {
            result = new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... params) {
                    return getServerResponse(table, column, value);
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

        return result;
    }

    private String getServerResponse(String table, String column, String value) {
        String output = "";

        try {
            URL url = new URL(urlstr + "?key=" + apikey);
            String urlParameters = "table=" + table + "&column=" + column + "&value=" + value;

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("USER-AGENT", "Android");
            connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");

            // send request
            connection.setDoOutput(true);
            DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
            dStream.writeBytes(urlParameters);
            dStream.flush();
            dStream.close();

            // receive request
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            output += br.readLine();

            // Log.v("Response", output);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return output;
    }

}
