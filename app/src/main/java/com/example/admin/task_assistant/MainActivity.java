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
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String name,mobile,email,tag,Admin,usertyp,txtadmin;
    SharedPreferences pref;

    TextView t1,t2;

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

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        pref = getApplication().getSharedPreferences("Options", MODE_PRIVATE);
        tag = pref.getString("usertyp", "");


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        t1= (TextView) findViewById(R.id.name);
        t2= (TextView) findViewById(R.id.mailid);

        String n = pref.getString("name", "");
        String e = pref.getString("email", "");
        t1.setText(n);
        t2.setText(e);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            Intent intent = new Intent(MainActivity.this, Signin.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_contact) {
            Intent intent = new Intent(MainActivity.this, MyContact.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);

        }
        else if (id == R.id.nav_assigntask) {
            Intent intent = new Intent(MainActivity.this, TaskAssign.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);

        }
        else if (id == R.id.nav_newtask) {
            Intent intent = new Intent(MainActivity.this, NewTask.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);

        }
        else if (id == R.id.nav_add_contact) {
            Intent intent = new Intent(MainActivity.this, Add_Contact.class);
            startActivity(intent);
            overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);
        }
        else if (id == R.id.nav_mytodo) {
            Intent intent = new Intent(MainActivity.this, MyTodoTask.class);
            startActivity(intent);
            overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);
        }

        else if (id == R.id.nav_mygroups) {
            Intent intent = new Intent(MainActivity.this, MyGroups.class);
            startActivity(intent);
            overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
