package com.example.admin.task_assistant;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

public class GetStart extends AppCompatActivity implements View.OnClickListener{

    TextView start;
    TextView n;
    String name,mobile;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    private static String GENERATE_KEY_URL = "https://orgone.solutions/task/keygen.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_start);

        start= (TextView) findViewById(R.id.get_started);
        n= (TextView) findViewById(R.id.admin_name);

        pref=getApplication().getSharedPreferences("Options",MODE_PRIVATE);
        name = pref.getString("name", "");
        n.setText(name);

        pref=getApplication().getSharedPreferences("Options",MODE_PRIVATE);
        mobile = pref.getString("mobile", "");

        start.setOnClickListener(this);
    }

    private void key() {

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.POST,GENERATE_KEY_URL,
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
                                //dismissing the progressbar
                                Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();

                                 JSONObject jsonObjectInfo=jsonObject.getJSONObject("User");
                                 String adminkeygen = jsonObjectInfo.getString("adminkeygen");

                                  pref = getApplicationContext().getSharedPreferences("Options", MODE_PRIVATE);
                                  editor=pref.edit();

                                  editor.putString("adminkeygen", adminkeygen);
                                  editor.commit();
                                //Starting a new activity
                                Intent intent=new Intent(GetStart.this,Dashboard.class);
                                startActivity(intent);
                                overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);
                            }else{
                                //Displaying a toast if the otp entered is wrong
                                Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                                Intent intent=new Intent(GetStart.this,GetStart.class);
                                startActivity(intent);
                                overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);
                               /* try {
                                    //Asking user to enter otp again
                                    confirmOtp();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }*/
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(GetStart.this, error.getMessage(), Toast.LENGTH_LONG).show();
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

        key();
        start.setEnabled(false);
    }
}
