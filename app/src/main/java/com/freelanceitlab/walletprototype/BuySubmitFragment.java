package com.freelanceitlab.walletprototype;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
        View view = inflater.inflate(R.layout.fragment_buy_submit, container, false);

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

        // Log.v("Data", String.valueOf(dataMap));
        // Toast.makeText(getContext(), String.valueOf(dataMap), Toast.LENGTH_LONG).show();

        // active submit button
        Button submitButton = (Button) view.findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FormatDataAsJson formatDataAsJson = new FormatDataAsJson(dataMap);

                Log.v("Data", formatDataAsJson.jsonMap());
                Toast.makeText(getContext(), formatDataAsJson.jsonMap(), Toast.LENGTH_LONG).show();

                // get data from
                // BuyRESTOperation buyRESTOperation   = new BuyRESTOperation(apiURL, apiKey);
                // final String response = buyRESTOperation.getAmount(formatDataAsJson.format());
            }
        });

        // active back button
        Button backButton = (Button) view.findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuyFragment buyFragment = new BuyFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.dashboard_container, buyFragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        return view;
    }

}
