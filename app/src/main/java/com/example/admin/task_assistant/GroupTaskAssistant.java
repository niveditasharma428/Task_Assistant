package com.example.admin.task_assistant;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.example.admin.task_assistant.adapter.GroupTaskAdapter;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class GroupTaskAssistant extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    GroupTaskAdapter groupTaskAdapter;
    ImageView iv_back;
    SharedPreferences pref;
    String al, groupname,groupcreatedby,usertyp;

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

        setContentView(R.layout.activity_group_task_assistant);

        Intent i = getIntent();
        final String groupmembers = i.getStringExtra("groupmember");
        groupname = i.getStringExtra("groupname");
        groupcreatedby= i.getStringExtra("groupcreatedby");
        al = i.getStringExtra("memberlis");

        System.out.println("DivyaGpMem:-" + groupmembers + "--" + groupname + al);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        iv_back = (ImageView) findViewById(R.id.backArrow);
        groupTaskAdapter = new GroupTaskAdapter(GroupTaskAssistant.this, getSupportFragmentManager(), "task", groupmembers, groupname, al);
        viewPager.setAdapter(groupTaskAdapter);
        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.setTabTextColors(
                getResources().getColor(R.color.black),
                getResources().getColor(R.color.white)
        );

        pref = getApplication().getSharedPreferences("Options", MODE_PRIVATE);
        usertyp = pref.getString("usertyp", "");


        if (!usertyp.equalsIgnoreCase("txtadmin")) {


            iv_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getApplicationContext(), MyGroups.class);
                    startActivity(i);

                }
            });
        }
        else {
            iv_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getApplicationContext(), Group.class);
                    startActivity(i);

                }
            });
        }

        /* iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Group.class);
               // i.putExtra("groupmembers", al);
               // i.putExtra("groupname", groupname);
               // i.putExtra("groupcreatedby",groupcreatedby);
                startActivity(i);
            }
        });
*/

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), Group.class);
        /*i.putExtra("groupmembers", al);
        i.putExtra("groupname", groupname);*/
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        startActivity(i);
    }
}
