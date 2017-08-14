package com.freelanceitlab.walletprototype;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import helpers.BuyRESTOperation;
import helpers.FormatDataAsJson;

public class BuySubmitFragment extends Fragment {

    String apiURL;
    String apiKey;

    HashMap<String, String> dataMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_buy_submit, container, false);

        // change action bar text
        getActivity().setTitle("Buy Currency");

        // receive the data
        dataMap = new HashMap<String, String>();
        Bundle b = this.getArguments();
        if(b.getSerializable("data") != null) {
            dataMap = (HashMap<String, String>)b.getSerializable("data");
        }

        // set label
        TextView buyInfoId = (TextView) view.findViewById(R.id.buyInfoId);
        TextView sendAddressLabelId = (TextView) view.findViewById(R.id.sendAddressLabelId);
        TextView sendTrxId = (TextView) view.findViewById(R.id.sendTrxId);
        TextView indentifireId = (TextView) view.findViewById(R.id.indentifireId);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            buyInfoId.setText(Html.fromHtml(dataMap.get("buy_info"), Html.FROM_HTML_MODE_LEGACY));
            sendAddressLabelId.setText(Html.fromHtml(dataMap.get("send_label"), Html.FROM_HTML_MODE_LEGACY));
            sendTrxId.setText(Html.fromHtml(dataMap.get("trx_label"), Html.FROM_HTML_MODE_LEGACY));
            indentifireId.setText(Html.fromHtml(dataMap.get("receive_label"), Html.FROM_HTML_MODE_LEGACY));
        } else {
            buyInfoId.setText(Html.fromHtml(dataMap.get("buy_info")));
            sendAddressLabelId.setText(Html.fromHtml(dataMap.get("send_label")));
            sendTrxId.setText(Html.fromHtml(dataMap.get("trx_label")));
            indentifireId.setText(Html.fromHtml(dataMap.get("receive_label")));
        }

        View viewsContainer = view.findViewById(R.id.viewsContainer);
        if(dataMap.get("trx_label").equals("")) {
            viewsContainer.setVisibility(View.GONE);
        }

        // Log.v("Data", String.valueOf(dataMap));
        // Toast.makeText(getContext(), String.valueOf(dataMap), Toast.LENGTH_LONG).show();

        // active submit button
        Button submitButton = (Button) view.findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // set api config
                apiURL = getString(R.string.apiURL);
                apiKey = getString(R.string.apiKey);

                // set data
                HashMap<String, String> data = new HashMap<String, String>();

                data.put("send_method", dataMap.get("send_method"));
                data.put("send_amount", dataMap.get("send_amount"));
                data.put("receive_method", dataMap.get("receive_method"));
                data.put("receive_amount", dataMap.get("receive_amount"));

                try {
                    JSONObject userdata = new JSONObject(setUserdata());

                    data.put("user_id", userdata.getString("id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                EditText sendAddressInput = (EditText) view.findViewById(R.id.sendAddressInputId);
                data.put("sender_identity", sendAddressInput.getText().toString());

                EditText sendTrxInput = (EditText) view.findViewById(R.id.sendTrxInputId);
                if(!(dataMap.get("trx_label")).equals("")) {
                    data.put("trx_number", sendTrxInput.getText().toString());
                }

                EditText indentifireInput = (EditText) view.findViewById(R.id.indentifireInputId);
                data.put("receiver_identity", indentifireInput.getText().toString());

                FormatDataAsJson transmitJsonData = new FormatDataAsJson(data);

                BuyRESTOperation buyRESTOperation   = new BuyRESTOperation(apiURL, apiKey);
                String response = buyRESTOperation.save(transmitJsonData.jsonMap());
                Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();

                Log.v("Userdata", response);
            }
        });

        // active back button
        Button backButton = (Button) view.findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuyFragment buyFragment = new BuyFragment();

                Bundle bundle = new Bundle();
                bundle.putSerializable("data", dataMap);
                buyFragment.setArguments(bundle);

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.dashboard_container, buyFragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        return view;
    }

    private String setUserdata() {
        SharedPreferences sharedData = getActivity().getSharedPreferences("Userdata", Context.MODE_PRIVATE);
        String data = sharedData.getString("userdetails", "");

        return data;
    }

}
