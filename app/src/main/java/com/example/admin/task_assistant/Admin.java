package com.example.admin.task_assistant;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Admin extends AppCompatActivity implements View.OnClickListener{

    private Toolbar toolbar;
    Button prev,reg,login;
    EditText Name, Email, Mobile ,Password,Organisation;
    String mobile,name,email;

    SharedPreferences.Editor editor;
    SharedPreferences pref;

    private RequestQueue requestQueue;
    private static String ADMIN_REG_URL = "https://orgone.solutions/task/register_data.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        login = (Button) findViewById(R.id.Login);
        reg= (Button) findViewById(R.id.next);
        Name=(EditText)findViewById(R.id.etName);
        Email=(EditText)findViewById(R.id.etEmail);
        Mobile=(EditText)findViewById(R.id.etMobile);
        Password=(EditText)findViewById(R.id.etPassword);
        Organisation=(EditText)findViewById(R.id.etOrganisation);

        requestQueue = Volley.newRequestQueue(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Admin.this, AdminLogin.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);

            }
        });

        reg.setOnClickListener(this);

    }

    private void admin_reg(String regUrl, final String getuserName, final String getuserEmail, final String getuserMobile, final String getuserPassword,final String getuserOccupation) {

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest  =new StringRequest(Request.Method.POST, ADMIN_REG_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response){
                try {
                    JSONObject jsonObject=new JSONObject(response);

                    if(jsonObject.getInt("success")==0)

                    {
                        Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                    }
                    else if(jsonObject.getInt("success")==1){

                        Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(Admin.this, Verify_Mobile.class);
                        startActivity(intent);
                        overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);

                        pref = getApplicationContext().getSharedPreferences("Options", MODE_PRIVATE);
                        editor=pref.edit();

                        editor.putString("name", getuserName);
                        editor.putString("email", getuserEmail);
                        editor.putString("mobile", getuserMobile);

                        editor.commit();
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

                param.put(Config.KEY_NAME,getuserName);
                param.put(Config.KEY_EMAIL, getuserEmail);
                param.put(Config.KEY_MOBILE, getuserMobile);
                param.put(Config.KEY_PASSWORD, getuserPassword);
                param.put(Config.KEY_OCCUPATION, getuserOccupation);

                return param;
            }
        };

        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);

    }
    @Override
    public void onClick(View view) {

        String userName = Name.getText().toString();
        String userEmail = Email.getText().toString();
        String userMobile = Mobile.getText().toString();
        String userPassword= Password.getText().toString();
        String occupation = Organisation.getText().toString();

        // Pattern match for email id
        Pattern p = Pattern.compile(Constants.regEx);
        Matcher m = p.matcher(userEmail);

        //		Pattern match for Mobile No
        Pattern mobi = Pattern.compile(Constants.mobregEx);
        Matcher mob = mobi.matcher(userMobile);

        if(userName.equalsIgnoreCase("")||userName.length() >=3 ||  userEmail.equalsIgnoreCase("")||userEmail.length()>=8||
                userMobile.equalsIgnoreCase("")||userMobile.length()==10||userPassword.equalsIgnoreCase("")||userPassword.length()>=5||occupation.equalsIgnoreCase(""))
        {

            if(userName.equalsIgnoreCase("")){
                Name.setError("Please Enter Name ");
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
            else if (userPassword.length()<=5){
                Password.setError("Password must contain atleast 5 Character ");
            }
            else if (occupation.equalsIgnoreCase("")){
                Organisation.setError("Please Enter Organisation ");
            }


            else {
                if(Constants.isOnline(getApplicationContext())){
                    admin_reg(ADMIN_REG_URL, userName, userEmail,userMobile, userPassword,occupation);


                } else {
                    Toast.makeText(getApplicationContext(), "No internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        }
        else{
            Toast.makeText(getApplicationContext(),"Entries are Wrong",Toast.LENGTH_LONG).show();
        }


    }

    public void onBackPressed() {
        Intent back = new Intent(getApplicationContext(), Admin_User.class);
        back.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        back.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(back);
        overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);
    }


}
