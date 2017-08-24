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

import helpers.ExchangeRestOperation;
import helpers.FormatDataAsJson;
import helpers.PairCls;


/**
 * A simple {@link Fragment} subclass.
 */
public class ExchangeFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    String apiURL;
    String apiKey;

    public String[] ingredients = new String[3];

    HashMap<String, String> emit = new HashMap<String, String>();

    EditText sendAmountField;
    EditText receiveAmountField;

    Button showButton;
    Button nextBtn;

    public ExchangeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_exchange, container, false);

        // change action bar text
        getActivity().setTitle("Exchange Currency");

        // set api meta data
        apiURL = getString(R.string.apiURL);
        apiKey = getString(R.string.apiKey);

        // target the spinner elements
        Spinner sendMethodSpinner = (Spinner) view.findViewById(R.id.sendMethodSpinner);
        Spinner receiveMethodSpinner = (Spinner) view.findViewById(R.id.receiveMethodSpinner);

        // rest api class
        ExchangeRestOperation exchangeRestOperation = new ExchangeRestOperation(apiURL, apiKey);

        // send methods spinner
        try {
            String response = exchangeRestOperation.getSendMethods();
            JSONObject resultObject = new JSONObject(response);

            if(resultObject.length() > 0) {
                Iterator<?> keys = resultObject.keys();
                ArrayList<PairCls> sendPairList = new ArrayList<>();

                while (keys.hasNext()) {
                    String key = (String) keys.next();
                    sendPairList.add(new PairCls(key, resultObject.getString(key)));
                }

                // Setup Spinner configuration
                ArrayAdapter<PairCls> adapter = new ArrayAdapter<PairCls>(getContext(), android.R.layout.simple_spinner_dropdown_item, sendPairList);
                sendMethodSpinner.setAdapter(adapter);
                // sendMethodSpinner.setSelection(adapter.getPosition(myItem)); // Optional

                // sendMethodSpinner.setOnItemSelectedListener(this);
                sendMethodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                        PairCls pairCls = (PairCls) parent.getSelectedItem();

                        String key = pairCls.getId();
                        String value = pairCls.getName();

                        // Toast.makeText(getContext(), "Key: " + key + ",  Value : " + value, Toast.LENGTH_SHORT).show();

                        ingredients[0] = pairCls.getId();
                        emit.put("send_method", pairCls.getId());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        PairCls pairCls = (PairCls) parent.getSelectedItem();

                        String key = pairCls.getId();
                        String value = pairCls.getName();

                        // Toast.makeText(getContext(), "Key: " + key + ",  Value : " + value, Toast.LENGTH_SHORT).show();

                        ingredients[0] = pairCls.getId();
                        emit.put("send_method", pairCls.getId());
                    }
                });

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // receive methods spinner
        try {
            final String response = exchangeRestOperation.getReceiveMethods();
            JSONObject resultObject = new JSONObject(response);

            if(resultObject.length() > 0) {
                Iterator<?> keys = resultObject.keys();
                ArrayList<PairCls> receivePairList = new ArrayList<>();

                while (keys.hasNext()) {
                    String key = (String) keys.next();
                    receivePairList.add(new PairCls(key, resultObject.getString(key)));
                }

                // Setup Spinner configuration
                ArrayAdapter<PairCls> adapter = new ArrayAdapter<PairCls>(getContext(), android.R.layout.simple_spinner_dropdown_item, receivePairList);
                receiveMethodSpinner.setAdapter(adapter);
                // receiveMethodSpinner.setSelection(adapter.getPosition(myItem)); // Optional

                // receiveMethodSpinner.setOnItemSelectedListener(this);
                receiveMethodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                        PairCls pairCls = (PairCls) parent.getSelectedItem();

                        String key = pairCls.getId();
                        String value = pairCls.getName();

                        // Toast.makeText(getContext(), "Key: " + key + ",  Value : " + value, Toast.LENGTH_SHORT).show();

                        ingredients[1] = pairCls.getId();
                        emit.put("receive_method", pairCls.getId());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        PairCls pairCls = (PairCls) parent.getSelectedItem();

                        String key = pairCls.getId();
                        String value = pairCls.getName();

                        // Toast.makeText(getContext(), "Key: " + key + ",  Value : " + value, Toast.LENGTH_SHORT).show();

                        ingredients[1] = pairCls.getId();
                        emit.put("receive_method", pairCls.getId());
                    }
                });

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // set default data into fields
        sendAmountField = (EditText) view.findViewById(R.id.sendAmountId);
        sendAmountField.setText("0.00");

        receiveAmountField = (EditText) view.findViewById(R.id.receiveAmountId);
        receiveAmountField.setText("0.00");
        receiveAmountField.setEnabled(false);
        receiveAmountField.setFocusable(false);

        showButton = (Button) view.findViewById(R.id.showButtonId);
        nextBtn = (Button) view.findViewById(R.id.nextButtonId);

        // show button
        showButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // check the send amount
                double sendAmount = Double.parseDouble(String.valueOf(sendAmountField.getText()));
                int retval = Double.compare(sendAmount, 0.00);

                if (retval > 0) {
                    ingredients[2] = String.valueOf(sendAmountField.getText());
                    emit.put("send_amount", String.valueOf(sendAmountField.getText()));

                    // get data from
                    FormatDataAsJson formatDataAsJson = new FormatDataAsJson(new String[]{"send", "receive", "amount"}, ingredients);

                    // rest api class
                    ExchangeRestOperation exchangeRestOperation = new ExchangeRestOperation(apiURL, apiKey);
                    final String response = exchangeRestOperation.getAmount(formatDataAsJson.format());

                    Log.v("Response", response);

                    try {
                        JSONObject resultObject = new JSONObject(response);

                        receiveAmountField.setText(resultObject.getString("receive_amount"));

                        emit.put("receive_amount", resultObject.getString("receive_amount"));
                        emit.put("send_methodInfo", resultObject.getString("send_methodInfo"));
                        emit.put("send_level", resultObject.getString("send_level"));
                        emit.put("receive_level", resultObject.getString("receive_level"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    nextBtn.setEnabled(true);
                } else {
                    Toast.makeText(getContext(), "Send amount at least 1.00", Toast.LENGTH_LONG).show();
                    nextBtn.setEnabled(false);
                }
            }
        });

        // next page
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // collect data
                emit.put("send_currency", "USD");
                emit.put("receive_currency", "USD");

                ExchangeSubmitFragment exchangeSubmitFragment = new ExchangeSubmitFragment();

                Bundle bundle = new Bundle();
                bundle.putSerializable("data", emit);
                exchangeSubmitFragment.setArguments(bundle);

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.dashboard_container, exchangeSubmitFragment);
                ft.addToBackStack(null);
                ft.commit();

                // Toast.makeText(getContext(), "Hello World!", Toast.LENGTH_LONG).show();
                Log.v("DATA", emit.toString());
            }
        });

        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
