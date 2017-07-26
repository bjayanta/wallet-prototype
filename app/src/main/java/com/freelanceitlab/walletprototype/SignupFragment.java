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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SignupFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        TextView textView = (TextView) view.findViewById(R.id.back);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                HomeFragment homeFragment = new HomeFragment();
                ft.replace(R.id.fragment_container, homeFragment);
                ft.commit();
            }
        });

        Button signup = (Button) view.findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDataToServer();
            }
        });

        return view;
    }

    private String formatDataAsJson() {
        final JSONObject root = new JSONObject();

        try {
            root.put("name", "Jayanta Biswas");
            root.put("email", "bjayanta.neo@gmail.com");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return root.toString();
    }

    private void sendDataToServer() {
        final String data = formatDataAsJson();

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                return getServerResponse(data);
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

                Toast.makeText(getActivity().getApplicationContext(), result.toString(), Toast.LENGTH_LONG).show();
            }

        }.execute();

    }

    private String getServerResponse(String data) {
        String key = "db5d8d6959ccb6288afffa1b018631f5";
        String userdata = getUserdata();

        try {
            JSONObject object = new JSONObject(userdata);

            String username = object.getString("username");
            String password = object.getString("password");

            URL url = new URL("http://dbsewallet.com/api/permission/save?key=" + key + "&user=" + username + "&pass=" + password);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("USER-AGENT", "Android");
            connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");



        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String getUserdata() {
        SharedPreferences sharedData = getActivity().getSharedPreferences("Userdata", Context.MODE_PRIVATE);
        String data = sharedData.getString("userdetails", "");

        return data;
    }

}
