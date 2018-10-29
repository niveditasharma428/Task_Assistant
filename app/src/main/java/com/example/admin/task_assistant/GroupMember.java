package com.example.admin.task_assistant;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.admin.task_assistant.Network.APIClient;
import com.example.admin.task_assistant.adapter.ShowMemberAdapter;
import com.example.admin.task_assistant.model.Member;
import com.example.admin.task_assistant.model.MemberDetails;
import com.example.admin.task_assistant.model.MemberPD;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class GroupMember extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<MemberPD> al;
    ShowMemberAdapter showMemberAdapter;
    ImageView iv, iv_delete;
    SwipeRefreshLayout swipeRefreshLayout;
    SharedPreferences pref;
    String mobile, name, groupName, groupMembers, groupcreatedby,usertyp;
    List<MemberDetails> memberDetailsList;


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

        setContentView(R.layout.activity_group_members);
        pref = getApplication().getSharedPreferences("Options", MODE_PRIVATE);
        mobile = pref.getString("mobile", "");
        name = pref.getString("name", "");
        usertyp = pref.getString("usertyp", "");
        recyclerView = (RecyclerView) findViewById(R.id.recyclemem);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        iv = (ImageView) findViewById(R.id.backArrow);
        al = new ArrayList();

        Intent intent = getIntent();
        groupMembers = intent.getStringExtra("groupmembers");
        groupName = intent.getStringExtra("groupname");
        groupcreatedby = intent.getStringExtra("groupcreatedby");

        try {
            String[] groupMemArray = groupMembers.split(",");
            System.out.println("groupmembers = " + groupMembers);

            for (String item : groupMemArray) {
                al.add(new MemberPD(item));
            }

        } catch (Exception e) {
            System.out.println("GroupMmeberException:-" + e);
        }

        if (!usertyp.equalsIgnoreCase("txtadmin")) {


            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getApplicationContext(), MyGroups.class);
                    startActivity(i);

                }
            });
        }
        else {
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getApplicationContext(), Group.class);
                    startActivity(i);

                }
            });
        }

        showMemberAdapter = new ShowMemberAdapter(GroupMember.this, al, groupName, groupMembers, groupcreatedby);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(showMemberAdapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                refreshList();
            }
        });

    }

    private void refreshList() {

        Call<Member> getGroupMembers = APIClient.getInstance().getGroupMembers(name, groupName);

        getGroupMembers.enqueue(new Callback<Member>() {
            @Override
            public void onResponse(Call<Member> call, Response<Member> response) {
                if (response.isSuccessful()) {
                    Member member = response.body();
                    if (member.getSuccess().equalsIgnoreCase("1")) {
                        swipeRefreshLayout.setRefreshing(false);
                        memberDetailsList = member.getMemberDetails();
                        for (int i = 0; i < memberDetailsList.size(); i++) {
                            groupMembers = memberDetailsList.get(i).getGROUP_MEMBER();
                            System.out.println("DivyaRefreshMember:-" + groupMembers);

                            Toast.makeText(getApplicationContext(), "Group Members Refreshed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Member> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });


    }

    @Override
    public void onBackPressed() {


        Intent i = new Intent(getApplicationContext(), Dashboard.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        startActivity(i);
    }


}
