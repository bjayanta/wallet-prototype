package com.freelanceitlab.walletprototype;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


public class HomeFragment extends Fragment {
    Button signup_btn;
    Button signin_btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // sign up button
        signup_btn = (Button) view.findViewById(R.id.signup);
        // get button text
        // final String signup_txt = signup_btn.getText().toString();
        // execute the click event
        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                SignupFragment signupFragment = new SignupFragment();
                ft.replace(R.id.fragment_container, signupFragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        // sign in button
        signin_btn = (Button) view.findViewById(R.id.signin);
        // get button text
        final String signin_txt = signin_btn.getText().toString();
        // execute the click event
        signin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                SigninFragment signinFragment = new SigninFragment();
                ft.replace(R.id.fragment_container, signinFragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        return view;
    }

}
