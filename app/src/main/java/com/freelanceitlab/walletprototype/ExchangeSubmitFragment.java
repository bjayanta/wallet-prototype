package com.freelanceitlab.walletprototype;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class ExchangeSubmitFragment extends Fragment {

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


        return view;
    }

}
