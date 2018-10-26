
package com.example.admin.task_assistant;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Discription extends AppCompatActivity {

    SharedPreferences pref;

    public void attachBaseContext(Context newBase){
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("font/Montserrat-Light.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_discription);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Thread thread = new Thread() {

            @Override
            public void run() {
                try {
                    sleep(4000);
                    pref = getApplication().getSharedPreferences("Options", Context.MODE_PRIVATE);

                    String val = pref.getString("mobile", "");
                     String val1 = pref.getString("adminkeygen", "");


                    if (val.length() == 0) {

                        Intent intent = new Intent(Discription.this, Admin_User.class);
                        startActivity(intent);
                        finish();


                    }
                    else if(val1.length() == 0){
                         Intent intent = new Intent(Discription.this, AdminLogin.class);
                        startActivity(intent);
                        finish();

                    }

                    else {

                        Intent intent = new Intent(Discription.this, Dashboard.class);
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
}

