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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.admin.task_assistant.Network.APIClient;
import com.example.admin.task_assistant.adapter.MyGroupsTaskAdapter;
import com.example.admin.task_assistant.adapter.ShowMyGroupAdapter;
import com.example.admin.task_assistant.model.GroupTask;
import com.example.admin.task_assistant.model.GroupTaskDetails;
import com.example.admin.task_assistant.model.MyGroupMemberDetails;

import java.util.ArrayList;
import java.util.List;
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
    String mobile, TASK_ID, name, email, CREATED_BY, usertyp;

    TextView txtnorec,txt1,txt2;

    LinearLayout layout1;

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
        t3 = (TextView) findViewById(R.id.count);

        recyclerView = (RecyclerView)findViewById(R.id.recshowgroup);

        navigationView.setNavigationItemSelectedListener(this);


        pref = getApplication().getSharedPreferences("Options", MODE_PRIVATE);
        name = pref.getString("name", "");
        email = pref.getString("email", "");
        mobile = pref.getString("mobile", "");
        name1.setText(name);
        email1.setText(email);

        pref = getApplication().getSharedPreferences("Options", MODE_PRIVATE);
        usertyp = pref.getString("usertyp", "");

        createdBy = pref.getString("name", "");

        System.out.println("Task_Login:-" + usertyp);

        if (!usertyp.equalsIgnoreCase("txtadmin")) {
            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.nav_add_contact).setVisible(false);

        } else {
            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.nav_add_contact).setVisible(true);
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

        } else if (id == R.id.nav_assigntask) {
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
        } else if (id == R.id.nav_mytodo) {
            Intent intent = new Intent(MyGroups.this, MyTodoTask.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        } else if (id == R.id.nav_group) {
            Intent intent = new Intent(MyGroups.this, Group.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
        else if (id == R.id.nav_mygroupTask) {
            Intent intent = new Intent(MyGroups.this, MyGroups.class);
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
