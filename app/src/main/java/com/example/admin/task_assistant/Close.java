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
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.admin.task_assistant.Network.APIClient;
import com.example.admin.task_assistant.model.CloseData;
import com.example.admin.task_assistant.model.CloseTaskDetails;
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
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Close extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar toolbar;

    List<Contact4> ListOfcontact4 = new ArrayList<>();
    RecyclerView recyclerView6, recyclerView7;
    RecyclerView.LayoutManager layoutManagerOfrecyclerView;

    RecyclerViewAdapter6 recyclerViewadapter6;
    RecyclerViewAdapter7 recyclerViewadapter7;
    TextView name1, email1, t3;
    CircleImageView profile;
    String mobile, TASK_ID, name, email, CREATED_BY, Seen, usertyp;
    TextView txtnorec;
    ImageView no_record;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    ArrayList<CloseTaskDetails> aluser1 = new ArrayList();
    ArrayList<CloseTaskDetails> aluser2 = new ArrayList();

    TextView txt1, txt2;

    LinearLayout layout1;
    private static String PROFILE_URL = "https://orgone.solutions/task/profile.php";


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

        setContentView(R.layout.activity_my_todo_task);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_white_24dp);
        setSupportActionBar(toolbar);

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
        txtnorec = (TextView)findViewById(R.id.txtnorec);
        no_record=(ImageView)findViewById(R.id.no_record);

      /*  Bundle bundle=getIntent().getExtras();
        name1.setText(bundle.getString("name", String.valueOf(bundle)));
        email1.setText(bundle.getString("email", String.valueOf(bundle)));*/
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        pref = getApplication().getSharedPreferences("Options", MODE_PRIVATE);
        name = pref.getString("name", "");
        email = pref.getString("email", "");
        mobile = pref.getString("mobile", "");
       // name1.setText(name);
      //  email1.setText(email);

        ListOfcontact4 = new ArrayList<>();
        recyclerView6 = (RecyclerView) findViewById(R.id.my_recycler_view4);
      //  recyclerView7 = (RecyclerView) findViewById(R.id.my_recycler_view5);
       // txt1 = (TextView) findViewById(R.id.txt1);
       // txt2 = (TextView) findViewById(R.id.txt2);

        //txt1.setText("Closed Task");
        //txt2.setText("Closed Group Task");


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
            Toast.makeText(Close.this, "error", Toast.LENGTH_LONG).show();

        }

        Call<CloseData> closetask = APIClient.getInstance().closeTask(name);

        closetask.enqueue(new Callback<CloseData>() {
            @Override
            public void onResponse(Call<CloseData> call, retrofit2.Response<CloseData> response) {
                if (response.isSuccessful()) {

                    CloseData closeData = response.body();
                    List<CloseTaskDetails> closeTaskDetails1 = new ArrayList<>();
                    List<CloseTaskDetails> closeTaskDetails2 = new ArrayList<>();

                    if (closeData.getSuccess().equalsIgnoreCase("1")) {
                        closeTaskDetails1 = closeData.getCloseUser1();
                        closeTaskDetails2 = closeData.getCloseUser2();



                        for (int i = 0; i < closeTaskDetails1.size(); i++) {
                            String task_desc = closeTaskDetails1.get(i).getTASK_DES();
                            String assign = closeTaskDetails1.get(i).getTASK_ASSIGN();
                            String task_assign=assign.substring(0,assign.indexOf('-'));
                            String task_priority = closeTaskDetails1.get(i).getTASK_PRIORITY();
                            String task_group = closeTaskDetails1.get(i).getTASK_GROUP();
                            String task_id = closeTaskDetails1.get(i).getTASK_ID();
                            aluser1.add(new CloseTaskDetails(task_desc, task_assign, task_priority, task_group, task_id));

                            System.out.println("DivyaCloseTask:-" + task_desc + task_assign + task_priority + task_group + task_id);


                        }


                        for (int i = 0; i < closeTaskDetails2.size(); i++) {

                            String task_id = closeTaskDetails2.get(i).getTASK_ID();
                            String task_desc = closeTaskDetails2.get(i).getTASK_DES();
                            String task_assign = closeTaskDetails2.get(i).getTASK_ASSIGN();
                            String task_priority = closeTaskDetails2.get(i).getTASK_PRIORITY();
                            String task_group = closeTaskDetails2.get(i).getTASK_GROUP();
                            aluser2.add(new CloseTaskDetails(task_desc, task_assign, task_priority, task_group, task_id));

                            System.out.println("DivyaCloseTask:-" + task_desc + task_assign + task_priority + task_group + task_id);


                        }

                        recyclerViewadapter6 = new RecyclerViewAdapter6(aluser1, getApplicationContext());
                       // recyclerViewadapter7 = new RecyclerViewAdapter7(aluser2, getApplicationContext());

                        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                        LinearLayoutManager mLayoutManager1 = new LinearLayoutManager(getApplicationContext());

                        recyclerView6.setLayoutManager(mLayoutManager);
                        recyclerView6.setAdapter(recyclerViewadapter6);
                       // recyclerView7.setLayoutManager(mLayoutManager1);
                       // recyclerView7.setAdapter(recyclerViewadapter7);

                    }
                }

            }

            @Override
            public void onFailure(Call<CloseData> call, Throwable t) {

            }
        });



        StringRequest stringRequest = new StringRequest(Request.Method.POST, PROFILE_URL, new Response.Listener<String>() {
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
                    Toast.makeText(Close.this, response, Toast.LENGTH_LONG).show();
                }


            }

        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                        Toast.makeText(Close.this, error.toString(), Toast.LENGTH_LONG).show();

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

        RequestQueue requestQueue = Volley.newRequestQueue(Close.this);
        requestQueue.add(stringRequest);

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

        if (id == R.id.nav_contact) {
            Intent intent = new Intent(Close.this, MyContact.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

        } else if (id == R.id.nav_assigntask) {
            Intent intent = new Intent(Close.this, TaskAssign.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

        } else if (id == R.id.nav_newtask) {
            Intent intent = new Intent(Close.this, NewTask.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

        } else if (id == R.id.nav_closetask) {
            Intent intent = new Intent(Close.this, Close.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

        } else if (id == R.id.nav_profile) {
            Intent intent = new Intent(Close.this, Profile.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

        } else if (id == R.id.nav_mydashboard) {
            Intent intent = new Intent(Close.this, Dashboard.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

        } else if (id == R.id.nav_add_contact) {
            Intent intent = new Intent(Close.this, Add_Contact.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        } else if (id == R.id.nav_mytodo) {
            Intent intent = new Intent(Close.this, MyTodoTask.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        } else if (id == R.id.nav_group) {
            Intent intent = new Intent(Close.this, Group.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
        else if (id == R.id.nav_mygroupTask) {
            Intent intent = new Intent(Close.this, MyGroupTask.class);
            startActivity(intent);
            overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);
        }
        else if (id == R.id.nav_mygroups) {
            Intent intent = new Intent(Close.this, MyGroups.class);
            startActivity(intent);
            overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);
        }

        else if (id == R.id.nav_closegrouptask) {
            Intent intent = new Intent(Close.this, GroupCloseTask.class);
            startActivity(intent);
            overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);
        }

        else if (id == R.id.nav_mygrouptodo) {
            Intent intent = new Intent(Close.this, MyGroupTodo.class);
            startActivity(intent);
            overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);
        }





        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private MyTask.ClickListener clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(Context context, final RecyclerView recycleView, final MyTask.ClickListener clicklistener) {

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
