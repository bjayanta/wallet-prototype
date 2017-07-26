package com.freelanceitlab.walletprototype;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class BuyFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private String[] sendGatewayItems;
    private String[] receiveGatewayItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_buy, container, false);

        // change action bar text
        getActivity().setTitle("Buy Currency");

        // target the send spinner
        Spinner sendGatewayId = (Spinner) view.findViewById(R.id.sendGatewayId);
        Spinner receiveGatewayId = (Spinner) view.findViewById(R.id.receiveGatewayId);

        // get all the list array
        sendGatewayItems = getResources().getStringArray(R.array.bdGateway);
        receiveGatewayItems = getResources().getStringArray(R.array.foreignGateway);

        // initialize the array-adapter
        ArrayAdapter<String> bdGatewayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, sendGatewayItems);
        // set the array-adapter
        bdGatewayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sendGatewayId.setAdapter(bdGatewayAdapter);

        // initialize the array-adapter
        ArrayAdapter<String> foreignGatewayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, receiveGatewayItems);
        // set the array-adapter
        foreignGatewayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        receiveGatewayId.setAdapter(foreignGatewayAdapter);

        // active readonly mode the BDT amount
        EditText sendAmountId = (EditText) view.findViewById(R.id.sendAmountId);
        sendAmountId.setEnabled(false);
        sendAmountId.setText("500.00");

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

        sendGatewayId.setOnItemSelectedListener(this);
        receiveGatewayId.setOnItemSelectedListener(this);

        // log the userdata
        Log.v("USERDATA", setUserdata());

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

    private String setUserdata() {
        SharedPreferences appdata = getActivity().getSharedPreferences("Appdata", Context.MODE_PRIVATE);
        String data = appdata.getString("userdata", "");

        return data;
    }
}
