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
import com.example.admin.task_assistant.model.GroupTask;
import com.example.admin.task_assistant.model.GroupTaskDetails;
import com.example.admin.task_assistant.model.MyGroups;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MyGroupTask extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String createdBy;
    RecyclerView recyclerView,recyclerView1;
    List<GroupTaskDetails> groupTaskDetailsList;
    ProgressDialog progressDialog;
    MyGroupsTaskAdapter myGroupsTaskAdapter;

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

        setContentView(R.layout.activity_my_group_task);

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
        recyclerView=(RecyclerView)findViewById(R.id.my_recycler_view4);
        recyclerView1=(RecyclerView)findViewById(R.id.my_recycler_view5);
        txt1=(TextView)findViewById(R.id.txt1);
        txt2=(TextView)findViewById(R.id.txt2);

        navigationView.setNavigationItemSelectedListener(this);

        recyclerView1.setVisibility(View.GONE);
        txt1.setVisibility(View.GONE);
        txt2.setVisibility(View.GONE);


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
        progressDialog = new ProgressDialog(MyGroupTask.this);
        progressDialog.setMessage("Loading, please wait...");
        progressDialog.show();

        Call<GroupTask> mygrouptasks = APIClient.getInstance().mygrouptasks(mobile);

        mygrouptasks.enqueue(new Callback<GroupTask>() {
            @Override
            public void onResponse(Call<GroupTask> call, Response<GroupTask> response) {
                if (response.isSuccessful()) {
                    GroupTask groupTask = response.body();
                    ArrayList al = new ArrayList();
                    if (groupTask.getSuccess().equalsIgnoreCase("1")) {
                        groupTaskDetailsList = groupTask.getGroupTaskDetails();
                        System.out.println("DivyaGroupTaskDetailsNo:-" + groupTaskDetailsList.size());
                        if (groupTaskDetailsList.size() == 0) {
                         //   txtnorec.setVisibility(View.VISIBLE);
                        } else {
                          //  txtnorec.setVisibility(View.GONE);
                        }
                        for (int i = 0; i < groupTaskDetailsList.size(); i++) {
                            String TASK_ID = groupTaskDetailsList.get(i).getTASK_ID().toString();
                            String TASK_DES = groupTaskDetailsList.get(i).getTASK_DES();
                            String TASK_COMMENT = groupTaskDetailsList.get(i).getTASK_COMMENT();
                            String TASK_PRIORITY = groupTaskDetailsList.get(i).getTASK_PRIORITY();
                            String TASK_STATUS = groupTaskDetailsList.get(i).getTASK_STATUS();
                            String TASK_GROUP = groupTaskDetailsList.get(i).getTASK_GROUP();
                            String TASK_ASSIGN1 = groupTaskDetailsList.get(i).getTASK_ASSIGN();
                            String TASK_ASSIGN = TASK_ASSIGN1.substring(0, TASK_ASSIGN1.indexOf('-'));
                            al.add(new GroupTaskDetails(TASK_ID, TASK_DES, TASK_COMMENT, TASK_PRIORITY, TASK_STATUS, TASK_GROUP, TASK_ASSIGN));

                            System.out.println("DivyaAL:-" + TASK_ID + TASK_ASSIGN);
                        }

                        myGroupsTaskAdapter = new MyGroupsTaskAdapter(getApplicationContext(), al);
                        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.setAdapter(myGroupsTaskAdapter);
                        progressDialog.dismiss();


                    }
                }
            }

            @Override
            public void onFailure(Call<GroupTask> call, Throwable t) {
                progressDialog.dismiss();
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
            Intent intent = new Intent(MyGroupTask.this, MyContact.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

        } else if (id == R.id.nav_assigntask) {
            Intent intent = new Intent(MyGroupTask.this, TaskAssign.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

        } else if (id == R.id.nav_newtask) {
            Intent intent = new Intent(MyGroupTask.this, NewTask.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

        } else if (id == R.id.nav_closetask) {
            Intent intent = new Intent(MyGroupTask.this, Close.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

        } else if (id == R.id.nav_profile) {
            Intent intent = new Intent(MyGroupTask.this, Profile.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

        } else if (id == R.id.nav_mydashboard) {
            Intent intent = new Intent(MyGroupTask.this, Dashboard.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

        } else if (id == R.id.nav_add_contact) {
            Intent intent = new Intent(MyGroupTask.this, Add_Contact.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        } else if (id == R.id.nav_mytodo) {
            Intent intent = new Intent(MyGroupTask.this, MyTodoTask.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        } else if (id == R.id.nav_group) {
            Intent intent = new Intent(MyGroupTask.this, Group.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
        else if (id == R.id.nav_mygroupTask) {
            Intent intent = new Intent(MyGroupTask.this, MyGroupTask.class);
            startActivity(intent);
            overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




}
