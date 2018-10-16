package com.example.admin.task_assistant;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecyclerViewAdapter2 extends RecyclerView.Adapter<RecyclerViewAdapter2.ViewHolder> {
    List<Contact2> mData2 = new ArrayList<>();
    Context context2;
    LinearLayout rootLayout;


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView img;
        public TextView t1,t3,t6,t7,active_task,in_process_task,done_task;
        LinearLayout data;
        ImageView del,reassign,t4,t5;
        CardView cardView;


        public ViewHolder(View itemView) {

            super(itemView);

            t1 = (TextView) itemView.findViewById(R.id.title);
            t7 = (TextView) itemView.findViewById(R.id.assign_to);
           // t2 = (TextView) itemView.findViewById(R.id.priority);
            t3 = (TextView) itemView.findViewById(R.id.comment);
            del = (ImageView)itemView.findViewById(R.id.delete);
            t6 = (TextView) itemView.findViewById(R.id.status);

            t4= (ImageView) itemView.findViewById(R.id.high);
            t5= (ImageView) itemView.findViewById(R.id.low);

            active_task= (TextView) itemView.findViewById(R.id.active);
            in_process_task=(TextView)itemView.findViewById(R.id.in_process);
            done_task=(TextView)itemView.findViewById(R.id.done);


            rootLayout= (LinearLayout) itemView.findViewById(R.id.rootLayout);

            cardView= (CardView) itemView.findViewById(R.id.cv2);
        }


        @Override
        public void onClick(View view) {

        }
    }

    public RecyclerViewAdapter2(List<Contact2>getContact2,Context context2 ) {

        super();
        this.mData2 = getContact2;
        this.context2= context2;

    }

    public void setData(List<Contact2> getContact2) {

        this.mData2 = getContact2;

    }



    @Override
    public RecyclerViewAdapter2.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item2, parent, false);

        return new RecyclerViewAdapter2.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final RecyclerViewAdapter2.ViewHolder holder, final int position) {

        final Contact2 mDataOBJ = mData2.get(position);


        holder.t1.setText(mDataOBJ.getTASK_TITLE());
       // holder.t2.setText(mDataOBJ.getTASK_PRIORITY());
        holder.t3.setText(mDataOBJ.getComment());
//        holder.t6.setText(mDataOBJ.getTASK_STATUS());
        holder.t7.setText(mDataOBJ.getAssign());


        if(mDataOBJ.getTASK_PRIORITY().equalsIgnoreCase("High")){
            holder.t4.setVisibility(View.VISIBLE);
        } else {
            holder.t5.setVisibility(View.VISIBLE);
        }

        if(mDataOBJ.getTASK_STATUS().equalsIgnoreCase("Active")){
            holder.active_task.setVisibility(View.VISIBLE);
        } else if(mDataOBJ.getTASK_STATUS().equalsIgnoreCase("In process"))
            {
            holder.in_process_task.setVisibility(View.VISIBLE);
        }
        else {
            holder.done_task.setVisibility(View.VISIBLE);
        }





        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent i = new Intent(view.getContext(),TaskAssignComment.class);
                i.putExtra("TASK_ID",mDataOBJ.getTASK_ID());
                view.getContext().startActivity(i);

            }
        });



        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {


                String DELETE_TASK_URL = "https://orgone.solutions/task/task_delete.php";

                RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());

                StringRequest stringRequest  =new StringRequest(Request.Method.POST, DELETE_TASK_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);


                            if(jsonObject.getInt("success")==0)
                            {

                                Toast.makeText(view.getContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();

                            }
                            else if(jsonObject.getInt("success")==1){
                                Snackbar.make(rootLayout, jsonObject.getString("message") + "", Snackbar.LENGTH_LONG)
                                        .show();

                               // Toast.makeText(view.getContext(), "Delete task successful", Toast.LENGTH_SHORT).show();

                                mData2.remove(holder.getAdapterPosition());
                                notifyDataSetChanged();
                            }

                            else

                                Toast.makeText(view.getContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();

                    }
                })
                {
                    @Override
                    protected Map<String, String> getParams() {


                        Map<String, String> param = new HashMap<String, String>();

                        param.put("TASK_ID",mDataOBJ.getTASK_ID());

                        return param;
                    }
                };
                int socketTimeout = 30000;
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                stringRequest.setRetryPolicy(policy);
                requestQueue.add(stringRequest);

            }




        });


    }

    @Override
    public int getItemCount() {
        return mData2.size();
    }


    public void onBindViewHolder(RecyclerViewAdapter2 holder, int position) {

    }
}
