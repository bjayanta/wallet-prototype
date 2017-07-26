package com.freelanceitlab.walletprototype;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class LandingFragment extends Fragment {

    String attention = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_landing, container, false);

        // change action bar text
        getActivity().setTitle("Dashboard");

        // get user data
        String userdata = setUserdata();
        try {
            JSONObject userObj = new JSONObject(userdata);

            if(userObj != null) {
                // set the username
                String username = userObj.getString("name");
                TextView hello = (TextView) view.findViewById(R.id.greeting);
                hello.setText("Hello " + username);

                // set the Referral Link
                String referralLink = userObj.getString("self_link");
                TextView referralLinkView = (TextView) view.findViewById(R.id.referralLink);
                referralLinkView.setText(referralLink);

                // referred person quantity
                String referredQuantity = userObj.getString("referred_quantity");
                TextView referredQuantityView = (TextView) view.findViewById(R.id.referredQuantity);
                referredQuantityView.setText(referredQuantity + " Times");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // get the attention
        String noticedata = setNoticedata();
        try {
            JSONObject object = new JSONObject(noticedata);

            if(object != null) {
                attention = object.getString("attention");

                JSONObject attentionObj = new JSONObject(attention);
                String title = attentionObj.getString("title");
                String description = attentionObj.getString("description");

                TextView attentionDescription = (TextView) view.findViewById(R.id.attentionDescription);
                attentionDescription.setText(description);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return view;
    }

    private String setUserdata() {
        SharedPreferences sharedData = getActivity().getSharedPreferences("Userdata", Context.MODE_PRIVATE);
        String data = sharedData.getString("userdetails", "");

        return data;
    }

    private String setNoticedata() {
        SharedPreferences sharedData = getActivity().getSharedPreferences("Noticedata", Context.MODE_PRIVATE);
        String data = sharedData.getString("noticedetails", "");

        return data;
    }

}
