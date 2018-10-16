package com.example.admin.task_assistant;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
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

public class Verify_Mobile extends AppCompatActivity implements View.OnClickListener{

    private AppCompatButton buttonConfirm;
    private EditText editTextConfirmOtp;
    TextView mob,resend;
    String mobile;

    private static String CONFIRM_URL = "https://orgone.solutions/task/checkotp.php";
     private static String RESEND_URL = "https://orgone.solutions/task/resendotp.php";

    SharedPreferences pref;
    SharedPreferences.Editor editor;

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

        setContentView(R.layout.activity_verify__mobile);
        buttonConfirm = (AppCompatButton) findViewById(R.id.buttonConfirm);
        editTextConfirmOtp = (EditText) findViewById(R.id.editTextOtp);
        mob=(TextView) findViewById(R.id.verify_mob);
        resend=(TextView) findViewById(R.id.Resend_otp);

        pref=getApplication().getSharedPreferences("Options",MODE_PRIVATE);
        mobile = pref.getString("mobile", "");
        mob.setText(mobile);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("Options", MODE_PRIVATE);
        editor=pref.edit();

        buttonConfirm.setOnClickListener(this);

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    try {
            resendOtp();
        } catch (JSONException e) {
            e.printStackTrace();
        }
                }
        });
    }
    private void confirmOtp() throws JSONException {

        final ProgressDialog loading = ProgressDialog.show(Verify_Mobile.this, "Authenticating", "Please wait while we check the entered code", false,false);
        final String otp = editTextConfirmOtp.getText().toString().trim();

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.POST,CONFIRM_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //if the server response is success
                        JSONObject jsonObject= null;
                        try {
                            jsonObject = new JSONObject(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        try {
                            if(jsonObject.getInt("success")==1){

                                loading.dismiss();

                                JSONObject jsonObjectInfo=jsonObject.getJSONObject("User");
                                String usertyp = jsonObjectInfo.getString("usertyp");

                                Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();


                                Intent intent=new Intent(Verify_Mobile.this,GetStart.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);

                                pref = getApplicationContext().getSharedPreferences("Options", MODE_PRIVATE);
                                editor=pref.edit();
                                editor.putString("usertyp", usertyp);
                                editor.commit();
                            }else{

                                Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();

                                Intent intent=new Intent(Verify_Mobile.this,Verify_Mobile.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(Verify_Mobile.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                //Adding the parameters otp and username
                params.put(Config.KEY_OTP, otp);
                params.put(Config.KEY_MOBILE, mobile);
                return params;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        requestQueue.add(postRequest);

    }

        private void resendOtp() throws JSONException {

        final ProgressDialog loading = ProgressDialog.show(Verify_Mobile.this, "Authenticating", "Please wait while we check the entered code", false,false);
        final String otp = editTextConfirmOtp.getText().toString().trim();

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.POST,RESEND_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //if the server response is success
                        JSONObject jsonObject= null;
                        try {
                            jsonObject = new JSONObject(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        try {
                            if(jsonObject.getInt("success")==1){

                                loading.dismiss();

                                Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();


                                Intent intent=new Intent(Verify_Mobile.this,Verify_Mobile.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);
                            }else{

                                Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                                Intent intent=new Intent(Verify_Mobile.this,Verify_Mobile.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(Verify_Mobile.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                //Adding the parameters otp and username

                params.put(Config.KEY_MOBILE, mobile);
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
        try {
            confirmOtp();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
