
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
        import android.widget.LinearLayout;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.example.admin.task_assistant.Network.APIClient;
        import com.example.admin.task_assistant.model.CloseData;
        import com.example.admin.task_assistant.model.CloseTaskDetails;
        import java.util.ArrayList;
        import java.util.List;
        import retrofit2.Call;
        import retrofit2.Callback;
        import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
        import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class GroupCloseTask extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar toolbar;

    List<Contact4> ListOfcontact4 = new ArrayList<>();
    RecyclerView recyclerView6, recyclerView7;
    RecyclerView.LayoutManager layoutManagerOfrecyclerView;

    RecyclerViewAdapter6 recyclerViewadapter6;
    RecyclerViewAdapter7 recyclerViewadapter7;
    TextView name1, email1, t3;
    String mobile, TASK_ID, name, email, CREATED_BY, Seen, usertyp;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    ArrayList<CloseTaskDetails> aluser1 = new ArrayList();
    ArrayList<CloseTaskDetails> aluser2 = new ArrayList();

    TextView txt1, txt2;

    LinearLayout layout1;


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

        setContentView(R.layout.activity_group_close_task);
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
        t3 = (TextView) findViewById(R.id.count);

      /*  Bundle bundle=getIntent().getExtras();
        name1.setText(bundle.getString("name", String.valueOf(bundle)));
        email1.setText(bundle.getString("email", String.valueOf(bundle)));*/
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        pref = getApplication().getSharedPreferences("Options", MODE_PRIVATE);
        name = pref.getString("name", "");
        email = pref.getString("email", "");
        mobile = pref.getString("mobile", "");
       name1.setText(name);
        email1.setText(email);

        ListOfcontact4 = new ArrayList<>();
       // recyclerView6 = (RecyclerView) findViewById(R.id.my_recycler_view4);
        recyclerView7 = (RecyclerView) findViewById(R.id.my_recycler_view5);
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
            Toast.makeText(GroupCloseTask.this, "error", Toast.LENGTH_LONG).show();

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
                            String task_assign = closeTaskDetails1.get(i).getTASK_ASSIGN();
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

                     //   recyclerViewadapter6 = new RecyclerViewAdapter6(aluser1, getApplicationContext());
                        recyclerViewadapter7 = new RecyclerViewAdapter7(aluser2, getApplicationContext());

                        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                        LinearLayoutManager mLayoutManager1 = new LinearLayoutManager(getApplicationContext());

                       // recyclerView6.setLayoutManager(mLayoutManager);
                       // recyclerView6.setAdapter(recyclerViewadapter6);
                        recyclerView7.setLayoutManager(mLayoutManager1);
                        recyclerView7.setAdapter(recyclerViewadapter7);

                    }
                }

            }

            @Override
            public void onFailure(Call<CloseData> call, Throwable t) {

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
            Intent intent = new Intent(GroupCloseTask.this, MyContact.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

        } else if (id == R.id.nav_assigntask) {
            Intent intent = new Intent(GroupCloseTask.this, TaskAssign.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

        } else if (id == R.id.nav_newtask) {
            Intent intent = new Intent(GroupCloseTask.this, NewTask.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

        } else if (id == R.id.nav_closetask) {
            Intent intent = new Intent(GroupCloseTask.this, Close.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

        } else if (id == R.id.nav_profile) {
            Intent intent = new Intent(GroupCloseTask.this, Profile.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

        } else if (id == R.id.nav_mydashboard) {
            Intent intent = new Intent(GroupCloseTask.this, Dashboard.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

        } else if (id == R.id.nav_add_contact) {
            Intent intent = new Intent(GroupCloseTask.this, Add_Contact.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        } else if (id == R.id.nav_mytodo) {
            Intent intent = new Intent(GroupCloseTask.this, MyTodoTask.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        } else if (id == R.id.nav_group) {
            Intent intent = new Intent(GroupCloseTask.this, Group.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
        else if (id == R.id.nav_mygroupTask) {
            Intent intent = new Intent(GroupCloseTask.this, MyGroupTask.class);
            startActivity(intent);
            overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);
        }
        else if (id == R.id.nav_mygroups) {
            Intent intent = new Intent(GroupCloseTask.this, MyGroups.class);
            startActivity(intent);
            overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);
        }

        else if (id == R.id.nav_closetask) {
            Intent intent = new Intent(GroupCloseTask.this, Close.class);
            startActivity(intent);
            overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);
        }

        else if (id == R.id.nav_mygrouptodo) {
            Intent intent = new Intent(GroupCloseTask.this, MyGroupTodo.class);
            startActivity(intent);
            overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);
        }
        else if (id == R.id.nav_closegrouptask) {
            Intent intent = new Intent(GroupCloseTask.this, GroupCloseTask.class);
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

