package com.example.admin.task_assistant;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.admin.task_assistant.adapter.GroupFragmentAdapter;
import com.example.admin.task_assistant.fragment.FragAddGroupUser;
import com.example.admin.task_assistant.fragment.FragGroup;
import com.example.admin.task_assistant.fragment.FragMyGroup;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Group extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar toolbar;

    TextView name1, email1, t3;
    String mobile, TASK_ID, name, email, CREATED_BY, usertyp,admin_name,admin_mob;

    SharedPreferences pref,pref1;
    SharedPreferences.Editor editor;

    LinearLayout layout1;

    TabLayout tabLayout;
    ViewPager viewPager;
    GroupFragmentAdapter groupFragmentAdapter;

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

        setContentView(R.layout.activity_group);
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

        navigationView.setNavigationItemSelectedListener(this);

        pref = getApplicationContext().getSharedPreferences("Options", MODE_PRIVATE);
        name = pref.getString("name", "");
        email = pref.getString("email", "");
        mobile = pref.getString("mobile", "");
        admin_mob=pref.getString("admin_mob","");
        admin_name=pref.getString("admin_name","");
        name1.setText(name);
        email1.setText(email);

        System.out.println("DivyaAdminCred:-"+admin_name+"--"+admin_mob);

        pref = getApplication().getSharedPreferences("Options", MODE_PRIVATE);
        usertyp = pref.getString("usertyp", "");

        System.out.println("Task_Login:-" + usertyp);

        if (!usertyp.equalsIgnoreCase("txtadmin")) {
            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.nav_add_contact).setVisible(false);

        } else {
            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.nav_add_contact).setVisible(true);
        }

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        groupFragmentAdapter = new GroupFragmentAdapter(Group.this, getSupportFragmentManager(), "group", "", "", "");
        viewPager.setAdapter(groupFragmentAdapter);
        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);

        //  setupViewPager(viewPager);
        //  tabLayout.getChildAt(0).setVisibility(View.GONE);

        //   tabHost.getTabWidget().getChildAt(0).setVisibility(View.GONE);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.setTabTextColors(
                getResources().getColor(R.color.black),
                getResources().getColor(R.color.white)
        );

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
            Intent intent = new Intent(Group.this, MyContact.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

        } else if (id == R.id.nav_assigntask) {
            Intent intent = new Intent(Group.this, TaskAssign.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

        } else if (id == R.id.nav_newtask) {
            Intent intent = new Intent(Group.this, NewTask.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

        } else if (id == R.id.nav_closetask) {
            Intent intent = new Intent(Group.this, Close.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

        } else if (id == R.id.nav_profile) {
            Intent intent = new Intent(Group.this, Profile.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

        } else if (id == R.id.nav_mydashboard) {
            Intent intent = new Intent(Group.this, Dashboard.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

        } else if (id == R.id.nav_add_contact) {
            Intent intent = new Intent(Group.this, Add_Contact.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        } else if (id == R.id.nav_mytodo) {
            Intent intent = new Intent(Group.this, MyTodoTask.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        } else if (id == R.id.nav_group) {
            Intent intent = new Intent(Group.this, Group.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
        else if (id == R.id.nav_mygroupTask) {
            Intent intent = new Intent(Group.this, MyGroupTask.class);
            startActivity(intent);
            overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);
        }
        else if (id == R.id.nav_mygroups) {
            Intent intent = new Intent(Group.this, MyGroups.class);
            startActivity(intent);
            overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);
        }




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


/*
    private void setupViewPager(ViewPager viewPager) {

        groupFragmentAdapter.addFragment(new FragGroup(), "Created Groups", 0);
        groupFragmentAdapter.addFragment(new FragAddGroupUser(), "Create Groups", 1);
        groupFragmentAdapter.addFragment(new FragMyGroup(), "My Groups", 2);
        groupFragmentAdapter.notifyDataSetChanged();

        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(groupFragmentAdapter);
    }
*/


}
