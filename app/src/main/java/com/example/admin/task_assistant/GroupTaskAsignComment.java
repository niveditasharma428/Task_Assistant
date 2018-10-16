package com.example.admin.task_assistant;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.task_assistant.Network.APIClient;
import com.example.admin.task_assistant.model.GroupDetails;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class GroupTaskAsignComment extends AppCompatActivity {

    String taskid, taskdesc, txtstat, txtgrpname, txtgrpmem, txtal;
    TextView txt_des, txt_stat;
    Button btn_reassign, btn_close;


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

        setContentView(R.layout.activity_task_assign_comment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_white_24dp);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent i = getIntent();
        taskid = i.getStringExtra("TASK_ID");
        taskdesc = i.getStringExtra("desc");
        txtstat = i.getStringExtra("status");
        txtgrpname = i.getStringExtra("groupname");
        txtgrpmem = i.getStringExtra("groupmember");
        txtal = i.getStringExtra("al");

        txt_des = (TextView) findViewById(R.id.etDes);
        txt_stat = (TextView) findViewById(R.id.status);
        btn_reassign = (Button) findViewById(R.id.reassign);
        btn_close = (Button) findViewById(R.id.close);

        txt_des.setText(taskdesc);
        txt_stat.setText(txtstat);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(GroupTaskAsignComment.this, GroupTaskAssistant.class);
                i.putExtra("groupmember", txtgrpmem);
                i.putExtra("groupname", txtgrpname);
                i.putExtra("memberlis", txtal);
                startActivity(i);


            }
        });


        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Call<GroupDetails> closegrouptask = APIClient.getInstance().grouptaskclose(taskid);
                closegrouptask.enqueue(new Callback<GroupDetails>() {
                    @Override
                    public void onResponse(Call<GroupDetails> call, Response<GroupDetails> response) {

                        if (response.isSuccessful()) {
                            GroupDetails groupDetails = response.body();

                            if (groupDetails.getSuccess().equalsIgnoreCase("1")) {
                                Toast.makeText(getApplicationContext(), groupDetails.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<GroupDetails> call, Throwable t) {
                        System.out.println("DivyaGroupClose:-" + t);
                    }
                });

                Intent i = new Intent(GroupTaskAsignComment.this, GroupTaskAssistant.class);
                i.putExtra("groupmember", txtgrpmem);
                i.putExtra("groupname", txtgrpname);
                i.putExtra("memberlis", txtal);
                startActivity(i);
            }
        });

        btn_reassign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater li = LayoutInflater.from(GroupTaskAsignComment.this);

                View confirmDialog = li.inflate(R.layout.dialog_taskassign_comment, null);

                Button send = (AppCompatButton) confirmDialog.findViewById(R.id.buttonSend);
                final EditText myComment = (EditText) confirmDialog.findViewById(R.id.editTextComment);

                //Creating an alertdialog builder
                AlertDialog.Builder alert = new AlertDialog.Builder(GroupTaskAsignComment.this);

                //Adding our dialog box to the view of alert dialog
                alert.setView(confirmDialog);

                //Creating an alert dialog
                final AlertDialog alertDialog = alert.create();

                //Displaying the alert dialog
                alertDialog.show();

                send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (TextUtils.isEmpty(myComment.getText().toString())) {
                            myComment.setError("Please Enter Comment");
                        } else {

                            Call<GroupDetails> groupTaskReAssignView = APIClient.getInstance().groupTaskReAssignView(taskid, myComment.getText().toString());

                            groupTaskReAssignView.enqueue(new Callback<GroupDetails>() {
                                @Override
                                public void onResponse(Call<GroupDetails> call, Response<GroupDetails> response) {

                                    GroupDetails groupDetails = response.body();

                                    if (response.isSuccessful()) {
                                        if (groupDetails.getSuccess().equalsIgnoreCase("1")) {
                                            Toast.makeText(getApplicationContext(), "Task Reassigned Successfully", Toast.LENGTH_SHORT).show();
                                            alertDialog.dismiss();
                                            Intent i = new Intent(GroupTaskAsignComment.this, GroupTaskAssistant.class);
                                            i.putExtra("groupmember", txtgrpmem);
                                            i.putExtra("groupname", txtgrpname);
                                            i.putExtra("memberlis", txtal);
                                            startActivity(i);
                                        }

                                    } else {
                                        Toast.makeText(getApplicationContext(), "Task Not Reassigned", Toast.LENGTH_SHORT).show();
                                        alertDialog.dismiss();
                                    }
                                }

                                @Override
                                public void onFailure(Call<GroupDetails> call, Throwable t) {

                                    System.out.println("DivyaGroupReassign:-" + t);

                                }
                            });


                        }
                    }
                });
            }
        });


    }
}

