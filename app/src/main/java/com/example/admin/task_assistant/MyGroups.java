package com.example.admin.task_assistant;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.admin.task_assistant.Network.APIClient;
import com.example.admin.task_assistant.adapter.ShowMyGroupAdapter;
import com.example.admin.task_assistant.model.MyGroupMemberDetails;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MyGroups extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String createdBy;
    RecyclerView recyclerView;
    ShowMyGroupAdapter showMyGroupAdapter;
    List<MyGroupMemberDetails> myGroupMemberDetails;


    TextView name1, email1, t3;
    CircleImageView profile;
    String mobile, TASK_ID, name, email, CREATED_BY, usertyp;

    TextView txtnorec,txt1,txt2;
    ImageView no_record;

    LinearLayout layout1;

    private static String PROFILE_URL = "https://orgone.solutions/task/profile.php";


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

        setContentView(R.layout.activity_mygroups);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_white_24dp);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Groups");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        // navigationView.setNavigationItemSelectedListener(this);
        View view = navigationView.getHeaderView(0);
        name1 = (TextView) view.findViewById(R.id.name);
        email1 = (TextView) view.findViewById(R.id.mailid);
        profile= (CircleImageView)view. findViewById(R.id.imageView);

        t3 = (TextView) findViewById(R.id.count);

        recyclerView = (RecyclerView)findViewById(R.id.recshowgroup);

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        pref = getApplication().getSharedPreferences("Options", MODE_PRIVATE);
        name = pref.getString("name", "");
        email = pref.getString("email", "");
        mobile = pref.getString("mobile", "");

        pref=getApplication().getSharedPreferences("Options",MODE_PRIVATE);
        usertyp = pref.getString("usertyp", "");

        System.out.println("Task_Login:-"+usertyp);

        if(!usertyp.equalsIgnoreCase("txtadmin")) {

            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.nav_add_contact).setVisible(false);
            nav_Menu.findItem(R.id.nav_group).setVisible(false);
            nav_Menu.findItem(R.id.nav_closegrouptask).setVisible(false);
            nav_Menu.findItem(R.id.nav_mygroups).setVisible(true);
            nav_Menu.findItem(R.id.nav_mygroupTask).setVisible(true);
            nav_Menu.findItem(R.id.nav_mygrouptodo).setVisible(true);

        }
        else if(usertyp.equalsIgnoreCase("txtadmin"))
        {
            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.nav_add_contact).setVisible(true);
            nav_Menu.findItem(R.id.nav_group).setVisible(true);
            nav_Menu.findItem(R.id.nav_closegrouptask).setVisible(true);
            nav_Menu.findItem(R.id.nav_mygroups).setVisible(false);
            nav_Menu.findItem(R.id.nav_mygroupTask).setVisible(false);
            nav_Menu.findItem(R.id.nav_mygrouptodo).setVisible(false);

        }
        else
        {
            Toast.makeText(MyGroups.this, "error", Toast.LENGTH_LONG).show();

        }

        System.out.println("FragGroupCreatedBy:-" + createdBy);

        Call<com.example.admin.task_assistant.model.MyGroups> myallgroups = APIClient.getInstance().myallgroups(mobile);

        myallgroups.enqueue(new Callback<com.example.admin.task_assistant.model.MyGroups>() {
            @Override
            public void onResponse(Call<com.example.admin.task_assistant.model.MyGroups> call, Response<com.example.admin.task_assistant.model.MyGroups> response) {
                if (response.isSuccessful()) {
                    ArrayList al = new ArrayList();
                    myGroupMemberDetails = response.body().getMyGroupMemberDetails();

                    for (int i = 0; i < myGroupMemberDetails.size(); i++) {
                        String groupname = myGroupMemberDetails.get(i).getGROUP_NAME();
                        String groupcreatedby = myGroupMemberDetails.get(i).getCREATED_BY();
                        String createddate = myGroupMemberDetails.get(i).getCREATED_DATE();
                        System.out.println("DivyaGroupCreatedBy:-" + groupcreatedby);
                        al.add(new MyGroupMemberDetails(groupname, groupcreatedby,createddate));
                    }

                    showMyGroupAdapter = new ShowMyGroupAdapter(getApplicationContext(), al);
                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setAdapter(showMyGroupAdapter);
                  // progressDialog.dismiss();

                }
            }

            @Override
            public void onFailure(Call<com.example.admin.task_assistant.model.MyGroups> call, Throwable t) {

            }
        });


        StringRequest stringRequest = new StringRequest(Request.Method.POST, PROFILE_URL, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(login.this, response, Toast.LENGTH_SHORT).show();
                try {
                    // progressDialog.dismiss();
                    JSONArray jsonObj = new JSONArray(response);
                    final int numberOfItemsInResp = jsonObj.length();
                    for (int i = 0; i < numberOfItemsInResp; i++) {
                        JSONObject jsonObject = (JSONObject) jsonObj.get(i);

                        String name = jsonObject.getString("name");
                        System.out.println("name:-"+name);
                        name1.setText(name);

                        String email = jsonObject.getString("email");
                        System.out.println("email:-"+email);
                        email1.setText(email);


                        String img = jsonObject.getString("USER_PHOTO");
                        // System.out.println("profile:-"+img);

                        if(jsonObject.getString("USER_PHOTO").equals(""))
                        {
                            profile.setImageResource(R.drawable.pro1);
                        }
                        else {

                            System.out.println("profile1:-"+img);
                            Picasso.with(getApplicationContext()).load("https://orgone.solutions/task/image/"+img)
                                    .into(profile);
                        }




                    }

                } catch (JSONException e) {
                    Toast.makeText(MyGroups.this, response, Toast.LENGTH_LONG).show();
                }


            }

        },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                        Toast.makeText(MyGroups.this, error.toString(), Toast.LENGTH_LONG).show();

                    }

                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mobile", mobile);
                params.put("usertyp", usertyp);


                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(MyGroups.this);
        requestQueue.add(stringRequest);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        Intent i = new Intent(getApplicationContext(), Dashboard.class);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
       /* if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {


        }*/
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_contact) {
            Intent intent = new Intent(MyGroups.this, MyContact.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

        } else if (id == R.id.nav_mytodo) {
            Intent intent = new Intent(MyGroups.this, MyTodoTask.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }  else if (id == R.id.nav_mygrouptodo) {
            Intent intent = new Intent(MyGroups.this, MyGroupTodo.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }else if (id == R.id.nav_assigntask) {
            Intent intent = new Intent(MyGroups.this, TaskAssign.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

        } else if (id == R.id.nav_newtask) {
            Intent intent = new Intent(MyGroups.this, NewTask.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

        } else if (id == R.id.nav_closetask) {
            Intent intent = new Intent(MyGroups.this, Close.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

        } else if (id == R.id.nav_profile) {
            Intent intent = new Intent(MyGroups.this, Profile.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

        } else if (id == R.id.nav_mydashboard) {
            Intent intent = new Intent(MyGroups.this, Dashboard.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

        } else if (id == R.id.nav_add_contact) {
            Intent intent = new Intent(MyGroups.this, Add_Contact.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        } else if (id == R.id.nav_group) {
            Intent intent = new Intent(MyGroups.this, Group.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
        else if (id == R.id.nav_mygroupTask) {
            Intent intent = new Intent(MyGroups.this, MyGroupTask.class);
            startActivity(intent);
            overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);
        }
        else if (id == R.id.nav_mygroups) {
            Intent intent = new Intent(MyGroups.this, MyGroups.class);
            startActivity(intent);
            overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);
        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




}
