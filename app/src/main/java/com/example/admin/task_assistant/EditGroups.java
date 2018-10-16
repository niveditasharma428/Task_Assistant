package com.example.admin.task_assistant;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.task_assistant.adapter.EditMemberAdapter;
import com.example.admin.task_assistant.model.MemberPD;

import java.util.ArrayList;

public class EditGroups extends AppCompatActivity {
    ArrayList al;
    EditMemberAdapter editMemberAdapter;
    RecyclerView recyclerView;
    TextView txttile;
    Button btn_submit;
    ImageView backarrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_groups);

        recyclerView = (RecyclerView) findViewById(R.id.recyclemem);
        txttile = (TextView) findViewById(R.id.txttitle);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        backarrow = (ImageView) findViewById(R.id.backArrow);

        Intent i = getIntent();
        String groupname = i.getStringExtra("groupname");
        String groupmembers = i.getStringExtra("groupmembers");
        String groupsource = i.getStringExtra("groupsource");
        String groupcreatedby = i.getStringExtra("groupcreatedby");


        if (groupsource.equalsIgnoreCase("addmem")) {
            txttile.setText("Add Members");
            btn_submit.setText("ADD");
        } else if (groupsource.equalsIgnoreCase("delmem")) {
            txttile.setText("Delete Members");
            btn_submit.setText("DELETE");
        }

        al = new ArrayList();

        try {
            String[] groupMemArray = groupmembers.split(",");
            System.out.println("groupmembers = " + groupmembers);

            for (String item : groupMemArray) {
                al.add(new MemberPD(item));
            }

        } catch (Exception e) {
            System.out.println("GroupMmeberException:-" + e);
        }


        editMemberAdapter = new EditMemberAdapter(EditGroups.this, al, groupname, groupmembers, btn_submit, groupsource, groupcreatedby);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(editMemberAdapter);

        backarrow.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View view) {
                                             Intent i = new Intent(getApplicationContext(), Group.class);
                                             getApplication().startActivity(i);
                                         }
                                     }
        );


    }
}
