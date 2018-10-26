package com.example.admin.task_assistant;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.task_assistant.Network.APIClient;
import com.example.admin.task_assistant.model.CloseTaskDetails;
import com.example.admin.task_assistant.model.ContactDetails;
import com.example.admin.task_assistant.model.GroupDetails;
import com.example.admin.task_assistant.model.MemberDetails;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecyclerViewAdapter6 extends RecyclerView.Adapter<RecyclerViewAdapter6.ViewHolder> {


    List<CloseTaskDetails> closeTaskDetailsList;
    List<MemberDetails> memberDetailsList;
    Context context;
    SharedPreferences pref;
    String name, mobile, email,reassignMem;

    public RecyclerViewAdapter6(List<CloseTaskDetails> closeTaskDetailsList, Context context) {
        this.closeTaskDetailsList = closeTaskDetailsList;
        this.context = context;
        System.out.println("DivyaTodo2");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        System.out.println("DivyaTodo1");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerviewitem6, parent, false);
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

        final CloseTaskDetails closeTaskDetails = closeTaskDetailsList.get(position);

        holder.title.setText(closeTaskDetails.getTASK_DES());
        holder.groupname.setText(closeTaskDetails.getTASK_GROUP());
        holder.priority.setText(closeTaskDetails.getTASK_PRIORITY());
        holder.assign.setText(closeTaskDetails.getTASK_ASSIGN());

        if (closeTaskDetails.getTASK_GROUP().equalsIgnoreCase(" ")) {
            holder.groupname.setVisibility(View.GONE);
        } else {
            holder.groupname.setVisibility(View.VISIBLE);
        }

        holder.iv_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Call<GroupDetails> task_delete = APIClient.getInstance().closetask_delete(closeTaskDetails.getTASK_ID().toString(), closeTaskDetails.getTASK_GROUP());
                task_delete.enqueue(new Callback<GroupDetails>() {
                    @Override
                    public void onResponse(Call<GroupDetails> call, Response<GroupDetails> response) {

                        if (response.isSuccessful()) {
                            GroupDetails groupDetails = response.body();
                            if (groupDetails.getSuccess().equalsIgnoreCase("1")) {
                                closeTaskDetailsList.remove(holder.getAdapterPosition());
                                notifyDataSetChanged();

                                Toast.makeText(context, groupDetails.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<GroupDetails> call, Throwable t) {

                    }
                });

            }
        });

        holder.iv_reassign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater li = LayoutInflater.from(view.getContext());

                View confirmDialog = li.inflate(R.layout.dialog_reassign, null);

                final Button send = (Button) confirmDialog.findViewById(R.id.buttonSend);
                final Spinner spinnerDropDown = (Spinner) confirmDialog.findViewById(R.id.spinner3);

                AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());

                alert.setView(confirmDialog);

                final AlertDialog alertDialog = alert.create();

                alertDialog.show();

                Call<List<ContactDetails>> getContacts = APIClient.getInstance().getContactDetails(mobile);

               getContacts.enqueue(new Callback<List<ContactDetails>>() {
                   @Override
                   public void onResponse(Call<List<ContactDetails>> call, Response<List<ContactDetails>> response) {

                       ArrayList al= new ArrayList();
                       List<ContactDetails> contactDetailsList = response.body();
                       for (int i = 0; i < contactDetailsList.size(); i++) {

                           name = contactDetailsList.get(i).getName();
                           al.add(name);

                           System.out.println("TaskAssistant:-" + name );
                       }

                       final ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, al);
                       spinnerDropDown.setAdapter(adapter1);


                       spinnerDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                           @Override
                           public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                               reassignMem = spinnerDropDown.getItemAtPosition(i).toString();


                           }

                           @Override
                           public void onNothingSelected(AdapterView<?> adapterView) {

                           }
                       });

                       send.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View view) {

                               System.out.println("DivyaSrtingname1;-" + reassignMem);

                               Call<GroupDetails> closetask_reassign = APIClient.getInstance().closetask_reassign(closeTaskDetails.getTASK_ID().toString(), closeTaskDetails.getTASK_GROUP().toString(), reassignMem);

                               System.out.println("DivyaSrtingname;-" + reassignMem);
                               closetask_reassign.enqueue(new Callback<GroupDetails>() {
                                   @Override
                                   public void onResponse(Call<GroupDetails> call, Response<GroupDetails> response) {
                                       GroupDetails groupDetails = response.body();
                                       if (response.isSuccessful()) {
                                           if (groupDetails.getSuccess().equalsIgnoreCase("1")) {
                                               Toast.makeText(context, groupDetails.getMessage(), Toast.LENGTH_SHORT).show();
                                               alertDialog.dismiss();
                                               closeTaskDetailsList.remove(holder.getAdapterPosition());
                                               notifyDataSetChanged();
                                           }

                                       }
                                   }

                                   @Override
                                   public void onFailure(Call<GroupDetails> call, Throwable t) {

                                   }
                               });

                           }
                       });


                   }

                   @Override
                   public void onFailure(Call<List<ContactDetails>> call, Throwable t) {

                   }
               });



            }
        });


    }

    @Override
    public int getItemCount() {

        System.out.println("DivyaRecy6count:-" + closeTaskDetailsList.size());
        return closeTaskDetailsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView img;
        public TextView title, groupname, priority, assign;
        LinearLayout data;
        ImageView iv_del, iv_reassign;

        public ViewHolder(View itemView) {

            super(itemView);

            title = (TextView) itemView.findViewById(R.id.title);
            groupname = (TextView) itemView.findViewById(R.id.txtgroupname);
            priority = (TextView) itemView.findViewById(R.id.priority);
            assign = (TextView) itemView.findViewById(R.id.assign);
            iv_del = (ImageView) itemView.findViewById(R.id.delete);
            iv_reassign = (ImageView) itemView.findViewById(R.id.reassign);
            data = (LinearLayout) itemView.findViewById(R.id.layout1);


        }


        @Override
        public void onClick(View view) {

        }
    }


}
