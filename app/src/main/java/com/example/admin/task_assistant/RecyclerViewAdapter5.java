package com.example.admin.task_assistant;

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
import android.widget.Toast;

import com.example.admin.task_assistant.Network.APIClient;
import com.example.admin.task_assistant.model.GroupDetails;
import com.example.admin.task_assistant.model.MyTodoDetails;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecyclerViewAdapter5 extends RecyclerView.Adapter<RecyclerViewAdapter5.ViewHolder> {

    List<Contact4> mData = new ArrayList<>();
    List<MyTodoDetails> myTodoDetailsList;
    Context context;
    SharedPreferences pref;
    String name, mobile, email;

    public RecyclerViewAdapter5(List<MyTodoDetails> myTodoDetailsList, Context context) {
        this.myTodoDetailsList = myTodoDetailsList;
        this.context = context;
        System.out.println("DivyaTodo2");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        System.out.println("DivyaTodo1");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item4, parent, false);
        pref = context.getSharedPreferences("Options", Context.MODE_PRIVATE);
        name = pref.getString("name", "");
        email = pref.getString("email", "");
        mobile = pref.getString("mobile", "");
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        System.out.println("DivyaTodo3");
        //  final Contact4 mDataOBJ = mData.get(position);

        final MyTodoDetails myTodoDetails = myTodoDetailsList.get(position);


        holder.t1.setText(myTodoDetails.getCreatedBY());
        holder.t2.setText(myTodoDetails.getTaskGroup());
        holder.t3.setText(myTodoDetails.getNoOfTask());

        if(myTodoDetails.getNoOfTask().isEmpty()){
            holder.t3.setVisibility(View.INVISIBLE);
        } else {
            holder.t3.setVisibility(View.VISIBLE);
        }
        // holder.t2.setText(mDataOBJ.getSeen());
//        holder.img.setImageResource(mData.get(position).getPhoto());
        holder.data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Call<GroupDetails> taskseen = APIClient.getInstance().taskseen(mobile, myTodoDetails.getCreatedBY(), myTodoDetails.getTaskGroup());

                taskseen.enqueue(new Callback<GroupDetails>() {
                    @Override
                    public void onResponse(Call<GroupDetails> call, Response<GroupDetails> response) {
                        if (response.isSuccessful()) {
                            GroupDetails groupTask = response.body();

                            if (groupTask.getSuccess().equalsIgnoreCase("1")) {
                                Toast.makeText(context, groupTask.getMessage(), Toast.LENGTH_SHORT).show();

                                holder.t3.setText("");
                                Intent i = new Intent(context, MyTask.class);
                                i.putExtra("CREATED_BY", myTodoDetails.getCreatedBY());
                                i.putExtra("GROUP_NAME", myTodoDetails.getTaskGroup());
                                context.startActivity(i);
                            } else if (groupTask.getSuccess().equalsIgnoreCase("0")) {
                                Toast.makeText(context, groupTask.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<GroupDetails> call, Throwable t) {

                        System.out.println("DivyaRecycleviewAdapter:-" + t);

                    }
                });


            }
        });


    }

    @Override
    public int getItemCount() {
        return myTodoDetailsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView img;
        public TextView t1, t2, t3;
        LinearLayout data;


        public ViewHolder(View itemView) {

            super(itemView);


            img = (ImageView) itemView.findViewById(R.id.imageView);
            t1 = (TextView) itemView.findViewById(R.id.textViewName);
            t2 = (TextView) itemView.findViewById(R.id.textViewGroupName);
            t3 = (TextView) itemView.findViewById(R.id.count);
            data = (LinearLayout) itemView.findViewById(R.id.layout1);


        }


        @Override
        public void onClick(View view) {

        }
    }


}
