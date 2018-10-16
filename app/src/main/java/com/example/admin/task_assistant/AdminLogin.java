package com.example.admin.task_assistant;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;

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

public class AdminLogin extends AppCompatActivity implements View.OnClickListener {


    EditText Login_Mob, Login_Pwd;
    Button Login_signup_here, Login_Forget_Pwd;
    Button Login_Button;
    CheckBox show_pass;
    Context context;
    RelativeLayout rootLayout, rLayout;
    private static Animation shakeAnimation;

    SharedPreferences.Editor editor;
    SharedPreferences pref;
    String admin;

    private static String ADMIN_LOGIN_URL = "https://orgone.solutions/task/admin_login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        Login_Mob = (EditText) findViewById(R.id.etMobile);
        Login_Pwd = (EditText) findViewById(R.id.etPassword);
        Login_signup_here = (Button) findViewById(R.id.textViewRegister);
        Login_Forget_Pwd = (Button) findViewById(R.id.admin_Forgot);
        Login_Button = (Button) findViewById(R.id.buttonLogin);
        show_pass = (CheckBox) findViewById(R.id.show_pass);
        rootLayout = (RelativeLayout) findViewById(R.id.rootLayout);
        rLayout = (RelativeLayout) findViewById(R.id.rLayout);


        Login_signup_here.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminLogin.this, Admin.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        Login_Forget_Pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminLogin.this, Forgot_Password.class);
                intent.putExtra("usertyp", "admin");
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        show_pass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if (!isChecked) {
                    Login_Pwd.setInputType(129);
                    Login_Pwd.setSelection(Login_Pwd.length());
                } else {
                    Login_Pwd.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    Login_Pwd.setSelection(Login_Pwd.length());
                }


            }
        });

        Login_Button.setOnClickListener(this);

        shakeAnimation = AnimationUtils.loadAnimation(this,
                R.anim.shake);

    }

    private void login(String loginUrl, final String login_userMob, final String login_userPass) {
        RequestQueue requestQueue = new Volley().newRequestQueue(getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.POST, loginUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.getInt("success") == 1) {
                        System.out.println("adminlogin1");
                        Snackbar.make(rootLayout, jsonObject.getString("message") + "", Snackbar.LENGTH_LONG)
                                .show();
                        // Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();

                        JSONObject jsonObjectInfo = jsonObject.getJSONObject("User");
                        String mobile = jsonObjectInfo.getString("mobile");
                        String name = jsonObjectInfo.getString("name");
                        String email = jsonObjectInfo.getString("email");
                        String usertyp = jsonObjectInfo.getString("usertyp");
                        String count = jsonObjectInfo.getString("grp_count");
                     //   String admin_name = jsonObject.getString("admin_name");

                        pref = getApplicationContext().getSharedPreferences("Options", MODE_PRIVATE);
                        editor = pref.edit();

                        editor.putString("name", name);
                        editor.putString("email", email);
                        editor.putString("mobile", mobile);
                        editor.putString("usertyp", usertyp);
                        editor.putString("count", count);
                       // editor.putString("admin_name", admin_name);
                        editor.commit();

                        Intent intent = new Intent(AdminLogin.this, Verify_Mobile.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);


                    } else if (jsonObject.getInt("success") == 2) {

                        System.out.println("adminlogin2");
                        Snackbar.make(rootLayout, jsonObject.getString("message") + "", Snackbar.LENGTH_LONG)
                                .show();

                        JSONObject jsonObjectInfo = jsonObject.getJSONObject("User");
                        String mobile = jsonObjectInfo.getString("mobile");
                        String name = jsonObjectInfo.getString("name");
                        String email = jsonObjectInfo.getString("email");
                        String usertyp = jsonObjectInfo.getString("usertyp");
                        String count = jsonObjectInfo.getString("grp_count");
                     //   String admin_name = jsonObject.getString("admin_name");


                        pref = getApplicationContext().getSharedPreferences("Options", MODE_PRIVATE);
                        editor = pref.edit();

                        editor.putString("name", name);
                        editor.putString("email", email);
                        editor.putString("mobile", mobile);
                        editor.putString("usertyp", usertyp);
                        editor.putString("count", count);
                     //  editor.putString("admin_name", admin_name);
                        editor.commit();

                        Intent intent = new Intent(AdminLogin.this, GetStart.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    } else if (jsonObject.getInt("success") == 3) {

                        System.out.println("adminlogin3");
                        Snackbar.make(rootLayout, jsonObject.getString("message") + "", Snackbar.LENGTH_LONG)
                                .show();

                        JSONObject jsonObjectInfo = jsonObject.getJSONObject("User");

                    //    String admin_name = jsonObject.getString("admin_name");
                     //   System.out.println("DivyaAdmin:-" + admin_name);
                        String adminkeygen = jsonObjectInfo.getString("adminkeygen");
                        String mobile = jsonObjectInfo.getString("mobile");
                        String name = jsonObjectInfo.getString("name");
                        String email = jsonObjectInfo.getString("email");
                        String usertyp = jsonObjectInfo.getString("usertyp");
                        String count = jsonObjectInfo.getString("grp_count");


                        pref = getApplicationContext().getSharedPreferences("Options", MODE_PRIVATE);
                        editor = pref.edit();

                        editor.putString("adminkeygen", adminkeygen);
                        editor.putString("name", name);
                        editor.putString("email", email);
                        editor.putString("mobile", mobile);
                        editor.putString("usertyp", usertyp);
                        editor.putString("count", count);
                       // editor.putString("admin_name", admin_name);
                        editor.commit();

                        Intent intent = new Intent(AdminLogin.this, Dashboard.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    } else {

                        System.out.println("adminlogin1");
                        Snackbar.make(rootLayout, jsonObject.getString("message") + "", Snackbar.LENGTH_LONG)
                                .show();

                    }

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
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("mobile", login_userMob);
                param.put("password", login_userPass);

                return param;
            }

        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        requestQueue.add(postRequest);
    }

    public void onClick(View v) {
        final String Login_UserMob = Login_Mob.getText().toString();
        final String Login_UserPass = Login_Pwd.getText().toString();


        if (Login_UserMob.equalsIgnoreCase("") ||
                Login_UserPass.equalsIgnoreCase("") || Login_UserPass.length() >= 5) {
            if (Login_UserMob.equalsIgnoreCase("")) {

                rLayout.startAnimation(shakeAnimation);
                new CustomToast().Show_Toast(this, rootLayout,
                        "Enter your mobile No.");
            } else if (Login_UserPass.equalsIgnoreCase("")) {
                rLayout.startAnimation(shakeAnimation);
                new CustomToast().Show_Toast(this, rootLayout,
                        "Enter your password.");
            } else {
                if (Constants.isOnline(getApplicationContext())) {
                    login(ADMIN_LOGIN_URL, Login_UserMob, Login_UserPass);

                } else {
                    new CustomToast().Show_Toast(this, rootLayout,
                            "No internet Connection.");

                }
            }
        } else {

            rLayout.startAnimation(shakeAnimation);
            new CustomToast().Show_Toast(this, rootLayout,
                    "Entries are Wrong.");

        }
    }


}


