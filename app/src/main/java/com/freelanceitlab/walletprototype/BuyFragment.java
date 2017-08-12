package com.freelanceitlab.walletprototype;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import helpers.BuyRESTOperation;
import helpers.FormatDataAsJson;
import helpers.PairCls;

public class BuyFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    public String[] ingredients = new String[3];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_buy, container, false);

        // change action bar text
        getActivity().setTitle("Buy Currency");

        // set api meta data
        String apiURL = getString(R.string.apiURL);
        String apiKey = getString(R.string.apiKey);

        // target the send spinner
        Spinner sendGatewayId       = (Spinner) view.findViewById(R.id.sendSpinner);
        Spinner receiveGatewayId    = (Spinner) view.findViewById(R.id.receiveGatewayId);

        BuyRESTOperation buyRESTOperation   = new BuyRESTOperation(apiURL, apiKey);
        JSONObject resultObject             = null;

        // Send Gateway
        try {
            String response     = buyRESTOperation.getSendMethods();
            resultObject        = new JSONObject(response);

            if(resultObject.length() > 0) {
                Iterator<?> keys            = resultObject.keys();
                ArrayList<PairCls> parList  = new ArrayList<>();

                while(keys.hasNext() ) {
                    String key = (String) keys.next();
                    parList.add(new PairCls(key, resultObject.getString(key)));
                }

                // Setup Spinner configuration
                ArrayAdapter<PairCls> adapter = new ArrayAdapter<PairCls>(getContext(), android.R.layout.simple_spinner_dropdown_item, parList);
                sendGatewayId.setAdapter(adapter);
                // sendGatewayId.setSelection(adapter.getPosition(myItem)); // Optional

                // sendGatewayId.setOnItemSelectedListener(this);
                sendGatewayId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        PairCls pairCls = (PairCls) parent.getSelectedItem();
                        // Toast.makeText(getContext(), "Key: " + pairCls.getId() + ",  Value : " + pairCls.getName(), Toast.LENGTH_SHORT).show();

                        ingredients[0] = pairCls.getId();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {}
                });

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // receive Gateway
        try {
            final String response     = buyRESTOperation.getReceiveMethods();
            resultObject        = new JSONObject(response);

            if(resultObject.length() > 0) {
                Iterator<?> keys                    = resultObject.keys();
                ArrayList<PairCls> receivePairList  = new ArrayList<>();

                while(keys.hasNext() ) {
                    String key = (String) keys.next();
                    receivePairList.add(new PairCls(key, resultObject.getString(key)));
                }

                // Setup Spinner configuration
                ArrayAdapter<PairCls> adapter = new ArrayAdapter<PairCls>(getContext(), android.R.layout.simple_spinner_dropdown_item, receivePairList);
                receiveGatewayId.setAdapter(adapter);
                // receiveGatewayId.setSelection(adapter.getPosition(myItem)); // Optional

                // receiveGatewayId.setOnItemSelectedListener(this);
                receiveGatewayId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        PairCls receivePairCls = (PairCls) parent.getSelectedItem();
                        // Toast.makeText(getContext(), "Key: " + receivePairCls.getId() + ",  Value : " + receivePairCls.getName(), Toast.LENGTH_SHORT).show();

                        ingredients[1] = receivePairCls.getId();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {}
                });

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // active readonly mode the BDT amount
        EditText receiveAmountId = (EditText) view.findViewById(R.id.receiveAmountId);
        receiveAmountId.setText("1.00");

        ingredients[2] = "1.00";

        // get the buy rate
        Button showBtn = (Button) view.findViewById(R.id.showButtonID);
        showBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get data from
                FormatDataAsJson formatDataAsJson = new FormatDataAsJson(new String[]{"send", "receive", "amount"}, ingredients);
                Log.v("Ingredients", formatDataAsJson.format());

                // BuyRESTOperation buyRESTOperation   = new BuyRESTOperation(apiURL, apiKey);
                // final String response     = buyRESTOperation.getReceiveMethods();
                // resultObject        = new JSONObject(response);
            }
        });

        // active readonly mode the BDT amount
        EditText sendAmountId = (EditText) view.findViewById(R.id.sendAmountId);
        sendAmountId.setEnabled(false);
        sendAmountId.setText("0.00");

        /*
        // text change event
        receiveAmountId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Toast.makeText(getContext(), "After Text Changed", Toast.LENGTH_LONG).show();

                FormatDataAsJson formatDataAsJson = new FormatDataAsJson(new String[]{"send", "receive", "amount"}, ingredients);
                Log.v("Ingredients", formatDataAsJson.format());
            }
        });
        */

        // active the next button
        Button nextBtn = (Button) view.findViewById(R.id.nextButtonId);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuySubmitFragment buySubmitFragment = new BuySubmitFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.dashboard_container, buySubmitFragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // PairCls pairCls = (PairCls) parent.getSelectedItem();
        // Toast.makeText(getContext(), "Key: " + pairCls.getId() + ",  Value : " + pairCls.getName(), Toast.LENGTH_SHORT).show();

        String itemSelected = parent.getItemAtPosition(position).toString();
        Toast.makeText(getContext(), itemSelected, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}
