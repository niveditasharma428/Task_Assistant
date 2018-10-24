package com.example.admin.task_assistant.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.admin.task_assistant.GroupTaskAssistant;
import com.example.admin.task_assistant.R;
import com.example.admin.task_assistant.model.MemberPD;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShowMemberAdapter extends RecyclerView.Adapter<ShowMemberAdapter.MyViewHolder> {
    Context context;
    List<MemberPD> memberslist;
    ArrayList al;
    String groupName, groupMember, name,groupcreatedby,usertyp;
    SharedPreferences pref;


    public ShowMemberAdapter(Context context, List<MemberPD> memberslist, String groupName, String groupMember, String groupcreatedby) {
        this.context = context;
        this.memberslist = memberslist;
        this.groupName = groupName;
        this.groupMember = groupMember;
        this.groupcreatedby=groupcreatedby;
        this.al = al;
    }

    @Override
    public ShowMemberAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleshowmembers, parent, false);
        pref = context.getSharedPreferences("Options", context.MODE_PRIVATE);
        name = pref.getString("name", "");
        usertyp = pref.getString("usertyp", "");

        return new ShowMemberAdapter.MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final ShowMemberAdapter.MyViewHolder holder, int position) {

        final MemberPD memberDetails = memberslist.get(position);

        System.out.println("DivyaMemberNAME:-"+memberDetails.getName());

        holder.iv_delete.setVisibility(View.GONE);
        holder.txt_groupName.setText(groupName);
        holder.txt_name.setText(memberDetails.getName().substring(0, memberDetails.getName().indexOf('-')));
        holder.txt_mob.setText(memberDetails.getName().replaceAll(".+- ", ""));

        System.out.println("GroupCreatedBy:-"+groupcreatedby);

        if(groupcreatedby.equalsIgnoreCase(memberDetails.getName().substring(0, memberDetails.getName().indexOf('-')).trim()))
        {
            holder.textadminName.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.textadminName.setVisibility(View.GONE);
        }

       /* if (usertyp.equalsIgnoreCase("txtadmin")) {*/

            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("textviewname:-" + holder.txt_name.getText().toString() + name);

                    String txtname = holder.txt_name.getText().toString().trim();

                    if (!name.equalsIgnoreCase(txtname)) {

                        Intent i = new Intent(context, GroupTaskAssistant.class);
                        i.putExtra("groupmember", memberDetails.getName().toString());
                        i.putExtra("groupname", groupName);
                        i.putExtra("memberlis", groupMember);
                        i.putExtra("groupcreatedby", groupcreatedby);

                        context.startActivity(i);
                    }


                }
            });
        //}

    }


    @Override
    public int getItemCount() {
        return memberslist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_groupName, txt_name, txt_mob,textadminName;
        LinearLayout linearLayout;
        CircleImageView img;
        ImageView iv_delete;


        public MyViewHolder(View itemView) {
            super(itemView);

            txt_name = (TextView) itemView.findViewById(R.id.textViewName);
            textadminName = (TextView) itemView.findViewById(R.id.textadminName);
            txt_groupName = (TextView) itemView.findViewById(R.id.textViewDate);
            txt_mob = (TextView) itemView.findViewById(R.id.textViewmob);
            img=(CircleImageView)itemView.findViewById(R.id.image) ;
            linearLayout = (LinearLayout) itemView.findViewById(R.id.layout1);
            iv_delete = (ImageView) itemView.findViewById(R.id.iv_delete);


        }
    }
}
