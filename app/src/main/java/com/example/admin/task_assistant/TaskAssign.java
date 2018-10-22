package com.example.admin.task_assistant;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
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

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class TaskAssign extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    SharedPreferences pref;

    List<Contact2> ListOfcontact2 = new ArrayList<>();
    RecyclerView recyclerView;

    RecyclerView.LayoutManager layoutManagerOfrecyclerView2;
    RecyclerViewAdapter2 recyclerViewadapter2;
    Spinner spinnerDropDown2;
    LinearLayout layout1;

    String mobile,name,email,assign,TASK_ID,CREATED_BY,usertyp;
    TextView name1,email1,create;
    ArrayList<String> ContactName;

    private static String ASSIGN_TASK_URL = "https://orgone.solutions/task/taskassign.php";
    private static String DROP_DOWN_URL = "https://orgone.solutions/task/assigntask_drop.php";

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
        setContentView(R.layout.activity_task_assign);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        navigationView.setItemIconTintList(null);

        create = (TextView)view.findViewById(R.id.Create);

        pref = getApplication().getSharedPreferences("Options", MODE_PRIVATE);
        name = pref.getString("name", "");
        email = pref.getString("email", "");
        mobile = pref.getString("mobile", "");

        name1.setText(name);
        email1.setText(email);

        ListOfcontact2 = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view3);
       // recyclerView.setHasFixedSize(true);
        recyclerViewadapter2 = new RecyclerViewAdapter2(ListOfcontact2,getApplicationContext());
        layoutManagerOfrecyclerView2= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManagerOfrecyclerView2);

        ContactName=new ArrayList<String>();

        spinnerDropDown2=(Spinner)findViewById(R.id.spinner3);

        loadSpinnerData();

        initCustomSpinner();

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
            Toast.makeText(TaskAssign.this, "error", Toast.LENGTH_LONG).show();

        }

    }

    private void initCustomSpinner() {

        Spinner spinnerDropDown2= (Spinner) findViewById(R.id.spinner3);

        ArrayList<String> ContactName = new ArrayList<String>();


        spinnerDropDown2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                int sid = parent.getSelectedItemPosition();
                taskassign(sid);
                //  Toast.makeText(parent.getContext(), "Android Custom Spinner Example Output..." + sid, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void loadSpinnerData() {

        int i = 0;

        StringRequest stringRequest=new StringRequest(Request.Method.POST, DROP_DOWN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONArray jsonObj = new JSONArray(response);
                    final int numberOfItemsInResp = jsonObj.length();
                    ContactName.clear();
                    for (int i = 0; i < numberOfItemsInResp; i++) {
                        JSONObject jsonObject = (JSONObject) jsonObj.get(i);

                        assign = jsonObject.getString("TASK_ASSIGN");

                        ContactName.add( assign);

                    }
                    CustomSpinnerAdapter customSpinnerAdapter=new CustomSpinnerAdapter(TaskAssign.this,ContactName);
                    spinnerDropDown2.setAdapter(customSpinnerAdapter);

                }catch (JSONException e){e.printStackTrace();}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name",name);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(TaskAssign.this);
        requestQueue.add(stringRequest);
    }


    ProgressDialog progressDialog;

    public void taskassign(final int pos) {

        progressDialog = new ProgressDialog(TaskAssign.this);
        progressDialog.setMessage("Loading, please wait...");
        progressDialog.show();
        int i = 0;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ASSIGN_TASK_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(login.this, response, Toast.LENGTH_SHORT).show();
                try {
                    progressDialog.dismiss();
                    JSONArray jsonObj = new JSONArray(response);
                    final int numberOfItemsInResp = jsonObj.length();
                    ListOfcontact2.clear();
                    for (int i = 0; i < numberOfItemsInResp; i++) {
                        JSONObject jsonObject = (JSONObject) jsonObj.get(i);

                        String title = jsonObject.getString("TASK_DES");
                        String priority=jsonObject.getString("TASK_PRIORITY");
                        TASK_ID = jsonObject.getString("TASK_ID");
                        String status = jsonObject.getString("TASK_STATUS");
                        String comment = jsonObject.getString("TASK_COMMENT");
                        String assign = jsonObject.getString("TASK_ASSIGN");
                        String assign_name = assign.substring(0, assign.indexOf('-'));

                        Contact2 mData2 = new Contact2(title,priority,status,assign_name,TASK_ID,comment);
                        ListOfcontact2.add(mData2);

                    }

                    recyclerViewadapter2.setData(ListOfcontact2);
                    recyclerView.setAdapter(recyclerViewadapter2);


                } catch (JSONException e) {
                    Toast.makeText(TaskAssign.this, response, Toast.LENGTH_LONG).show();
                }


            }

        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();

                        Toast.makeText(TaskAssign.this, error.toString(), Toast.LENGTH_LONG).show();

                    }

                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name",name);
                params.put("assign",ContactName.get(pos).toString());



                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(TaskAssign.this);
        requestQueue.add(stringRequest);
    }

    /*@Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }*/


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_contact) {
            Intent intent=new Intent(TaskAssign.this,MyContact.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);

        }
        else if (id == R.id.nav_assigntask) {
            Intent intent=new Intent(TaskAssign.this,TaskAssign.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);
        }
        else if (id == R.id.nav_newtask) {
            Intent intent=new Intent(TaskAssign.this,NewTask.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);

        }

        else if (id == R.id.nav_closetask) {
            Intent intent=new Intent(TaskAssign.this,Close.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);

        }
        else if (id == R.id.nav_profile) {
            Intent intent=new Intent(TaskAssign.this,Profile.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);

        }
        else if (id == R.id.nav_mydashboard) {
            Intent intent=new Intent(TaskAssign.this,Dashboard.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);

        }

        else if (id == R.id.nav_add_contact) {
            Intent intent = new Intent(TaskAssign.this, Add_Contact.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);

        }
        else if (id == R.id.nav_add_contact) {
            Intent intent=new Intent(TaskAssign.this,Add_Contact.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);

        }
        else if (id == R.id.nav_mytodo) {
            Intent intent = new Intent(TaskAssign.this, MyTodoTask.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);
        }
        else if (id == R.id.nav_group) {
            Intent intent = new Intent(TaskAssign.this, Group.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);
        }

        else if (id == R.id.nav_group) {
            Intent intent = new Intent(TaskAssign.this, Group.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);
        }
        else if (id == R.id.nav_mygroupTask) {
            Intent intent = new Intent(TaskAssign.this, MyGroupTask.class);
            startActivity(intent);
            overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);
        }
        else if (id == R.id.nav_mygroups) {
            Intent intent = new Intent(TaskAssign.this, MyGroups.class);
            startActivity(intent);
            overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class CustomSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

        private final Context activity;
        private ArrayList<String> asr;

        Typeface font = Typeface.createFromAsset(getApplication().getAssets(),
                "font/Arkhip_font.ttf");

        public CustomSpinnerAdapter(Context context,ArrayList<String> asr) {
            this.asr=asr;
            activity = context;
        }



        public int getCount()
        {
            return asr.size();
        }

        public Object getItem(int i)
        {
            return asr.get(i);
        }

        public long getItemId(int i)
        {
            return (long)i;
        }



        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            TextView txt = new TextView(TaskAssign.this);
            txt.setPadding(16, 16, 16, 16);
            txt.setTextSize(16);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setText(asr.get(position));
            txt.setTypeface(font);
            txt.setTextColor(Color.parseColor("#212121"));
            return  txt;
        }

        public View getView(int i, View view, ViewGroup viewgroup) {
            TextView txt = new TextView(TaskAssign.this);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setPadding(16, 16, 16, 16);
            txt.setTextSize(16);
            txt.setTypeface(font);
            txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down, 0);
            txt.setText(asr.get(i));
            txt.setTextColor(Color.parseColor("#212121"));
            return  txt;
        }

    }
    public void onBackPressed() {
        Intent back = new Intent(getApplicationContext(), Dashboard.class);
        back.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        back.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(back);
        overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);
    }


}
