package com.freelanceitlab.walletprototype;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
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

import helpers.ExchangeRestOperation;
import helpers.FormatDataAsJson;


/**
 * A simple {@link Fragment} subclass.
 */
public class ExchangeSubmitFragment extends Fragment {

    EditText sendAddressInput;
    EditText receiveAddressInput;

    HashMap<String, String> dataMap;

    public ExchangeSubmitFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_exchange_submit, container, false);

        // change action bar text
        getActivity().setTitle("Exchange Currency");

        sendAddressInput = (EditText) view.findViewById(R.id.sendAddressInput);
        receiveAddressInput = (EditText) view.findViewById(R.id.receiveAddressInput);

        // receive the data
        dataMap = new HashMap<String, String>();
        Bundle b = this.getArguments();
        if(b.getSerializable("data") != null) {
            dataMap = (HashMap<String, String>)b.getSerializable("data");
        }

        TextView description = (TextView) view.findViewById(R.id.description);
        TextView sendLabel = (TextView) view.findViewById(R.id.sendLabel);
        TextView receiveLabel = (TextView) view.findViewById(R.id.receiveLabel);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            description.setText(Html.fromHtml(dataMap.get("send_methodInfo"), Html.FROM_HTML_MODE_LEGACY));
            sendLabel.setText(Html.fromHtml(dataMap.get("send_level"), Html.FROM_HTML_MODE_LEGACY));
            receiveLabel.setText(Html.fromHtml(dataMap.get("receive_level"), Html.FROM_HTML_MODE_LEGACY));
        } else {
            description.setText(Html.fromHtml(dataMap.get("send_methodInfo")));
            sendLabel.setText(Html.fromHtml(dataMap.get("send_level")));
            receiveLabel.setText(Html.fromHtml(dataMap.get("receive_level")));
        }

        // submit functionality
        Button submit = (Button) view.findViewById(R.id.submitButton);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // set api config
                String apiURL = getString(R.string.apiURL);
                String apiKey = getString(R.string.apiKey);

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


                data.put("sender_identity", sendAddressInput.getText().toString());
                data.put("receiver_identity", receiveAddressInput.getText().toString());

                FormatDataAsJson transmitJsonData = new FormatDataAsJson(data);

                ExchangeRestOperation exchangeRestOperation = new ExchangeRestOperation(apiURL, apiKey);
                String response = exchangeRestOperation.save(transmitJsonData.jsonMap());

                Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();
                Log.v("Userdata", response);
            }
        });

        // active back button
        Button backButton = (Button) view.findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExchangeFragment exchangeFragment = new ExchangeFragment();

                Bundle bundle = new Bundle();
                bundle.putSerializable("data", dataMap);
                exchangeFragment.setArguments(bundle);

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.dashboard_container, exchangeFragment);
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
