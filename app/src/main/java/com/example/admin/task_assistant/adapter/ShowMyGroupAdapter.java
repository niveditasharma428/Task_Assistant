package com.example.admin.task_assistant.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.task_assistant.GroupMember;
import com.example.admin.task_assistant.Network.APIClient;
import com.example.admin.task_assistant.R;
import com.example.admin.task_assistant.model.MyGroupMemberDetails;
import com.example.admin.task_assistant.model.MyGroupMembers;
import com.example.admin.task_assistant.model.MyGroupMembersList;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowMyGroupAdapter extends RecyclerView.Adapter<ShowMyGroupAdapter.MyViewHolder> {
    Context context;
    List<MyGroupMemberDetails> myGroupMemberDetails;
    SharedPreferences pref;
    String mobile, name;
    ShowGroupAdapter showGroupAdapter;
    List<MyGroupMembersList> myGroupMembersLists;

    public ShowMyGroupAdapter(Context context, List<MyGroupMemberDetails> myGroupMemberDetails) {
        this.context = context;
        this.myGroupMemberDetails = myGroupMemberDetails;

    }

    @Override
    public ShowMyGroupAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclemyshowgroup, parent, false);
        pref = context.getSharedPreferences("Options", context.MODE_PRIVATE);
        mobile = pref.getString("mobile", "");
        name = pref.getString("name", "");
        return new ShowMyGroupAdapter.MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final MyGroupMemberDetails groupMembers = myGroupMemberDetails.get(position);
        holder.txt_groupName.setText(groupMembers.getGROUP_NAME());
        holder.txt_date.setText(groupMembers.getCREATED_DATE());
        holder.txt_createdby.setText("Created By:-" + groupMembers.getCREATED_BY());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Call<MyGroupMembers> mygroupmembers = APIClient.getInstance().mygroupmembers(groupMembers.getGROUP_NAME(), groupMembers.getCREATED_BY());

                mygroupmembers.enqueue(new Callback<MyGroupMembers>() {
                    @Override
                    public void onResponse(Call<MyGroupMembers> call, Response<MyGroupMembers> response) {
                        MyGroupMembers myGroupMembers = response.body();
                        if (myGroupMembers.getSuccess().equalsIgnoreCase("1")) {
                            myGroupMembersLists = myGroupMembers.getMyGroupMembersLists();

                            if (myGroupMembersLists.size() > 0)
                            {
                                String groupmembers = "";
                            for (int i = 0; i < myGroupMembersLists.size(); i++) {
                                String name = myGroupMembersLists.get(i).getGROUP_MEMBER_NAME();
                                String mobile = myGroupMembersLists.get(i).getGROUP_MEMBER_MOBILE();
                                groupmembers = groupmembers + name + " - " + mobile + ",";

                            }
                            System.out.println("DivyaGroupMyMembers:-" + groupmembers);

                            Intent i = new Intent(context, GroupMember.class);
                            i.putExtra("groupname", groupMembers.getGROUP_NAME());
                            i.putExtra("groupmembers", groupmembers);
                            i.putExtra("groupcreatedby", groupMembers.getCREATED_BY());
                            context.startActivity(i);
                        }
                        else
                            {
                                Toast.makeText(context,"Something Went Wrong",Toast.LENGTH_SHORT).show();
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<MyGroupMembers> call, Throwable t) {

                    }
                });


            }
        });


    }


    @Override
    public int getItemCount() {
        return myGroupMemberDetails.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_groupName, txt_date, txt_createdby;
        LinearLayout linearLayout;


        public MyViewHolder(View itemView) {
            super(itemView);

            txt_groupName = (TextView) itemView.findViewById(R.id.textViewName);
            txt_date = (TextView) itemView.findViewById(R.id.textViewDate);
            txt_createdby = (TextView) itemView.findViewById(R.id.textViewCreatedBy);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.layout1);

        }
    }
}
