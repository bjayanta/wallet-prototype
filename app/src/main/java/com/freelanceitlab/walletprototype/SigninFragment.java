package com.freelanceitlab.walletprototype;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SigninFragment extends Fragment {

    String username;
    String password;

    // api meta variable
    String apiURL;
    String apiKey;

    // initialize response variable
    public String resultset;

    ProgressDialog preloader;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signin, container, false);

        final EditText usernameEditText = (EditText) view.findViewById(R.id.username);
        final EditText passwordEditText = (EditText) view.findViewById(R.id.password);

        // set api meta data
        apiURL = getString(R.string.apiURL) + "authentication/signin?";
        apiKey = getString(R.string.apiKey);

        final Button signinBtn = (Button) view.findViewById(R.id.signinBtn);
        signinBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // check the internet connection
                if(isConnected(getActivity())) {
                    username = usernameEditText.getText().toString().trim();
                    password = passwordEditText.getText().toString().trim();

                    // login validation
                    if(username.equals("")) {
                        usernameEditText.setError("Username is required!");
                    } else {
                        if(password.equals("")) {
                            passwordEditText.setError("Password is required!");
                        } else {
                            // get the user data from server
                            apiURL += "key=" + apiKey;
                            apiURL += "&user=" + username;
                            apiURL += "&pass=" + password;

                            new RESTOperation().execute(apiURL);
                        }
                    }
                } else {
                    Toast.makeText(getContext(), "Connection not available!", Toast.LENGTH_LONG).show();
                }
            }
        });

        Button backBtn = (Button) view.findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                HomeFragment homeFragment = new HomeFragment();
                ft.replace(R.id.fragment_container, homeFragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        return view;
    }

    // check the internet connection
    public boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            connectivityManager.getActiveNetworkInfo();
            android.net.NetworkInfo wifi = connectivityManager.getNetworkInfo(connectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = connectivityManager.getNetworkInfo(connectivityManager.TYPE_MOBILE);

            if((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting())) return true;
            else return false;
        } else {
            return false;
        }
    }

    public class RESTOperation extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // Define and call the onPreExecute method for the preloader
            preloader = new ProgressDialog(getActivity());
            preloader.setTitle("Authentication Process Running !");
            preloader.setMessage("Please wait...");
            preloader.setCancelable(false);
            preloader.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (preloader.isShowing()) {
                try {
                    JSONObject object = new JSONObject(s);

                    if (object != null) {
                        // get the permission
                        int permission = Integer.parseInt(object.getString("permission"));
                        int error = Integer.parseInt(object.getString("error"));
                        String description = object.getString("description");

                        if (permission == 1 && error == 0) {
                            // set the user data
                            String data = object.getString("userinfo");
                            setUserdata(data);

                            // set notice data
                            String notice = object.getString("notice");
                            setNoticedata(notice);

                            // run dashboard activity
                            Intent dashboardIntent = new Intent(getContext(), DashboardActivity.class);
                            startActivity(dashboardIntent);

                            // kill the current activity
                            getActivity().finish();
                        } else {
                            Toast.makeText(getContext(), description, Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                preloader.dismiss();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                con.setRequestMethod("GET");
                con.connect();

                BufferedReader bf = new BufferedReader(new InputStreamReader(con.getInputStream()));
                resultset = bf.readLine();
                Log.v("abc", params[0]);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return resultset;
        }
    }

    private void setUserdata(String data) {
        SharedPreferences sharedData = getActivity().getSharedPreferences("Userdata", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedData.edit();

        editor.putString("userdetails", data);
        editor.apply();
    }

    private void setNoticedata(String data) {
        SharedPreferences sharedData = getActivity().getSharedPreferences("Noticedata", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedData.edit();

        editor.putString("noticedetails", data);
        editor.apply();
    }

}
