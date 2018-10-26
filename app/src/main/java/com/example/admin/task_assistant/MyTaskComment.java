package com.example.admin.task_assistant;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
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

public class MyTaskComment extends AppCompatActivity implements View.OnClickListener {

    TextView t1, t2, t5;
    Button update, comment_task, send;
    EditText comment, Mycomment;
    private int mYear, mMonth, mDay;
    String TASK_ID, mobile, name, email, TASK_STATUS, CREATED_BY, GROUP_NAME;
    LinearLayout rootLayout;
    private Button sendButton, dash;
    private Toolbar toolbar;
    private int mHour, mMinute;

    SharedPreferences pref;
    Spinner spinnerDropDown2;

    private static String COMMENT_URL = "https://orgone.solutions/task/taskupdate.php";
    private static String TASK_DETAILS_URL = "https://orgone.solutions/task/taskdetails.php";
    private static String TASK_DONE_URL = "https://orgone.solutions/task/taskstatusupdate.php";

    public void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("font/Arkhip_font.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        setContentView(R.layout.activity_my_task_comment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_white_24dp);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(MyTaskComment.this, MyTask.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("CREATED_BY", CREATED_BY);
                intent.putExtra("GROUP_NAME", GROUP_NAME);
                startActivity(intent);
                overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);

            }
        });

        pref = getApplication().getSharedPreferences("Options", MODE_PRIVATE);
        name = pref.getString("name", "");
        email = pref.getString("email", "");
        mobile = pref.getString("mobile", "");
       // TASK_ID = pref.getString("TASK_ID", "");
        CREATED_BY = getIntent().getExtras().getString("CREATED_BY");
        TASK_ID=getIntent().getExtras().getString("TASK_ID");
        GROUP_NAME = getIntent().getExtras().getString("GROUP_NAME");
        // TASK_STATUS = getIntent().getStringExtra("TASK_STATUS");


        t2 = (TextView) findViewById(R.id.etDes);
        update = (Button) findViewById(R.id.update);
        comment_task = (Button) findViewById(R.id.t_comment);
        rootLayout = (LinearLayout) findViewById(R.id.mytaskupdate);
        dash = (Button) findViewById(R.id.goto_dash);

        comment_task.setOnClickListener(this);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

                    doneStatus();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        dash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyTaskComment.this, MyTask.class);
                intent.putExtra("CREATED_BY", CREATED_BY);
                intent.putExtra("GROUP_NAME", GROUP_NAME);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });


        taskDetails(TASK_ID);

    }

    private void doneStatus() throws JSONException {

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.POST, TASK_DONE_URL,
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

                                // Snackbar.make(rootLayout, jsonObject.getString("message") + "", Snackbar.LENGTH_LONG)
                                //        .show();
                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                //Starting a new activity
                                Intent intent = new Intent(MyTaskComment.this, MyTask.class);
                                intent.putExtra("CREATED_BY", CREATED_BY);
                                intent.putExtra("GROUP_NAME", GROUP_NAME);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

                            } else {
                                //Displaying a toast if the otp entered is wrong
                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(MyTaskComment.this, MyTaskComment.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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

                        Toast.makeText(MyTaskComment.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                //Adding the parameters otp and username
                ;
                params.put(Config.KEY_TASK_ID, TASK_ID);
                params.put(Config.KEY_TASK_STATUS, "DONE");
                params.put("GROUP_NAME", GROUP_NAME);

                return params;
            }
        };


        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        requestQueue.add(postRequest);

    }

    public void taskDetails(final String TASK_ID) {

        int i = 0;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, TASK_DETAILS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(login.this, response, Toast.LENGTH_SHORT).show();
                        try {

                            JSONArray jsonObj = new JSONArray(response);
                            final int numberOfItemsInResp = jsonObj.length();
                            for (int i = 0; i < numberOfItemsInResp; i++) {
                                JSONObject jsonObject = (JSONObject) jsonObj.get(i);

                                String TASK_DES = jsonObject.getString("TASK_DES");
                                t2.setText(TASK_DES);

                            }

                        } catch (JSONException e) {
                            Toast.makeText(MyTaskComment.this, response, Toast.LENGTH_LONG).show();
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                        Toast.makeText(MyTaskComment.this, error.toString(), Toast.LENGTH_LONG).show();

                    }

                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("TASK_ID", TASK_ID);
                params.put("GROUP_NAME", GROUP_NAME);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(MyTaskComment.this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onClick(View view) {

        try {

            comment();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void comment() throws JSONException {

        LayoutInflater li = LayoutInflater.from(this);

        View confirmDialog = li.inflate(R.layout.dialog_comment, null);

        send = (AppCompatButton) confirmDialog.findViewById(R.id.buttonSend);
        Mycomment = (EditText) confirmDialog.findViewById(R.id.editTextComment);

        //Creating an alertdialog builder
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        //Adding our dialog box to the view of alert dialog
        alert.setView(confirmDialog);

        //Creating an alert dialog
        final AlertDialog alertDialog = alert.create();

        //Displaying the alert dialog
        alertDialog.show();

        //On the click of the confirm button from alert dialog
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Hiding the alert dialog
                alertDialog.dismiss();

                //Displaying a progressbar
                final ProgressDialog loading = ProgressDialog.show(MyTaskComment.this, "Authenticating", "Please wait while we check the entered code", false, false);

                //Getting the user entered otp from edittext
                final String mycomment = Mycomment.getText().toString().trim();

                //Creating an string request
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                StringRequest postRequest = new StringRequest(Request.Method.POST, COMMENT_URL,
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
                                        loading.dismiss();

                                        Snackbar.make(rootLayout, jsonObject.getString("message") + "", Snackbar.LENGTH_LONG)
                                                .show();
                                        //Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                        //Starting a new activity
                                        Intent intent = new Intent(MyTaskComment.this, MyTask.class);
                                        intent.putExtra("CREATED_BY", CREATED_BY);
                                        intent.putExtra("GROUP_NAME", GROUP_NAME);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

                                    } else {
                                        //Displaying a toast if the otp entered is wrong
                                        Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(MyTaskComment.this, MyTaskComment.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
                                Toast.makeText(MyTaskComment.this, error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        //Adding the parameters otp and username
                        ;
                        params.put(Config.KEY_TASK_ID, TASK_ID);
                        params.put(Config.KEY_COMMENT, mycomment);
                        params.put("GROUP_NAME",GROUP_NAME );

                        return params;
                    }
                };

                //Adding the request to the queue
                int socketTimeout = 30000;
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                postRequest.setRetryPolicy(policy);
                requestQueue.add(postRequest);
            }
        });
    }

}