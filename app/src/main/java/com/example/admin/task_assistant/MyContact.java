package com.example.admin.task_assistant;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MyContact extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar toolbar;

    List<Contact> ListOfcontact = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManagerOfrecyclerView;
    RecyclerViewAdapter recyclerViewadapter;

    TextView name1,email1;
    CircleImageView circleImageView;
    LinearLayout layout1;
    String mobile,TASK_ID,name,email,usertyp;
    private SwipeRefreshLayout swipeRefreshLayout;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    private static String MY_CONTACT_URL = "https://orgone.solutions/task/userdata.php";

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

        setContentView(R.layout.activity_my_contact);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_white_24dp);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        View view= navigationView.getHeaderView(0);
        name1 = (TextView)view.findViewById(R.id.name);
        email1 = (TextView)view.findViewById(R.id.mailid);
        circleImageView= (CircleImageView) findViewById(R.id.image);
        swipeRefreshLayout= (SwipeRefreshLayout) findViewById(R.id.refresh);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                contactData();
            }
        });


        navigationView.setNavigationItemSelectedListener(this);

        pref = getApplication().getSharedPreferences("Options", MODE_PRIVATE);
        name = pref.getString("name", "");
        email = pref.getString("email", "");
        mobile = pref.getString("mobile", "");
        name1.setText(name);
        email1.setText(email);



        ListOfcontact = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view2);
        recyclerView.setHasFixedSize(true);
        recyclerViewadapter = new RecyclerViewAdapter(ListOfcontact,getApplicationContext());
        layoutManagerOfrecyclerView = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManagerOfrecyclerView);
        recyclerView.setAdapter(recyclerViewadapter);



        pref=getApplication().getSharedPreferences("Options",MODE_PRIVATE);
        usertyp = pref.getString("usertyp", "");

        System.out.println("Task_Login:-"+usertyp);

        if(!usertyp.equalsIgnoreCase("txtadmin")) {


            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.nav_add_contact).setVisible(false);

        }
        else
        {
            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.nav_add_contact).setVisible(true);

        }

        contactData();

    }


    //ProgressDialog progressDialog;

    public void contactData() {

     //   progressDialog = new ProgressDialog(MyContact.this);
      //  progressDialog.setMessage("Loading, please wait...");
      //  progressDialog.show();


        int i = 0;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, MY_CONTACT_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                   // progressDialog.dismiss();
                    JSONArray jsonObj = new JSONArray(response);
                    final int numberOfItemsInResp = jsonObj.length();
                    ListOfcontact.clear();
                    for (int i = 0; i < numberOfItemsInResp; i++) {
                        JSONObject jsonObject = (JSONObject) jsonObj.get(i);

                        String name = jsonObject.getString("name");
                        String phone = jsonObject.getString("mobile_no");
                        String tag = jsonObject.getString("tag");

                        Contact mData = new Contact(name, phone,tag);

                        ListOfcontact.add(mData);
                        recyclerView.setAdapter(recyclerViewadapter);

                        pref = getApplicationContext().getSharedPreferences("Options", MODE_PRIVATE);
                        editor=pref.edit();
                        editor.putString("tag", tag);
                        editor.commit();


                    }

                    swipeRefreshLayout.setRefreshing(false);

                } catch (JSONException e) {

                    Toast.makeText(MyContact.this, response, Toast.LENGTH_LONG).show();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                //progressDialog.dismiss();

                Toast.makeText(MyContact.this, error.toString(), Toast.LENGTH_LONG).show();

            }

        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Config.KEY_MOBILE, mobile);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(MyContact.this);
        requestQueue.add(stringRequest);

    }
    @Override
    public void onBackPressed() {
        Intent back = new Intent(getApplicationContext(), Dashboard.class);
        back.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        back.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(back);
        overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_contact) {
            Intent intent=new Intent(MyContact.this,MyContact.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);

        }
        else if (id == R.id.nav_assigntask) {
            Intent intent=new Intent(MyContact.this,TaskAssign.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);

        }
        else if (id == R.id.nav_newtask) {
            Intent intent=new Intent(MyContact.this,NewTask.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);

        }
        else if (id == R.id.nav_closetask) {
            Intent intent=new Intent(MyContact.this,Close.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);

        }
        else if (id == R.id.nav_profile) {
            Intent intent=new Intent(MyContact.this,Profile.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);

        }
        else if (id == R.id.nav_mydashboard) {
            Intent intent=new Intent(MyContact.this,Dashboard.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);

        }
        else if (id == R.id.nav_add_contact) {
            Intent intent = new Intent(MyContact.this, Add_Contact.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);
        }
        else if (id == R.id.nav_mytodo) {
            Intent intent = new Intent(MyContact.this, MyTodoTask.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);
        }
        else if (id == R.id.nav_group) {
            Intent intent = new Intent(MyContact.this, Group.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);
        }
        else if (id == R.id.nav_group) {
            Intent intent = new Intent(MyContact.this, Group.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);
        }
        else if (id == R.id.nav_mygroupTask) {
            Intent intent = new Intent(MyContact.this, MyGroupTask.class);
            startActivity(intent);
            overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);
        }
        else if (id == R.id.nav_mygroups) {
            Intent intent = new Intent(MyContact.this, MyGroups.class);
            startActivity(intent);
            overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);
        }





        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
