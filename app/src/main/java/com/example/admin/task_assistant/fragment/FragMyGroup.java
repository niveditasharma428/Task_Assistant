package com.example.admin.task_assistant.fragment;


import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.admin.task_assistant.Network.APIClient;
import com.example.admin.task_assistant.R;
import com.example.admin.task_assistant.adapter.ShowMyGroupAdapter;
import com.example.admin.task_assistant.model.MyGroupMemberDetails;
import com.example.admin.task_assistant.model.MyGroups;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragMyGroup extends Fragment {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String createdBy, mobile;
    RecyclerView recyclerView;
    ShowMyGroupAdapter showMyGroupAdapter;
    List<MyGroupMemberDetails> myGroupMemberDetails;
    ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.frag_group, container, false);
        pref = getContext().getSharedPreferences("Options", getContext().MODE_PRIVATE);
        createdBy = pref.getString("name", "");
        mobile = pref.getString("mobile", "");
        System.out.println("FragGroupCreatedBy:-" + createdBy);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recshowgroup);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading, please wait...");
        progressDialog.show();

        Call<MyGroups> myallgroups = APIClient.getInstance().myallgroups(mobile);

        myallgroups.enqueue(new Callback<MyGroups>() {
            @Override
            public void onResponse(Call<MyGroups> call, Response<MyGroups> response) {
                if (response.isSuccessful()) {
                    ArrayList al = new ArrayList();
                    myGroupMemberDetails = response.body().getMyGroupMemberDetails();

                    for (int i = 0; i < myGroupMemberDetails.size(); i++) {
                        String groupname = myGroupMemberDetails.get(i).getGROUP_NAME();
                        String groupcreatedby = myGroupMemberDetails.get(i).getCREATED_BY();
                        String createddate = myGroupMemberDetails.get(i).getCREATED_DATE();
                        System.out.println("DivyaGroupCreatedBy:-" + groupcreatedby);
                        al.add(new MyGroupMemberDetails(groupname, groupcreatedby,createddate));
                        }

                    showMyGroupAdapter = new ShowMyGroupAdapter(getContext(), al);
                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setAdapter(showMyGroupAdapter);
                    progressDialog.dismiss();

                }
            }

            @Override
            public void onFailure(Call<MyGroups> call, Throwable t) {
                progressDialog.dismiss();
            }
        });


        return rootView;


    }
}
