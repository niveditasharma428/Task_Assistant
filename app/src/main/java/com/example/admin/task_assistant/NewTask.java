package com.example.admin.task_assistant;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class NewTask extends AppCompatActivity {

    SharedPreferences pref;
    SharedPreferences pref1;

    private Toolbar toolbar;
    String id,mobile,name,email,tag;

    Spinner spinnerDropDown,spinnerDropDown2;
    String[] priority = {
            "High",
            "Low",

    };

    EditText t1,t2;
    TextView t3;
    Button assign,cancel;
    Context context;

    ArrayList<String> ContactName;
    ArrayList<String> ContactPhone;

    private static String ADD_TASK_URL = "https://orgone.solutions/task/task.php";
    private static String CONTACT_URL = "https://orgone.solutions/task/userdata.php";

    public void attachBaseContext(Context newBase){
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("font/Arkhip_font.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        setContentView(R.layout.activity_new_task);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_white_24dp);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(NewTask.this, Dashboard.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);

            }
        });

        pref=getApplication().getSharedPreferences("Options",MODE_PRIVATE);
        name=pref.getString("name", "");
        email = pref.getString("email", "");
        mobile = pref.getString("mobile", "");

        spinnerDropDown =(Spinner)findViewById(R.id.spinner1);

        ContactName=new ArrayList<String>();
        ContactPhone=new ArrayList<String>();

        spinnerDropDown2=(Spinner)findViewById(R.id.spinner3);
        loadSpinnerData();

        t2= (EditText)findViewById(R.id.etDes);
        assign=(Button)findViewById(R.id.buttonAssign);
        cancel=(Button)findViewById(R.id.cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(NewTask.this,Dashboard.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this,android.
                R.layout.simple_spinner_dropdown_item ,priority);

        spinnerDropDown.setAdapter(adapter);

        spinnerDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                ((TextView) parent.getChildAt(0)).setTextSize(18);
                // Get select item
                int sid=spinnerDropDown.getSelectedItemPosition();
               // Toast.makeText(getBaseContext(), "You have selected City : " + priority[sid],
                 //       Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        spinnerDropDown2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                ((TextView) parent.getChildAt(0)).setTextSize(20);
                // Get select item
                int s1id=spinnerDropDown2.getSelectedItemPosition();


            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        assign.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent=new Intent(NewTask.this,TaskAssign.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);

                String 	TASK_DES = t2.getText().toString();
                String  CREATED_BY=pref.getString("name", "");
                String 	TASK_PRIORITY = spinnerDropDown.getSelectedItem().toString();
                String 	TASK_ASSIGN = spinnerDropDown2.getSelectedItem().toString();

                if(	TASK_DES.equalsIgnoreCase("")){
                    t1.setError("Please Enter  ");
                }

                else {
                    if(Constants.isOnline(getApplicationContext())){
                        task(ADD_TASK_URL,TASK_DES,TASK_PRIORITY,CREATED_BY,TASK_ASSIGN);
                    } else {
                        Toast.makeText(getApplicationContext(), "No internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    private void loadSpinnerData() {

        int i = 0;

        StringRequest stringRequest=new StringRequest(Request.Method.POST, CONTACT_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONArray jsonObj = new JSONArray(response);
                    final int numberOfItemsInResp = jsonObj.length();
                    for (int i = 0; i < numberOfItemsInResp; i++) {
                        JSONObject jsonObject = (JSONObject) jsonObj.get(i);

                        String name = jsonObject.getString("name");

                        ContactName.add( name);
                    }
                    spinnerDropDown2.setAdapter(new ArrayAdapter<String>(NewTask.this, android.R.layout.simple_spinner_dropdown_item, ContactName));

                }catch (JSONException e){e.printStackTrace();}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mobile",mobile);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(NewTask.this);
        requestQueue.add(stringRequest);
    }

    private void task(String signupUrl, final String getTASK_DES,final String getTASK_PRIORITY,final String getCREATED_BY,final String getTASK_ASSIGN){

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        StringRequest stringRequest  =new StringRequest(Request.Method.POST, signupUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);


                    if(jsonObject.getInt("success")==0)
                    {
                        Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();

                    }
                    else if(jsonObject.getInt("success")==1){
                        Toast.makeText(getApplicationContext(), "Assign task Successfully", Toast.LENGTH_SHORT).show();

                    }

                    else

                        Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
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
            protected Map<String, String> getParams() {


                Map<String, String> param = new HashMap<String, String>();

                param.put("TASK_DES",getTASK_DES);
                param.put("TASK_PRIORITY",getTASK_PRIORITY);
                param.put("CREATED_BY",getCREATED_BY);
                param.put("TASK_ASSIGN",getTASK_ASSIGN);


                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);

    }
    public void onBackPressed() {
        Intent back = new Intent(getApplicationContext(), Dashboard.class);
        back.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        back.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(back);
        overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);
    }


}
