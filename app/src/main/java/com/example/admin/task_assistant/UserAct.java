package com.example.admin.task_assistant;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class UserAct extends AppCompatActivity {

    Button button;
    EditText editText;
    SharedPreferences pref;
    String mobile;

    private static String VERIFY_URL = "https://orgone.solutions/task/key_master.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user2);

        pref = getApplication().getSharedPreferences("Options", MODE_PRIVATE);

        button= (Button) findViewById(R.id.verify);
        editText= (EditText) findViewById(R.id.key);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mobile = pref.getString("mobile", "");
                String userKey = editText.getText().toString();

                verify(VERIFY_URL, mobile, userKey);

            }
        });
    }

    private void verify(String verifyUrl, final String getuserMobile, final String getuserKey) {

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        StringRequest stringRequest  =new StringRequest(Request.Method.POST, verifyUrl, new Response.Listener<String>() {
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

                        Intent intent=new Intent(UserAct.this, Dashboard.class);
                        startActivity(intent);

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

                param.put("mobile",getuserMobile);
                param.put("userkey", getuserKey);

                return param;
            }
        };

        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);

    }
}
