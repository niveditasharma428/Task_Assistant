package com.example.admin.task_assistant;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        Thread thread = new Thread() {

            @Override
            public void run() {
                try {
                    sleep(3000);
                    SharedPreferences shared = getSharedPreferences("Options", Context.MODE_PRIVATE);
                    String val = shared.getString("mobile", "");
                    if (val.length() == 0) {

                        Intent intent = new Intent(Splash.this, Signin.class);
                        startActivity(intent);
                        finish();


                    } else {

                        Intent intent = new Intent(Splash.this, Dashboard.class);
                        startActivity(intent);
                        finish();
                    }


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        };

        thread.start();
    }
    protected void onPause() {
        super.onPause();
        finish();
    }
}
