package com.example.admin.task_assistant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Admin_User extends AppCompatActivity {

    Button user,admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__user);

        user= (Button) findViewById(R.id.user);
        admin= (Button) findViewById(R.id.admin);

        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Admin_User.this,User_code.class);
               // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);
                overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Admin_User.this, Admin.class);
                startActivity(intent);
                overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });


    }


}
