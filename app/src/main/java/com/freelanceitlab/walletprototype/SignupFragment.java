package com.freelanceitlab.walletprototype;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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

public class SignupFragment extends Fragment {

    EditText name;
    EditText username;
    EditText email;
    EditText password;
    EditText referral_email;
    Spinner country;

    String dataObject;

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

        name = (EditText) view.findViewById(R.id.name);
        username = (EditText) view.findViewById(R.id.username);
        email = (EditText) view.findViewById(R.id.email);
        password = (EditText) view.findViewById(R.id.password);
        referral_email = (EditText) view.findViewById(R.id.referral_email);
        country = (Spinner) view.findViewById(R.id.country);

        Button signup = (Button) view.findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataObject = formatDataAsJson();
                sendDataToServer();
            }
        });

        return view;
    }

    private String formatDataAsJson() {
        final JSONObject root = new JSONObject();

        try {
            root.put("name", name.getText());
            root.put("username", username.getText());
            root.put("email", email.getText());
            root.put("password", password.getText());
            root.put("referral_email", referral_email.getText());
            root.put("country", country.getSelectedItem().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return root.toString();
    }

    private void sendDataToServer() {
        // final String data = formatDataAsJson();

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                return getServerResponse();
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

                Toast.makeText(getActivity().getApplicationContext(), result.toString(), Toast.LENGTH_LONG).show();
            }

        }.execute();

    }

    private String getServerResponse() {
        String key = "db5d8d6959ccb6288afffa1b018631f5";
        String userdata = getUserdata();
        String output = "";

        try {
            JSONObject object = new JSONObject(userdata);

            String username = object.getString("username");
            String password = object.getString("password");

            URL url = new URL("http://dbsewallet.com/api/permission/save?key=" + key + "&user=" + username + "&pass=" + password);
            String urlParameters = "details="  + URLEncoder.encode(dataObject.toString(), "UTF-8");

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
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return output;
    }

    private String getUserdata() {
        SharedPreferences sharedData = getActivity().getSharedPreferences("Userdata", Context.MODE_PRIVATE);
        String data = sharedData.getString("userdetails", "");

        return data;
    }

}
