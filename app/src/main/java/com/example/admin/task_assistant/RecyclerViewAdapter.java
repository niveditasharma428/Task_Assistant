package com.example.admin.task_assistant;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {


    List<Contact> mData = new ArrayList<>();
    Context context;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView img;
        public TextView t1,t2,t3;
        LinearLayout data;


        public ViewHolder(View itemView) {

            super(itemView);

            img = (ImageView) itemView.findViewById(R.id.imageView);
            t1 = (TextView) itemView.findViewById(R.id.textViewName);
            t2 = (TextView) itemView.findViewById(R.id.textViewContact);
            t3 = (TextView) itemView.findViewById(R.id.textViewTag);
            data=(LinearLayout)itemView.findViewById(R.id.layout1);


        }

        @Override
        public void onClick(View view) {

        }
    }

    public RecyclerViewAdapter(List<Contact>getContact,Context context ) {


        super();
        this.mData = getContact;
        this.context= context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);



        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder,final int position) {

        final Contact mDataOBJ = mData.get(position);

        holder.t1.setText(mDataOBJ.getName());
        holder.t2.setText(mDataOBJ.getPhone());
        holder.t3.setText(mDataOBJ.getTag());


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public void onBindViewHolder(RecyclerViewAdapter holder, int position) {

    }

}
