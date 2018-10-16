package com.example.admin.task_assistant;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class New_Password extends AppCompatActivity implements View.OnClickListener {

    EditText Pass, Cpass;
    Button save;
    String usertyp;
    String password, cpassword,mobile;
    private RequestQueue requestQueue;

    SharedPreferences.Editor editor;
    SharedPreferences pref;

    private static String NEW_PASS_URL = "https://orgone.solutions/task/resetpass.php";

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

        setContentView(R.layout.activity_new__password);


        Pass = (EditText) findViewById(R.id.etNew);
        Cpass = (EditText) findViewById(R.id.etConfirm);
        save = (Button) findViewById(R.id.buttonSave);

        requestQueue = Volley.newRequestQueue(this);

        pref=getApplication().getSharedPreferences("Options",MODE_PRIVATE);

        save.setOnClickListener(this);
    }

    private void newPass() {
        password = Pass.getText().toString();
        cpassword = Cpass.getText().toString();
        mobile = pref.getString("mobile", "");
        usertyp = pref.getString("usertyp", "");

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.POST,NEW_PASS_URL,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                     if (jsonObject.getInt("success") == 1) {

                          Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                          Intent intent = new Intent(New_Password.this, AdminLogin.class);
                         intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                         intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                         startActivity(intent);
                         overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);

                    }
                   else if (jsonObject.getInt("success") == 2) {

                          Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                          Intent intent = new Intent(New_Password.this,Signin.class);
                         intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                         intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                         startActivity(intent);
                         overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);

                    }
                    else if (jsonObject.getInt("success") == 0)

                    {
                        Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();

                    }  else

                        Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
            Map<String,String> params = new HashMap<String, String>();

            params.put(Config.KEY_MOBILE, mobile);
            params.put(Config.KEY_NEW_PASSWORD, password);
            params.put(Config.KEY_CPASSWORD, cpassword);
            params.put(Config.KEY_USERTYPE, usertyp);
            return params;
        }
        };

        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        requestQueue.add(postRequest);

    }

    @Override
    public void onClick(View view) {
        newPass();
    }

    public void onBackPressed() {
        Intent back = new Intent(getApplicationContext(), Forgot_Password.class);
        back.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        back.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(back);
        overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);
    }

}
