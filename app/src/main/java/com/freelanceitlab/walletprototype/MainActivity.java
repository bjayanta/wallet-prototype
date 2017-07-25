package com.freelanceitlab.walletprototype;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Thread splashscreen = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(3000);

                    // start home screen activity
                    Intent intent = new Intent(getApplicationContext(), Homescreen.class);
                    startActivity(intent);

                    // kill the current activity
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        splashscreen.start();
    }
}
