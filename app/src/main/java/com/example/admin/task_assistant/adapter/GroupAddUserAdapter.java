package com.example.admin.task_assistant.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.task_assistant.Group;
import com.example.admin.task_assistant.Network.APIClient;
import com.example.admin.task_assistant.R;
import com.example.admin.task_assistant.model.ContactDetails;
import com.example.admin.task_assistant.model.GroupDetails;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupAddUserAdapter extends RecyclerView.Adapter<GroupAddUserAdapter.MyViewHolder> {

    List<ContactDetails> contactDetailsList;
    Context context;
    Button btn_add;
    EditText et_groupname;
    String createdby;
    ArrayList<String> al;
    SharedPreferences pref;
    String name, mobile, admin_mob, admin_name;

    public GroupAddUserAdapter(List<ContactDetails> contactDetailsList, Context context, Button btn_add, EditText et_groupname, String createdby) {
        this.contactDetailsList = contactDetailsList;
        this.context = context;
        this.btn_add = btn_add;
        this.et_groupname = et_groupname;
        this.createdby = createdby;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_groupadduser, parent, false);
        pref = context.getSharedPreferences("Options", context.MODE_PRIVATE);
        name = pref.getString("name", "");
        mobile = pref.getString("mobile", "");
        admin_mob = pref.getString("admin_mob", "");
        admin_name = pref.getString("admin_name", "");
        al = new ArrayList();
        al.add(name + " - " + mobile);
        if (!TextUtils.isEmpty(admin_name)) {
            al.add(admin_name + " - " + admin_mob);
            System.out.println("DivyaArrayList:-"+al);
        }

        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final ContactDetails contactDetails = contactDetailsList.get(position);

        holder.txt_mobileno.setText(contactDetails.getMobile_no());
        holder.txt_name.setText(contactDetails.getName());

        System.out.println("Contact:"+contactDetails.getUSER_PHOTO());




        if(contactDetails.getUSER_PHOTO().equals(""))
        {
            holder.image.setImageResource(R.drawable.pro1);
        }
        else {

            //System.out.println("Contact:"+contactDetails.getUSER_PHOTO());

           // Picasso.with((context)).load(contactDetails.getUSER_PHOTO())
            Picasso.with((context)).load("https://orgone.solutions/task/image/"+contactDetails.getUSER_PHOTO())
                    .into(holder.image);
        }

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.checkBox.isChecked()) {

                    if (!al.contains(holder.txt_name.getText().toString().trim() + " - " + holder.txt_mobileno.getText().toString().trim()) || !al.contains(admin_name + " - " + admin_mob)) {
                        al.add(holder.txt_name.getText().toString() + " - " + holder.txt_mobileno.getText().toString());
                        System.out.println("TaskArray:-" + al);
                    }


                } else {
                    if (!al.contains(admin_name + " - " + admin_mob) || !al.contains(name + " - " + mobile)) {
                        al.remove(holder.txt_name.getText().toString() + " - " + holder.txt_mobileno.getText().toString());
                        System.out.println("TaskArrayRemove:-" + al);
                    }
                }


            }
        });


        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(et_groupname.getText().toString())) {
                    et_groupname.setError("Please Enter Group Name");
                } else if (al.isEmpty()) {
                    Toast.makeText(context, "Please Select Members", Toast.LENGTH_SHORT).show();
                } else {

                    System.out.println("TaskArrayList1:-" + al);
                    String groupMember = "";
                    for (int i = 0; i < al.size(); i++) {
                        groupMember = groupMember + al.get(i).toString() + ",";
                    }

                    System.out.println("TaskArrayName:-" + groupMember);

                    Call<GroupDetails> createGroup = APIClient.getInstance().createGroup(et_groupname.getText().toString(), createdby, groupMember);
                    createGroup.enqueue(new Callback<GroupDetails>() {
                        @Override
                        public void onResponse(Call<GroupDetails> call, Response<GroupDetails> response) {

                            System.out.println("response:-" + response.code());

                            if (response.isSuccessful()) {

                                GroupDetails groupDetails = response.body();
                                System.out.println("DivyaGroupMessage:-" + groupDetails.getSuccess() + "--" + groupDetails.getMessage());

                                if (groupDetails.getSuccess().equalsIgnoreCase("1")) {
                                    Toast.makeText(context, groupDetails.getMessage(), Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(context, Group.class);
                                    context.startActivity(i);
                                    al.clear();

                                } else {
                                    Toast.makeText(context, groupDetails.getMessage(), Toast.LENGTH_SHORT).show();
                                }


                            } else {
                                Toast.makeText(context, "Members not added", Toast.LENGTH_SHORT).show();
                            }


                        }

                        @Override
                        public void onFailure(Call<GroupDetails> call, Throwable t) {
                            System.out.println("AddGroupMemnerFailure:-" + t);
                            al.clear();
                        }
                    });


                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return contactDetailsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txt_name, txt_mobileno;
        CheckBox checkBox;
        Button btn_add;
        CircleImageView image;

        public MyViewHolder(View itemView) {
            super(itemView);

            txt_name = (TextView) itemView.findViewById(R.id.textViewName);
            txt_mobileno = (TextView) itemView.findViewById(R.id.textViewContact);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkbox);
            image=(CircleImageView)itemView.findViewById(R.id.image);

        }
    }
}
