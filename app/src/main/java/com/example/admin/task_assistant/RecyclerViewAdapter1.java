package com.example.admin.task_assistant;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter1 extends RecyclerView.Adapter<RecyclerViewAdapter1.ViewHolder>{

    List<Contact1> mData1 = new ArrayList<>();
    Context context1;
    String TASK_ID,name;


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView img;
        public TextView t1,t2,t3,t4,t5,t6,active_task,in_process_task,done_task;
        LinearLayout data;
        ImageView low,high;
        public LinearLayout linearLayout;
        public  CardView cardView;

        public ViewHolder(View itemView) {

            super(itemView);


            t1 = (TextView) itemView.findViewById(R.id.title);
            //  t2 = (TextView) itemView.findViewById(R.id.priority);
            t3 = (TextView) itemView.findViewById(R.id.comment);
            t4 = (TextView) itemView.findViewById(R.id.assign);
            t6 = (TextView) itemView.findViewById(R.id.status);
            cardView= (CardView) itemView.findViewById(R.id.cv1);
            linearLayout= (LinearLayout) itemView.findViewById(R.id.layout1);

            high= (ImageView) itemView.findViewById(R.id.high);
            low= (ImageView) itemView.findViewById(R.id.low);

            active_task= (TextView) itemView.findViewById(R.id.activetask);
            in_process_task=(TextView)itemView.findViewById(R.id.in_processtask);
            done_task=(TextView)itemView.findViewById(R.id.donetask);




        }

        @Override
        public void onClick(View view) {

        }
    }

    public RecyclerViewAdapter1(List<Contact1>getContact1,Context context1 ) {


        super();
        this.mData1 = getContact1;
        this.context1= context1;



    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item1, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final Contact1 mDataOBJ = mData1.get(position);


        holder.t1.setText(mDataOBJ.getTASK_TITLE());
//        holder.t2.setText(mDataOBJ.getTASK_PRIORITY());
        holder.t3.setText(mDataOBJ.getCOMMENT());
        holder.t4.setText(mDataOBJ.getCREATED_BY());
//        holder.t6.setText(mDataOBJ.getSTATUS());


        if(mDataOBJ.getTASK_PRIORITY().equalsIgnoreCase("High")){
            holder.high.setVisibility(View.VISIBLE);
        } else {
            holder.low.setVisibility(View.VISIBLE);
        }

        if(mDataOBJ.getSTATUS().equalsIgnoreCase("Active")){
            holder.active_task.setVisibility(View.VISIBLE);
        } else if(mDataOBJ.getSTATUS().equalsIgnoreCase("In process"))
        {
            holder.in_process_task.setVisibility(View.VISIBLE);
        }
        else {
            holder.done_task.setVisibility(View.VISIBLE);
        }




      /*  holder.cardView.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View view) {


                                                   Intent i = new Intent(view.getContext(),MyTaskComment.class);
                                                   i.putExtra("CREATED_BY",mDataOBJ.getCREATED_BY());
                                                   i.putExtra("TASK_ID",mDataOBJ.getTASK_ID());

                                                   view.getContext().startActivity(i);

                                               }
                                           });*/



    }

    @Override
    public int getItemCount() {
        return mData1.size();
    }


}
