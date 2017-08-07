package com.freelanceitlab.walletprototype;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class BuySubmitFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_buy_submit, container, false);

        // change action bar text
        getActivity().setTitle("Buy Currency");

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
