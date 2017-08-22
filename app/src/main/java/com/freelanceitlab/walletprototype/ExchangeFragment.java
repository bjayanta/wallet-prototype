package com.freelanceitlab.walletprototype;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 */
public class ExchangeFragment extends Fragment {

    public ExchangeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_exchange, container, false);

        EditText receiveAmountField = (EditText) view.findViewById(R.id.receiveAmountId);
        receiveAmountField.setText("0.00");
        receiveAmountField.setEnabled(false);
        receiveAmountField.setFocusable(false);

        return view;
    }

}
