package com.example.admin.task_assistant.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.admin.task_assistant.Network.APIClient;
import com.example.admin.task_assistant.R;
import com.example.admin.task_assistant.adapter.GroupAddUserAdapter;
import com.example.admin.task_assistant.model.ContactDetails;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragAddGroupUser extends Fragment {

    SharedPreferences pref;
    String mobile, createdBy,name;
    RecyclerView recyclerView;
    GroupAddUserAdapter groupAddUserAdapter;
    Button btn_add;
    EditText et_groupname;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_addusergroup, container, false);
        pref = getContext().getSharedPreferences("Options", getContext().MODE_PRIVATE);
        name=pref.getString("name", "");
        mobile = pref.getString("mobile", "");
        createdBy = name;
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerviewadd);
        btn_add = (Button) rootView.findViewById(R.id.btn_add);
        et_groupname = (EditText) rootView.findViewById(R.id.et_groupname);
        System.out.println("TaskMobileNo:-" + mobile + createdBy);
        getContacts(mobile);

        return rootView;

        }


    private void getContacts(final String mobile) {

        final Call<List<ContactDetails>> getContacts = APIClient.getInstance().getContactDetails(mobile);

        getContacts.enqueue(new Callback<List<ContactDetails>>() {
            @Override
            public void onResponse(Call<List<ContactDetails>> call, Response<List<ContactDetails>> response) {

                ArrayList addContacts = new ArrayList<>();
                String mobileno, name, tag,image;

                if (response.isSuccessful()) {
                    List<ContactDetails> contactDetailsList = response.body();
                    for (int i = 0; i < contactDetailsList.size(); i++) {

                        mobileno = contactDetailsList.get(i).getMobile_no();
                        name = contactDetailsList.get(i).getName();
                        tag = contactDetailsList.get(i).getTag();
                        image=contactDetailsList.get(i).getUSER_PHOTO();

                        addContacts.add(new ContactDetails(mobileno, name, tag,image));
                        System.out.println("TaskAssistant:-" + mobileno + name + tag +image);
                    }

                }

                groupAddUserAdapter = new GroupAddUserAdapter(addContacts, getContext(), btn_add, et_groupname, createdBy);
                LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setAdapter(groupAddUserAdapter);

            }

            @Override
            public void onFailure(Call<List<ContactDetails>> call, Throwable t) {
                System.out.println("TaskFailure:-" + t);

            }
        });


    }


}
