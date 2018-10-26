package com.example.admin.task_assistant;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
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

public class User_code extends AppCompatActivity implements View.OnClickListener {

    private Button verify;
    private EditText mob,code;
    String ucode,umobile;

    SharedPreferences.Editor editor5;
    SharedPreferences pref5;
    Button login_here,register_here;

    private static String CODE_URL = "https://orgone.solutions/task/ucodeverif.php";

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
        setContentView(R.layout.activity_user_code);

        verify = (Button) findViewById(R.id.verify);
        mob = (EditText) findViewById(R.id.editTextMobile);
        code = (EditText) findViewById(R.id.code);
        login_here=(Button) findViewById(R.id.Login);
        register_here=(Button) findViewById(R.id.textViewRegister);
        verify.setOnClickListener(this);

        login_here.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(User_code.this,Signin.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

    }

    private void verifyMob() throws JSONException {


        ucode = code.getText().toString().trim();
        umobile = mob.getText().toString().trim();

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.POST,CODE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject jsonObject= null;
                        try {
                            jsonObject = new JSONObject(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        try {
                            if(jsonObject.getInt("success")==1){


                                Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                                JSONObject jsonObjectInfo=jsonObject.getJSONObject("User");
                                String umobile = jsonObjectInfo.getString("umobile");

                                pref5 = getApplicationContext().getSharedPreferences("Options_user", MODE_PRIVATE);
                                editor5=pref5.edit();
                                editor5.putString("umobile", umobile);
                                editor5.commit();
                                Intent intent=new Intent(User_code.this,Signup.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);


                            }else if(jsonObject.getInt("success")==2){

                                Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                                pref5 = getApplicationContext().getSharedPreferences("Options_user", MODE_PRIVATE);
                                editor5=pref5.edit();
                                editor5.putString("umobile", umobile);
                                editor5.commit();

                                Intent intent=new Intent(User_code.this,Signin.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);

                            }
                            else if(jsonObject.getInt("success")==3){

                                Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                                pref5 = getApplicationContext().getSharedPreferences("Options_user", MODE_PRIVATE);
                                editor5=pref5.edit();
                                editor5.putString("umobile", umobile);
                                editor5.commit();

                                Intent intent=new Intent(User_code.this,Signup.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);

                            }
                            else {

                                Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();

                                Intent intent=new Intent(User_code.this,User_code.class);
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

                        Toast.makeText(User_code.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();

                params.put(Config.KEY_UMOBILE, umobile);
                params.put(Config.KEY_UCODE, ucode);
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
            code.onEditorAction(EditorInfo.IME_ACTION_DONE);
            verifyMob();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onBackPressed() {
        Intent back = new Intent(getApplicationContext(),Admin_User.class);
        back.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        back.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(back);
        overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
