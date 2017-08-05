package com.freelanceitlab.walletprototype;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

import helpers.FormatDataAsJson;
import helpers.Validation;

public class SignupFragment extends Fragment {

    EditText name;
    EditText username;
    EditText email;
    EditText password;
    EditText referral_email;
    Spinner country;
    String dataset;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        Button backBtn = (Button) view.findViewById(R.id.back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                HomeFragment homeFragment = new HomeFragment();
                ft.replace(R.id.fragment_container, homeFragment);
                ft.commit();
            }
        });

        name            = (EditText) view.findViewById(R.id.name);
        username        = (EditText) view.findViewById(R.id.username);
        email           = (EditText) view.findViewById(R.id.email);
        password        = (EditText) view.findViewById(R.id.password);
        referral_email  = (EditText) view.findViewById(R.id.referral_email);
        country         = (Spinner) view.findViewById(R.id.country);

        Button signup = (Button) view.findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] fields = {"name", "username", "email", "password", "referral_email", "country"};
                String[] inputs = {
                        name.getText().toString().trim(),
                        username.getText().toString().trim(),
                        email.getText().toString().trim(),
                        password.getText().toString().trim(),
                        referral_email.getText().toString().trim(),
                        country.getSelectedItem().toString().trim()
                };

                // set json string
                FormatDataAsJson formatDataAsJson = new FormatDataAsJson(fields, inputs);
                dataset = formatDataAsJson.format();
                // Toast.makeText(getActivity(), dataset, Toast.LENGTH_LONG).show();

                // singup validation
                Validation validation = new Validation(dataset);

                validation.input("name", "Full Name", "empty|name");
                validation.input("username", "Username", "empty|unique.registration.username");
                validation.input("email", "Email", "empty|email|unique.registration.email");
                validation.input("password", "Password", "empty|alphanumeric");
                validation.input("referral_email", "Referral Email", "empty|email");
                validation.input("country", "Country", "empty");

                if(validation.run() == false) {
                    try {
                        // set the error
                        JSONObject errorObject = new JSONObject(validation.error());
                        Iterator<?> keys = errorObject.keys();

                        while(keys.hasNext() ) {
                            String key = (String) keys.next();

                            if(key.equals("name")){ name.setError(errorObject.getString(key)); }
                            if(key.equals("username")){ username.setError(errorObject.getString(key)); }
                            if(key.equals("email")){ email.setError(errorObject.getString(key)); }
                            if(key.equals("password")){ password.setError(errorObject.getString(key)); }
                            if(key.equals("referral_email")){ referral_email.setError(errorObject.getString(key)); }
                            if(key.equals("country")){
                                /**
                                 * Spinner to TextView:
                                 * TextView countryText = (TextView) country.getSelectedView();
                                 * countryText.setError(errorObject.getString(key));
                                 */

                                Toast.makeText(getContext(), errorObject.getString(key), Toast.LENGTH_LONG).show();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    String response = sendDataToServer();
                    Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }

    private String sendDataToServer() {
        String response = null;

        try {
            response = new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... params) {
                    return getServerResponse();
                }

                @Override
                protected void onPostExecute(String result) {
                    super.onPostExecute(result);
                    // Toast.makeText(getActivity().getApplicationContext(), result.toString(), Toast.LENGTH_LONG).show();
                }

            }.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return response;
    }

    private String getServerResponse() {
        String key = "db5d8d6959ccb6288afffa1b018631f5";
        String output = "";

        URL url = null;
        try {
            url = new URL("http://dbsewallet.com/api/permission/signup?key=" + key);
            String urlParameters = "details="  + URLEncoder.encode(dataset.toString(), "UTF-8");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("USER-AGENT", "Android");
            connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");

            connection.setDoOutput(true);
            DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
            dStream.writeBytes(urlParameters);
            dStream.flush();
            dStream.close();

            int responseCode = connection.getResponseCode();
            output += "Response URL " + url;
            output += System.getProperty("line.separator") + "Request Parameters " + urlParameters;
            output += System.getProperty("line.separator") + "Response Code " + responseCode;

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = "";
            StringBuilder responseOutput = new StringBuilder();

            while ((line = br.readLine()) != null) {
                responseOutput.append(line);
            }
            br.close();

            output += System.getProperty("line.separator") + responseOutput.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.v("response", output);
        return output;
    }

    private String getUserdata() {
        SharedPreferences sharedData = getActivity().getSharedPreferences("Userdata", Context.MODE_PRIVATE);
        String data = sharedData.getString("userdetails", "");

        return data;
    }

}
