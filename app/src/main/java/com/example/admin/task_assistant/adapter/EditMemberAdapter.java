package com.example.admin.task_assistant.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.task_assistant.Group;
import com.example.admin.task_assistant.Network.APIClient;
import com.example.admin.task_assistant.R;
import com.example.admin.task_assistant.model.GroupDetails;
import com.example.admin.task_assistant.model.MemberPD;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditMemberAdapter extends RecyclerView.Adapter<EditMemberAdapter.MyViewHolder> {
    Context context;
    List<MemberPD> memberslist;
    ArrayList al, al1;
    String groupName, groupMember;
    Button btn_submit;
    String groupsource, groupcreatedby;

    public EditMemberAdapter(Context context, List<MemberPD> memberslist, String groupName, String groupMember, Button btn_submit, String groupsource, String groupcreatedby) {
        this.context = context;
        this.memberslist = memberslist;
        this.groupName = groupName;
        this.groupMember = groupMember;
        this.al = al;
        this.btn_submit = btn_submit;
        this.groupsource = groupsource;
        this.groupcreatedby = groupcreatedby;
    }

    @Override
    public EditMemberAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_groupadduser, parent, false);
        return new EditMemberAdapter.MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final EditMemberAdapter.MyViewHolder holder, int position) {

        final MemberPD memberDetails = memberslist.get(position);
        al1 = new ArrayList();

        holder.txt_name.setText(memberDetails.getName().substring(0, memberDetails.getName().indexOf('-')));
        holder.txt_mob.setText(memberDetails.getName().replaceAll(".+- ", ""));

        holder.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.checkbox.isChecked()) {

                    if (!al1.contains(holder.txt_name.getText().toString().trim() + " - " + holder.txt_mob.getText().toString().trim())) {
                        al1.add(holder.txt_name.getText().toString().trim() + " - " + holder.txt_mob.getText().toString().trim());
                        System.out.println("TaskArrayEDIT:-" + al1);
                    }


                } else {

                    al1.remove(holder.txt_name.getText().toString().trim() + " - " + holder.txt_mob.getText().toString().trim());
                    System.out.println("TaskArrayRemoveEDIT:-" + al1);

                }


            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String GroupMembers = "";
                for (int i = 0; i < al1.size(); i++) {
                    GroupMembers = GroupMembers + al1.get(i).toString() + ",";
                }
                System.out.println("DelteGroupMmebers:-" + GroupMembers + "-" + groupcreatedby + "-" + groupName);

                if (groupsource.equalsIgnoreCase("delmem")) {
                    Call<GroupDetails> removegroup_mem = APIClient.getInstance().groupmembers_remove(groupName, groupcreatedby, GroupMembers);
                    removegroup_mem.enqueue(new Callback<GroupDetails>() {
                        @Override
                        public void onResponse(Call<GroupDetails> call, Response<GroupDetails> response) {

                            if (response.isSuccessful()) {
                                GroupDetails groupDetails = response.body();

                                if (groupDetails.getSuccess().equalsIgnoreCase("1")) {
                                    Toast.makeText(context, groupDetails.getMessage(), Toast.LENGTH_SHORT).show();
                                    al1.clear();
                                    Intent i = new Intent(context, Group.class);
                                    context.startActivity(i);

                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<GroupDetails> call, Throwable t) {
                            System.out.println("DelteGroupMmebersFail:-" + t);
                            al1.clear();

                        }
                    });
                } else if (groupsource.equalsIgnoreCase("addmem")) {

                    Call<GroupDetails> add_group_members = APIClient.getInstance().groupmembers_adding(groupName, groupcreatedby, GroupMembers);
                    add_group_members.enqueue(new Callback<GroupDetails>() {
                        @Override
                        public void onResponse(Call<GroupDetails> call, Response<GroupDetails> response) {

                            if (response.isSuccessful()) {
                                GroupDetails groupDetails = response.body();

                                if (groupDetails.getSuccess().equalsIgnoreCase("1")) {
                                    Toast.makeText(context, groupDetails.getMessage(), Toast.LENGTH_SHORT).show();
                                    al1.clear();
                                    Intent i = new Intent(context, Group.class);
                                    context.startActivity(i);

                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<GroupDetails> call, Throwable t) {
                            System.out.println("AddGroupMembersFail:-" + t);
                            al1.clear();

                        }
                    });

                }


            }
        });


       /* holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, GroupTaskAssistant.class);
                i.putExtra("groupmember", memberDetails.getName().toString());
                i.putExtra("groupname", groupName);
                i.putExtra("memberlis", groupMember);
                context.startActivity(i);

            }
        });*/

    }

    @Override
    public int getItemCount() {
        return memberslist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_name, txt_mob;
        LinearLayout linearLayout;
        CheckBox checkbox;


        public MyViewHolder(View itemView) {
            super(itemView);

            txt_name = (TextView) itemView.findViewById(R.id.textViewName);
            txt_mob = (TextView) itemView.findViewById(R.id.textViewContact);
            checkbox = (CheckBox) itemView.findViewById(R.id.checkbox);

        }
    }
}
