package com.example.admin.task_assistant;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class TaskAssignComment extends AppCompatActivity implements View.OnClickListener {

    String TASK_ID, mobile, name, email;
    SharedPreferences pref;
    TextView t2,t3;
    Button button_Reassign,button_Close;
    LinearLayout rootLayout;
    Button update, send;
    EditText comment, Mycomment;

    private static String REASSIGN_URL = "https://orgone.solutions/task/taskreasign.php";
    private static String TASK_ASSIGN_DETAILS_URL = "https://orgone.solutions/task/taskdetails.php";
    private static String TASK_CLOSE_URL = "https://orgone.solutions/task/taskclose.php";


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


        setContentView(R.layout.activity_task_assign_comment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_white_24dp);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(TaskAssignComment.this, TaskAssign.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);

            }
        });


       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pref = getApplication().getSharedPreferences("Options", MODE_PRIVATE);
        name = pref.getString("name", "");
        email = pref.getString("email", "");
        mobile = pref.getString("mobile", "");
       // TASK_ID = pref.getString("TASK_ID", "");

        TASK_ID = getIntent().getExtras().getString("TASK_ID");

        t2 = (TextView) findViewById(R.id.etDes);
        t3 = (TextView) findViewById(R.id.status);

        button_Reassign = (Button) findViewById(R.id.reassign);
        button_Close = (Button) findViewById(R.id.close);
        rootLayout= (LinearLayout) findViewById(R.id.taskassignupdate);

        taskDetails(TASK_ID);

        button_Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

                    closeStatus();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        button_Reassign.setOnClickListener(this);

    }

    public void taskDetails(final String TASK_ID) {

        int i = 0;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, TASK_ASSIGN_DETAILS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(login.this, response, Toast.LENGTH_SHORT).show();
                        try {

                            JSONArray jsonObj = new JSONArray(response);
                            final int numberOfItemsInResp = jsonObj.length();
                            for (int i = 0; i < numberOfItemsInResp; i++) {
                                JSONObject jsonObject = (JSONObject) jsonObj.get(i);

                               // String TASK_TITLE = jsonObject.getString("TASK_TITLE");
                              //  t1.setText(TASK_TITLE);
                                String TASK_DES = jsonObject.getString("TASK_DES");
                                t2.setText(TASK_DES);
                                String TASK_STATUS = jsonObject.getString("TASK_STATUS");
                                t3.setText(TASK_STATUS);
                                // String priority = jsonObject.getString("TASK_PRIORITY");
                                // t5.setText(priority);

                            }

                        } catch (JSONException e) {
                            Toast.makeText(TaskAssignComment.this, response, Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                        Toast.makeText(TaskAssignComment.this, error.toString(), Toast.LENGTH_LONG).show();

                    }

                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("TASK_ID", TASK_ID);
                params.put("GROUP_NAME", " ");

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(TaskAssignComment.this);
        requestQueue.add(stringRequest);
    }

    private void closeStatus() throws JSONException {


        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.POST, TASK_CLOSE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        try {
                            if (jsonObject.getInt("success") == 1) {
                                //dismissing the progressbar

                                Snackbar.make(rootLayout, jsonObject.getString("message") + "", Snackbar.LENGTH_LONG)
                                        .show();
                                //Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                //Starting a new activity
                                        Intent intent = new Intent(TaskAssignComment.this, TaskAssign.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

                            } else {
                                //Displaying a toast if the otp entered is wrong
                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(TaskAssignComment.this, TaskAssign.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(TaskAssignComment.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                //Adding the parameters otp and username
                ;
                params.put(Config.KEY_TASK_ID, TASK_ID);
                return params;
            }
        };

        //Adding the request to the queue
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        requestQueue.add(postRequest);

    }

    @Override
    public void onClick(View view) {
        try {
            reAssignTask();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void reAssignTask() throws JSONException {

        LayoutInflater li = LayoutInflater.from(this);

        View confirmDialog = li.inflate(R.layout.dialog_taskassign_comment, null);

        send = (AppCompatButton) confirmDialog.findViewById(R.id.buttonSend);
        Mycomment = (EditText) confirmDialog.findViewById(R.id.editTextComment);

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setView(confirmDialog);

        final AlertDialog alertDialog = alert.create();

        alertDialog.show();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();

                final ProgressDialog loading = ProgressDialog.show(TaskAssignComment.this, "Authenticating", "Please wait while we check the entered code", false, false);

                final String mycomment = Mycomment.getText().toString().trim();

                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                StringRequest postRequest = new StringRequest(Request.Method.POST, REASSIGN_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(response);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                try {
                                    if (jsonObject.getInt("success") == 1) {

                                        loading.dismiss();

                                        Snackbar.make(rootLayout, jsonObject.getString("message") + "", Snackbar.LENGTH_LONG)
                                                .show();

                                    } else {

                                        Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(TaskAssignComment.this, TaskAssignComment.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                alertDialog.dismiss();
                                Toast.makeText(TaskAssignComment.this, error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();

                        params.put(Config.KEY_TASK_ID, TASK_ID);
                        params.put(Config.KEY_COMMENT, mycomment);

                        return params;
                    }
                };


                int socketTimeout = 30000;
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                postRequest.setRetryPolicy(policy);
                requestQueue.add(postRequest);
            }
        });
    }
    public void onBackPressed() {
        Intent back = new Intent(getApplicationContext(), TaskAssign.class);
        back.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        back.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(back);
        overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);
    }



}
