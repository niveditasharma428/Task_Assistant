package com.example.admin.task_assistant;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Signup extends AppCompatActivity  {
    EditText Name, Email , Password ;
    TextView Mobile;
    Button btnSignup;
    Button login_here;
    String mobile;
    RelativeLayout rootLayout;

    SharedPreferences.Editor editor;
    SharedPreferences.Editor editor5;
    SharedPreferences pref5;

    private static String USER_SIGNUP_URL = "https://orgone.solutions/task/signup.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("Options", MODE_PRIVATE);
        editor=pref.edit();
        pref5 = getApplication().getSharedPreferences("Options_user", MODE_PRIVATE);

        Name=(EditText)findViewById(R.id.editTextName);
        Email=(EditText)findViewById(R.id.editTextEmail);
        Mobile=(TextView) findViewById(R.id.editTextMobile);
        Password=(EditText)findViewById(R.id.editTextPassword);
        login_here=(Button) findViewById(R.id.textViewLogin);
        rootLayout= (RelativeLayout) findViewById(R.id.rootLayout);

        mobile=pref5.getString("umobile", "");
        Mobile.setText(mobile);

        Name.addTextChangedListener(new MyTextWatcher(Name));
        Email.addTextChangedListener(new MyTextWatcher(Email));
        Mobile.addTextChangedListener(new MyTextWatcher(Mobile));
        Password.addTextChangedListener(new MyTextWatcher(Password));

        login_here.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Signup.this,Signin.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);



            }
        });

        btnSignup=(Button)findViewById(R.id.buttonRegister);
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = Name.getText().toString();
                String userEmail = Email.getText().toString();
                String userMobile = Mobile.getText().toString();
                String userPassword= Password.getText().toString();

                Pattern p = Pattern.compile(Constants.regEx);
                Matcher m = p.matcher(userEmail);

                //		Pattern match for Mobile No
                Pattern mobi = Pattern.compile(Constants.mobregEx);
                Matcher mob = mobi.matcher(userMobile);

                if(userName.equalsIgnoreCase("")||userName.length() >=3 ||  userEmail.equalsIgnoreCase("")||userEmail.length()>=8||
                        userMobile.equalsIgnoreCase("")||userMobile.length()==10||userPassword.equalsIgnoreCase("")||userPassword.length()>=5)
                {

                    if(userName.equalsIgnoreCase("")){
                        Name.setError("Please Enter Full Name ");
                    }

                    else if(userEmail.equalsIgnoreCase("")||!m.find()){
                        Email.setError("Pleas Enter Valid Email ");
                    }

                    else if(userMobile.equalsIgnoreCase("")||!mob.find()){
                        Mobile.setError("Please Enter Valid Mobile Number ");
                    }

                    else if (userPassword.equalsIgnoreCase("")){
                        Password.setError("Please Enter Password ");
                    }


                    else {
                        if(Constants.isOnline(getApplicationContext())){
                            register(USER_SIGNUP_URL, userName, userEmail,userMobile, userPassword);
                           // Snackbar.make(rootLayout,"Register Success fully ",Snackbar.LENGTH_SHORT)
                                 //   .show();

                        } else {
                            Toast.makeText(getApplicationContext(), "No internet Connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(),"Entries are Wrong",Toast.LENGTH_LONG).show();
                }

                editor.putString("name", userName);
                editor.putString("email", userEmail);
                editor.putString("mobile", userMobile);
               // editor.putString("role", userRole);
                editor.commit();

            }



        });


    }

    private void register(String signupUrl, final String getuserName, final String getuserEmail, final String getuserMobile, final String getuserPassword) {


        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        StringRequest stringRequest  =new StringRequest(Request.Method.POST, signupUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response){
                try {
                    JSONObject jsonObject=new JSONObject(response);


                    if(jsonObject.getInt("success")==0)
                    {

                       // Snackbar.make(fragmentView, jsonObject.getString("message") + "", Snackbar.LENGTH_LONG)
                        //show();
                        Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();

                    }
                    else if(jsonObject.getInt("success")==1){
                         Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();


                        Intent intent=new Intent(Signup.this, Signin.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);

                    }

                    else if(jsonObject.getInt("success")==5){
                        Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();

                        Intent intent=new Intent(Signup.this, User_code.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);


                    }

                    else

                        Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                }
                catch (JSONException e) {
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
            protected Map<String, String> getParams() {

                Map<String, String> param = new HashMap<String, String>();

                param.put("name",getuserName);
                param.put("email", getuserEmail);
                param.put("mobile", getuserMobile);
               // param.put("role", getuserRole);
                param.put("password", getuserPassword);
                return param;
            }
        };

        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);

    }


    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.editTextName:
                    break;
                case R.id.editTextEmail:
                    break;
                case R.id.editTextMobile:
                    break;
                case R.id.editTextPassword:
                    break;

            }
        }
    }


}



