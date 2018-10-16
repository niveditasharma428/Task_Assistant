package com.example.admin.task_assistant.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.admin.task_assistant.GroupTaskAsignComment;
import com.example.admin.task_assistant.R;
import com.example.admin.task_assistant.model.GroupTaskDetails;

import java.util.List;

public class GroupViewTaskAdapter extends RecyclerView.Adapter<GroupViewTaskAdapter.MyViewHolder> {
    Context context;
    List<GroupTaskDetails> groupTaskDetailsList;
    String al, groupmember, groupname;


    public GroupViewTaskAdapter(Context context, List<GroupTaskDetails> groupTaskDetailsList, String al, String groupmember, String groupname) {
        this.context = context;
        this.groupTaskDetailsList = groupTaskDetailsList;
        this.al = al;
        this.groupmember = groupmember;
        this.groupname = groupname;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleshowgrouptask, parent, false);
        return new GroupViewTaskAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final GroupTaskDetails groupTaskDetails = groupTaskDetailsList.get(position);
        holder.task_assign.setText(groupTaskDetails.getTASK_ASSIGN().toString());
        holder.taskdesc.setText(groupTaskDetails.getTASK_DES().toString());
        holder.priority.setText(groupTaskDetails.getTASK_PRIORITY().toString());
        holder.status.setText(groupTaskDetails.getTASK_STATUS().toString());
        holder.task_group.setText(groupTaskDetails.getTASK_GROUP().toString());
        holder.task_comment.setText(groupTaskDetails.getTASK_COMMENT().toString());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(view.getContext(), GroupTaskAsignComment.class);
                i.putExtra("TASK_ID", groupTaskDetails.getTASK_ID());
                i.putExtra("desc", groupTaskDetails.getTASK_DES());
                i.putExtra("status", groupTaskDetails.getTASK_STATUS());
                i.putExtra("al", al);
                i.putExtra("groupmember", groupmember);
                i.putExtra("groupname", groupname);

                view.getContext().startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return groupTaskDetailsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView task_assign, taskdesc, priority, status, task_group,task_comment;
        LinearLayout linearLayout;

        public MyViewHolder(View itemView) {
            super(itemView);

            task_assign = (TextView) itemView.findViewById(R.id.task_assign);
            taskdesc = (TextView) itemView.findViewById(R.id.taskdesc);
            priority = (TextView) itemView.findViewById(R.id.priority);
            status = (TextView) itemView.findViewById(R.id.status);
            task_group = (TextView) itemView.findViewById(R.id.task_group);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.layout_item_desc1);
            task_comment=(TextView)itemView.findViewById(R.id.comment);


        }
    }
}
