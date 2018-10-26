package com.example.admin.task_assistant.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.task_assistant.EditGroups;
import com.example.admin.task_assistant.GroupMember;
import com.example.admin.task_assistant.Network.APIClient;
import com.example.admin.task_assistant.R;
import com.example.admin.task_assistant.model.GroupDetails;
import com.example.admin.task_assistant.model.Member;
import com.example.admin.task_assistant.model.MemberDetails;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowGroupAdapter extends RecyclerView.Adapter<ShowGroupAdapter.MyViewHolder> {
    Context context;
    List<MemberDetails> groupMembersList;
    SharedPreferences pref;
    String mobile, name, admin_name, admin_mob;
    ShowGroupAdapter showGroupAdapter;

    public ShowGroupAdapter(Context context, List<MemberDetails> groupMembersList) {
        this.context = context;
        this.groupMembersList = groupMembersList;

    }

    @Override
    public ShowGroupAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleshowgroup, parent, false);
        pref = context.getSharedPreferences("Options", context.MODE_PRIVATE);
        mobile = pref.getString("mobile", "");
        name = pref.getString("name", "");
        admin_name = pref.getString("admin_name", "");
        admin_mob = pref.getString("admin_mob", "");

        return new ShowGroupAdapter.MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final ShowGroupAdapter.MyViewHolder holder, int position) {

        final MemberDetails groupMembers = groupMembersList.get(position);

        holder.txt_groupName.setText(groupMembers.getGROUP_NAME());
        holder.txt_date.setText(groupMembers.getCREATED_DATE());
        holder.txt_createdby.setText("Created By:-" + groupMembers.getCREATED_BY());
        System.out.println("DivyaAdmin:-" + admin_name);

        holder.iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu popupMenu = new PopupMenu(context, holder.iv_edit);
                popupMenu.getMenuInflater().inflate(R.menu.menu_main, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.addmem:
                                Call<Member> membertoadd = APIClient.getInstance().memb_for_adding(groupMembers.getGROUP_NAME(), name);
                                membertoadd.enqueue(new Callback<Member>() {
                                    @Override
                                    public void onResponse(Call<Member> call, Response<Member> response) {
                                        if (response.isSuccessful()) {
                                            groupMembersList = response.body().getMemberDetails();
                                            String groupMember = "";

                                            if (groupMembersList.size() > 0) {
                                                for (int i = 0; i < groupMembersList.size(); i++) {
                                                    //  groupMember = groupMember + al.get(i).toString() + ",";
                                                    groupMember = groupMember + groupMembersList.get(i).getGROUP_MEMBER() + ",";

                                                    System.out.println("Divyaddgroupmem:-" + groupMember);

                                                }

                                                Intent irem = new Intent(context, EditGroups.class);
                                                irem.putExtra("groupname", holder.txt_groupName.getText().toString());
                                                irem.putExtra("groupmembers", groupMember);
                                                irem.putExtra("groupsource", "addmem");
                                                irem.putExtra("groupcreatedby", groupMembers.getCREATED_BY());

                                                context.startActivity(irem);
                                            } else {
                                                Toast.makeText(context, "No Members to Add..Please Create Group!!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Member> call, Throwable t) {

                                    }
                                });


                                return true;
                            case R.id.delmem:
                                Call<Member> memberforremove = APIClient.getInstance().memb_for_remove(name, groupMembers.getGROUP_NAME(), admin_name);
                                memberforremove.enqueue(new Callback<Member>() {
                                    @Override
                                    public void onResponse(Call<Member> call, Response<Member> response) {
                                        if (response.isSuccessful()) {
                                            groupMembersList = response.body().getMemberDetails();
                                            String groupMember = "";

                                            if (groupMembersList.size() > 0) {
                                                for (int i = 0; i < groupMembersList.size(); i++) {
                                                    //  groupMember = groupMember + al.get(i).toString() + ",";
                                                    groupMember = groupMember + groupMembersList.get(i).getGROUP_MEMBER() + ",";

                                                    System.out.println("DivyadELgROUPmMEMBERS:-" + groupMember);

                                                }

                                                Intent irem = new Intent(context, EditGroups.class);
                                                irem.putExtra("groupname", holder.txt_groupName.getText().toString());
                                                irem.putExtra("groupmembers", groupMember);
                                                irem.putExtra("groupsource", "delmem");
                                                irem.putExtra("groupcreatedby", groupMembers.getCREATED_BY());

                                                context.startActivity(irem);
                                            } else {
                                                Toast.makeText(context, "No Members to Delete..Please Add Members!!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Member> call, Throwable t) {

                                    }
                                });


                                return true;
                        }

                        return false;
                    }
                });
                popupMenu.show();
            }
        });

        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Call<GroupDetails> deletGroup = APIClient.getInstance().deleteGroup(name, holder.txt_groupName.getText().toString());

                deletGroup.enqueue(new Callback<GroupDetails>() {
                    @Override
                    public void onResponse(Call<GroupDetails> call, Response<GroupDetails> response) {

                        if (response.isSuccessful()) {

                            GroupDetails groupDetails = response.body();

                            Toast.makeText(context, groupDetails.getMessage(), Toast.LENGTH_SHORT).show();
                            groupMembersList.remove(holder.getAdapterPosition());
                            notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<GroupDetails> call, Throwable t) {

                        System.out.println("GroupDeleteErro:-" + t);
                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(context, GroupMember.class);
                i.putExtra("groupname", groupMembers.getGROUP_NAME());
                i.putExtra("groupmembers", groupMembers.getGROUP_MEMBER());
                i.putExtra("groupcreatedby", groupMembers.getCREATED_BY());
                context.startActivity(i);

            }
        });

    }

    @Override
    public int getItemCount() {
        return groupMembersList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_groupName, txt_date, txt_createdby;
        LinearLayout linearLayout;
        ImageView iv_delete, iv_edit;


        public MyViewHolder(View itemView) {
            super(itemView);

            txt_groupName = (TextView) itemView.findViewById(R.id.textViewName);
            txt_date = (TextView) itemView.findViewById(R.id.textViewDate);
            txt_createdby = (TextView) itemView.findViewById(R.id.textViewCreatedBy);
            iv_delete = (ImageView) itemView.findViewById(R.id.iv_delete);
            iv_edit = (ImageView) itemView.findViewById(R.id.iv_edit);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.layout1);

        }
    }
}
