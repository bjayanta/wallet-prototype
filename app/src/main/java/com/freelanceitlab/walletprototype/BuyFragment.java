package com.freelanceitlab.walletprototype;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import java.util.HashMap;
import java.util.Iterator;

import helpers.BuyRESTOperation;
import helpers.FormatDataAsJson;
import helpers.PairCls;

public class BuyFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    String apiURL;
    String apiKey;
    public String[] ingredients = new String[3];

    EditText sendAmountId;
    EditText receiveAmountId;

    // public String[] emit = new String[3];
    HashMap<String, String> emit = new HashMap<String, String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_buy, container, false);

        // change action bar text
        getActivity().setTitle("Buy Currency");

        // set api meta data
        apiURL = getString(R.string.apiURL);
        apiKey = getString(R.string.apiKey);

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
                        emit.put("send_method", pairCls.getId());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        PairCls pairCls = (PairCls) parent.getSelectedItem();

                        ingredients[0] = pairCls.getId();
                        emit.put("send_method", pairCls.getId());
                    }
                });

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // receive Gateway
        try {
            final String response     = buyRESTOperation.getReceiveMethods();
            resultObject                = new JSONObject(response);

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
                        emit.put("receive_method", receivePairCls.getId());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        PairCls receivePairCls = (PairCls) parent.getSelectedItem();

                        ingredients[1] = receivePairCls.getId();
                        emit.put("receive_method", receivePairCls.getId());
                    }
                });
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // active readonly mode the BDT amount
        receiveAmountId = (EditText) view.findViewById(R.id.receiveAmountId);
        receiveAmountId.setText("1.00");

        // active readonly mode the BDT amount
        sendAmountId = (EditText) view.findViewById(R.id.sendAmountId);
        sendAmountId.setEnabled(false);
        sendAmountId.setText("0.00");

        // get the buy rate
        Button showBtn = (Button) view.findViewById(R.id.showButtonID);
        showBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ingredients[2] = String.valueOf(receiveAmountId.getText());
                emit.put("receive_amount", String.valueOf(receiveAmountId.getText()));

                // get data from
                FormatDataAsJson formatDataAsJson = new FormatDataAsJson(new String[]{"send", "receive", "amount"}, ingredients);

                BuyRESTOperation buyRESTOperation   = new BuyRESTOperation(apiURL, apiKey);
                final String response = buyRESTOperation.getAmount(formatDataAsJson.format());
                try {
                    JSONObject resultObject = new JSONObject(response);
                    sendAmountId.setText(resultObject.getString("send_amount"));

                    emit.put("send_amount", resultObject.getString("send_amount"));
                    emit.put("buy_info", resultObject.getString("send_methodInfo"));
                    emit.put("send_label", resultObject.getString("send_placeholder"));
                    emit.put("trx_label", resultObject.getString("send_TrxPlaceholder"));
                    emit.put("receive_label", resultObject.getString("receive_placeholder"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        // active the next button
        Button nextBtn = (Button) view.findViewById(R.id.nextButtonId);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // collect data
                emit.put("send_currency", "BDT");
                emit.put("receive_currency", "USD");

                BuySubmitFragment buySubmitFragment = new BuySubmitFragment();

                Bundle bundle = new Bundle();
                bundle.putSerializable("data", emit);
                buySubmitFragment.setArguments(bundle);

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
        String itemSelected = parent.getItemAtPosition(position).toString();
        Toast.makeText(getContext(), itemSelected, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}
