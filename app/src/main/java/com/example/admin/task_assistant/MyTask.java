package com.example.admin.task_assistant;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
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
import com.example.admin.task_assistant.model.MyTodo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MyTask extends AppCompatActivity {

    List<Contact1> ListOfcontact1 = new ArrayList<>();
    RecyclerView recyclerView;

    String TASK_ID, name, email, mobile, CREATED_BY, usertyp, GROUP_NAME;
    TextView name1, email1;
    CardView cardView;

    RecyclerView.LayoutManager layoutManagerOfrecyclerView1;
    RecyclerViewAdapter1 recyclerViewadapter1;
    LinearLayout layout1;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    private static String UPDATE_STATUS_URL = "https://orgone.solutions/task/taskproc.php";
    private static String MY_TASK_URL = "https://orgone.solutions/task/mytask.php";

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

        setContentView(R.layout.activity_my_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_white_24dp);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(MyTask.this, MyTodoTask.class);
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
        // CREATED_BY = pref.getString("CREATED_BY", "");
        TASK_ID = pref.getString("TASK_ID", "");
        CREATED_BY = getIntent().getExtras().getString("CREATED_BY");
        GROUP_NAME = getIntent().getExtras().getString("GROUP_NAME");

        System.out.println("Groupname:-" + GROUP_NAME);


        ListOfcontact1 = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view1);
        recyclerView.setHasFixedSize(true);
        recyclerViewadapter1 = new RecyclerViewAdapter1(ListOfcontact1, getApplicationContext());
        layoutManagerOfrecyclerView1 = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManagerOfrecyclerView1);
        recyclerView.setAdapter(recyclerViewadapter1);

        mytask();

        cardView = (CardView) findViewById(R.id.cv1);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.layout1);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, final int position) {

                update(ListOfcontact1.get(position).getTASK_ID());


            }

            @Override
            public void onLongClick(View view, int position) {
                Toast.makeText(MyTask.this, "Long press on position :" + position,
                        Toast.LENGTH_LONG).show();
            }
        }));

        pref=getApplication().getSharedPreferences("Options",MODE_PRIVATE);
        usertyp = pref.getString("usertyp", "");


        System.out.println("Task_Login:-"+usertyp);


    }

    public static interface ClickListener {
        public void onClick(View view, int position);

        public void onLongClick(View view, int position);
    }

    ProgressDialog progressDialog;

    public void mytask() {

        progressDialog = new ProgressDialog(MyTask.this);
        progressDialog.setMessage("Loading, please wait...");
        progressDialog.show();
        int i = 0;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, MY_TASK_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(login.this, response, Toast.LENGTH_SHORT).show();
                try {
                    progressDialog.dismiss();
                    JSONArray jsonObj = new JSONArray(response);
                    final int numberOfItemsInResp = jsonObj.length();
                    for (int i = 0; i < numberOfItemsInResp; i++) {
                        JSONObject jsonObject = (JSONObject) jsonObj.get(i);

                        String title = jsonObject.getString("TASK_DES");
                        String priority = jsonObject.getString("TASK_PRIORITY");
                        TASK_ID = jsonObject.getString("TASK_ID");
                        CREATED_BY = jsonObject.getString("CREATED_BY");
                        String STATUS = jsonObject.getString("TASK_STATUS");
                        String COMMENT = jsonObject.getString("TASK_COMMENT");

                        pref = getApplicationContext().getSharedPreferences("Options", MODE_PRIVATE);
                        editor = pref.edit();
                        editor.putString("TASK_ID", TASK_ID);
                        editor.putString("CREATED_BY", CREATED_BY);
                        editor.commit();

                        Contact1 mData1 = new Contact1(title, priority, TASK_ID, CREATED_BY, STATUS, COMMENT);
                        ListOfcontact1.add(mData1);
                        recyclerView.setAdapter(recyclerViewadapter1);

                    }

                } catch (JSONException e) {
                    Toast.makeText(MyTask.this, response, Toast.LENGTH_LONG).show();
                }

            }

        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();

                        Toast.makeText(MyTask.this, error.toString(), Toast.LENGTH_LONG).show();

                    }

                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mobile", mobile);
                params.put("CREATED_BY", CREATED_BY);
               params.put("GROUP_NAME",GROUP_NAME);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(MyTask.this);
        requestQueue.add(stringRequest);
    }

    private void update(final String task_id) {

        //Creating an string request
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.POST, UPDATE_STATUS_URL,
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

                                JSONObject jsonObjectInfo = jsonObject.getJSONObject("User");
                                String TASK_ID = jsonObjectInfo.getString("TASK_ID");

                                // Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(MyTask.this, MyTaskComment.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("TASK_ID", TASK_ID);
                                intent.putExtra("CREATED_BY", CREATED_BY);
                                intent.putExtra("GROUP_NAME", GROUP_NAME);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

                              /*  pref = getApplicationContext().getSharedPreferences("Options", MODE_PRIVATE);
                                editor=pref.edit();
                                editor.putString("TASK_ID", TASK_ID);
                                editor.putString("CREATED_BY", CREATED_BY);
                                editor.commit();*/

                            } else {
                                //Displaying a toast if the otp entered is wrong
                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(MyTask.this, MyTask.class);
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

                        Toast.makeText(MyTask.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                //Adding the parameters otp and username

                params.put(Config.KEY_TASK_ID, task_id);
                params.put("GROUP_NAME", GROUP_NAME);

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
    public void onBackPressed() {
        Intent back = new Intent(getApplicationContext(), MyTodoTask.class);
        back.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        back.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(back);
        overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);
    }


    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private ClickListener clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(Context context, final RecyclerView recycleView, final ClickListener clicklistener) {

            this.clicklistener = clicklistener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recycleView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clicklistener != null) {
                        clicklistener.onLongClick(child, recycleView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clicklistener != null && gestureDetector.onTouchEvent(e)) {
                clicklistener.onClick(child, rv.getChildAdapterPosition(child));
            }

            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

}
