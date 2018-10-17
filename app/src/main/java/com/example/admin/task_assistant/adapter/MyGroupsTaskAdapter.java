package com.example.admin.task_assistant.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.admin.task_assistant.R;
import com.example.admin.task_assistant.model.GroupTaskDetails;

import java.util.List;

public class MyGroupsTaskAdapter extends RecyclerView.Adapter<MyGroupsTaskAdapter.MyViewHolder> {
    Context context;
    List<GroupTaskDetails> groupTaskDetailsList;

    public MyGroupsTaskAdapter(Context context, List<GroupTaskDetails> groupTaskDetailsList) {
        this.context = context;
        this.groupTaskDetailsList = groupTaskDetailsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item1, parent, false);
        return new MyGroupsTaskAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final GroupTaskDetails groupTaskDetails = groupTaskDetailsList.get(position);

        holder.t1.setText(groupTaskDetails.getTASK_DES());
        //holder.t2.setText(groupTaskDetails.getTASK_PRIORITY());
        holder.t3.setText(groupTaskDetails.getTASK_COMMENT());
      //  holder.t6.setText(groupTaskDetails.getTASK_STATUS());
        holder.t4.setText(groupTaskDetails.getTASK_ASSIGN());


        if(groupTaskDetails.getTASK_PRIORITY().equalsIgnoreCase("High")){
            holder.high.setVisibility(View.VISIBLE);
        } else {
            holder.low.setVisibility(View.VISIBLE);
        }

        if(groupTaskDetails.getTASK_STATUS().equalsIgnoreCase("Active")){
            holder.active_task.setVisibility(View.VISIBLE);
        } else if(groupTaskDetails.getTASK_STATUS().equalsIgnoreCase("In process"))
        {
            holder.in_process_task.setVisibility(View.VISIBLE);
        }
        else {
            holder.done_task.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return groupTaskDetailsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView img;
        ImageView low,high;
        public TextView t1, t2, t3, t4, t5, t6,active_task,in_process_task,done_task;
        LinearLayout data;
        public LinearLayout linearLayout;
        public CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);
            t1 = (TextView) itemView.findViewById(R.id.title);
          //  t2 = (TextView) itemView.findViewById(R.id.priority);
            t3 = (TextView) itemView.findViewById(R.id.comment);
            t4 = (TextView) itemView.findViewById(R.id.assign);
           // t6 = (TextView) itemView.findViewById(R.id.status);
            cardView = (CardView) itemView.findViewById(R.id.cv1);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.layout1);


            high= (ImageView) itemView.findViewById(R.id.high);
            low= (ImageView) itemView.findViewById(R.id.low);

            active_task= (TextView) itemView.findViewById(R.id.activetask);
            in_process_task=(TextView)itemView.findViewById(R.id.in_processtask);
            done_task=(TextView)itemView.findViewById(R.id.donetask);
        }
    }
}
