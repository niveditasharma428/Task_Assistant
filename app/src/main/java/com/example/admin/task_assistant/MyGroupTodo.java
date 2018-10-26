
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
        import com.example.admin.task_assistant.Network.APIClient;
        import com.example.admin.task_assistant.model.MyTodo;
        import com.example.admin.task_assistant.model.MyTodoDetails;
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

public class MyGroupTodo extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar toolbar;

    List<Contact4> ListOfcontact4 = new ArrayList<>();
    RecyclerView recyclerView1, recyclerView2;
    RecyclerView.LayoutManager layoutManagerOfrecyclerView;

    RecyclerViewAdapter4 recyclerViewadapter4;
    RecyclerViewAdapter5 recyclerViewadapter5;
    TextView name1, email1, t3;
    CircleImageView profile;
    String mobile, TASK_ID, name, email, CREATED_BY, Seen, usertyp;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    ArrayList<MyTodoDetails> aluser1 = new ArrayList();
    ArrayList<MyTodoDetails> aluser2 = new ArrayList();

    LinearLayout layout1;

    private static String MY_TODO_TASK_URL = "https://orgone.solutions/task/mytask_count.php";
    private static String UPDATE_STATUS_URL = "https://orgone.solutions/task/tasks_seen.php";
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

        setContentView(R.layout.activity_my_group_todo);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_white_24dp);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        View view = navigationView.getHeaderView(0);
        name1 = (TextView) view.findViewById(R.id.name);
        email1 = (TextView) view.findViewById(R.id.mailid);
        profile=(CircleImageView)view.findViewById(R.id.imageView);
        t3 = (TextView) findViewById(R.id.count);

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        pref = getApplication().getSharedPreferences("Options", MODE_PRIVATE);
        name = pref.getString("name", "");
        email = pref.getString("email", "");
        mobile = pref.getString("mobile", "");

        ListOfcontact4 = new ArrayList<>();
        recyclerView2 = (RecyclerView) findViewById(R.id.my_recycler_view5);

        contactData();



        recyclerView2.addOnItemTouchListener(new MyTask.RecyclerTouchListener(this,
                recyclerView2, new MyTask.ClickListener() {
            @Override
            public void onClick(View view, final int position) {

                update(aluser2.get(position).getCreatedBY());


            }

            @Override
            public void onLongClick(View view, int position) {
                Toast.makeText(MyGroupTodo.this, "Long press on position :" + position,
                        Toast.LENGTH_LONG).show();
            }
        }));

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
            Toast.makeText(MyGroupTodo.this, "error", Toast.LENGTH_LONG).show();

        }

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
                    Toast.makeText(MyGroupTodo.this, response, Toast.LENGTH_LONG).show();
                }


            }

        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(MyGroupTodo.this, error.toString(), Toast.LENGTH_LONG).show();

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

        RequestQueue requestQueue = Volley.newRequestQueue(MyGroupTodo.this);
        requestQueue.add(stringRequest);

    }

    public static interface ClickListener {
        public void onClick(View view, int position);

        public void onLongClick(View view, int position);
    }

    private void update(final String created_by) {

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
                                String task_seen = jsonObjectInfo.getString("TASK_SEEN");


                                // Intent intent = new Intent(MyTodoTask.this, MyTask.class);
                                // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                // intent.putExtra("TASK_ID",TASK_ID);
                                // intent.putExtra("CREATED_BY",CREATED_BY);
                                // startActivity(intent);
                                //  overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);
                                //  finish();


                            } else {
                                //Displaying a toast if the otp entered is wrong
                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(MyGroupTodo.this, MyTask.class);
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

                        Toast.makeText(MyGroupTodo.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                //Adding the parameters otp and username

                params.put(Config.KEY_CREATED_BY, created_by);
                params.put(Config.KEY_MOBILE, mobile);

                return params;
            }
        };

        //Adding the request to the queue
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        requestQueue.add(postRequest);

    }

    ProgressDialog progressDialog;

    public void contactData() {

        progressDialog = new ProgressDialog(MyGroupTodo.this);
        progressDialog.setMessage("Loading, please wait...");
        progressDialog.show();

        Call<MyTodo> mytask_count = APIClient.getInstance().mytask_count(mobile);

        mytask_count.enqueue(new Callback<MyTodo>() {
            @Override
            public void onResponse(Call<MyTodo> call, retrofit2.Response<MyTodo> response) {

                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                    MyTodo myTodo = response.body();
                    List<MyTodoDetails> myTodoDetailsUser2 = new ArrayList<>();

                    if (myTodo.getSuccess().equalsIgnoreCase("1")) {
                        myTodoDetailsUser2 = myTodo.getMyTodoDetailsListUser2();


                        for (int i = 0; i < myTodoDetailsUser2.size(); i++) {

                            String task_group = myTodoDetailsUser2.get(i).getTaskGroup();
                            String created_by = myTodoDetailsUser2.get(i).getCreatedBY();
                            String no_oftask = myTodoDetailsUser2.get(i).getNoOfTask();
                            String image=myTodoDetailsUser2.get(i).getImage();

                            aluser2.add(new MyTodoDetails(created_by, no_oftask, task_group,image));

                            System.out.println("DivyaTodoTask:-" + task_group + created_by + no_oftask);

                        }
                        recyclerViewadapter5 = new RecyclerViewAdapter5(aluser2, getApplicationContext());

                        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                        LinearLayoutManager mLayoutManager1 = new LinearLayoutManager(getApplicationContext());

                        recyclerView2.setLayoutManager(mLayoutManager1);
                        recyclerView2.setAdapter(recyclerViewadapter5);

                    }
                }
            }

            @Override
            public void onFailure(Call<MyTodo> call, Throwable t) {

            }
        });


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
            Intent intent = new Intent(MyGroupTodo.this, MyContact.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

        } else if (id == R.id.nav_assigntask) {
            Intent intent = new Intent(MyGroupTodo.this, TaskAssign.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

        } else if (id == R.id.nav_newtask) {
            Intent intent = new Intent(MyGroupTodo.this, NewTask.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

        } else if (id == R.id.nav_closetask) {
            Intent intent = new Intent(MyGroupTodo.this, Close.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

        } else if (id == R.id.nav_profile) {
            Intent intent = new Intent(MyGroupTodo.this, Profile.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

        } else if (id == R.id.nav_mydashboard) {
            Intent intent = new Intent(MyGroupTodo.this, Dashboard.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

        } else if (id == R.id.nav_add_contact) {
            Intent intent = new Intent(MyGroupTodo.this, Add_Contact.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        } else if (id == R.id.nav_mytodo) {
            Intent intent = new Intent(MyGroupTodo.this, MyTodoTask.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        } else if (id == R.id.nav_group) {
            Intent intent = new Intent(MyGroupTodo.this, Group.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
        else if (id == R.id.nav_group) {
            Intent intent = new Intent(MyGroupTodo.this, Group.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);
        }
        else if (id == R.id.nav_mygroupTask) {
            Intent intent = new Intent(MyGroupTodo.this, MyGroupTask.class);
            startActivity(intent);
            overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);
        }
        else if (id == R.id.nav_mygroups) {
            Intent intent = new Intent(MyGroupTodo.this, MyGroups.class);
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

