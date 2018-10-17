package com.example.admin.task_assistant;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
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
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Dashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String TASK_ID,name,email,mobile,usertyp;
    TextView close_task,re_assign,in_process,done,active;
    SharedPreferences pref;
    TextView name1,email1;
    CardView c1,c2,c3,c4,c5;

    private static String DASHBOARD_URL = "https://orgone.solutions/task/dashbord.php";

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

        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pref=getApplication().getSharedPreferences("Options", MODE_PRIVATE);
        name = pref.getString("name", "");
        email = pref.getString("email", "");
        mobile = pref.getString("mobile", "");

        re_assign= (TextView) findViewById(R.id.reassign);
        in_process= (TextView) findViewById(R.id.in_process);
        done= (TextView) findViewById(R.id.done);
        active= (TextView) findViewById(R.id.active);
        close_task= (TextView) findViewById(R.id.close_task);

        c1= (CardView) findViewById(R.id.ActiveTask);
        c2= (CardView) findViewById(R.id.InprocessTask);
        c3= (CardView) findViewById(R.id.DoneTask);
        c4= (CardView) findViewById(R.id.ReassignTask);
        c5= (CardView) findViewById(R.id.CloseTask);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View view= navigationView.getHeaderView(0);
        name1 = (TextView)view.findViewById(R.id.name);
        email1 = (TextView)view.findViewById(R.id.mailid);
        navigationView.setNavigationItemSelectedListener(this);

        name=pref.getString("name", "");
        email=pref.getString("email", "");
        mobile=pref.getString("mobile", "");

        name1.setText(name);
        email1.setText(email);

        dashboard(DASHBOARD_URL,mobile);

        pref=getApplication().getSharedPreferences("Options",MODE_PRIVATE);
        usertyp = pref.getString("usertyp", "");


        System.out.println("Task_Login:-"+usertyp);

        if(!usertyp.equalsIgnoreCase("txtadmin")) {

            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.nav_add_contact).setVisible(false);
            nav_Menu.findItem(R.id.nav_mygroups).setVisible(true);
            nav_Menu.findItem(R.id.nav_group).setVisible(false);

        }
        else if(usertyp.equalsIgnoreCase("txtadmin"))
        {
            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.nav_add_contact).setVisible(true);
            nav_Menu.findItem(R.id.nav_mygroups).setVisible(false);
            nav_Menu.findItem(R.id.nav_group).setVisible(true);

        }
        else
        {
            Toast.makeText(Dashboard.this, "error", Toast.LENGTH_LONG).show();

        }
    }

    public void dashboard(String dashboardUrl,final String mobile) {

        int i = 0;
        RequestQueue requestQueue=new Volley().newRequestQueue(getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.POST, dashboardUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject=new JSONObject(response);

                    if(jsonObject.getInt("success")==1)
                    {
                        JSONObject jsonObjectInfo=jsonObject.getJSONObject("User");

                        String ACTIVE_TASK = jsonObjectInfo.getString("ACTIVE_COUNT");
                        active.setText(ACTIVE_TASK);

                        String INPROCESS_TASK=jsonObjectInfo.getString("INPROCESS_COUNT");
                        in_process.setText(INPROCESS_TASK);

                        String DONE_TASK = jsonObjectInfo.getString("DONE_COUNT");
                        done.setText(DONE_TASK);

                        String REASSIGN_TASK = jsonObjectInfo.getString("REASSIGNED_COUNT");
                        re_assign.setText(REASSIGN_TASK);

                        String CLOSE_TASK = jsonObjectInfo.getString("CLOSEDTASK_COUNT");
                        close_task.setText(CLOSE_TASK);


                    }


                } catch (JSONException e) {
                    Toast.makeText(Dashboard.this, response, Toast.LENGTH_LONG).show();
                }

            }

        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        error.printStackTrace();

                    }

                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mobile",mobile);

                return params;
            }

        };

        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        requestQueue.add(postRequest);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_mydashboard) {
            Intent intent=new Intent(Dashboard.this,Dashboard.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);

        }

        if (id == R.id.nav_contact) {
            Intent intent=new Intent(Dashboard.this,MyContact.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);

        }

        else if (id == R.id.nav_assigntask) {
            Intent intent=new Intent(Dashboard.this,TaskAssign.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);
        }
        else if (id == R.id.nav_newtask) {
            Intent intent=new Intent(Dashboard.this,NewTask.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);

        }
        else if (id == R.id.nav_closetask) {
            Intent intent=new Intent(Dashboard.this,Close.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);

        }
        else if (id == R.id.nav_profile) {
            Intent intent=new Intent(Dashboard.this,Profile.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);

        }

        else if (id == R.id.nav_add_contact) {
            Intent intent=new Intent(Dashboard.this,Add_Contact.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);

        }
        else if (id == R.id.nav_mytodo) {
            Intent intent = new Intent(Dashboard.this, MyTodoTask.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);
        }
        else if (id == R.id.nav_group) {
            Intent intent = new Intent(Dashboard.this, Group.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);
        }
        else if (id == R.id.nav_mygroupTask) {
            Intent intent = new Intent(Dashboard.this, MyGroupTask.class);
            startActivity(intent);
            overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);
        }
        else if (id == R.id.nav_mygroups) {
            Intent intent = new Intent(Dashboard.this, MyGroups.class);
            startActivity(intent);
            overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);
        }





        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
