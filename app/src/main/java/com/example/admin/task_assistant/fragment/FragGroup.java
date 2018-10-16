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
import com.example.admin.task_assistant.adapter.ShowGroupAdapter;
import com.example.admin.task_assistant.model.Member;
import com.example.admin.task_assistant.model.MemberDetails;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragGroup extends Fragment {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String createdBy;
    RecyclerView recyclerView;
    ShowGroupAdapter showGroupAdapter;
    List<MemberDetails> groupMembersList;
    ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.frag_group, container, false);
        pref = getContext().getSharedPreferences("Options", getContext().MODE_PRIVATE);
        createdBy = pref.getString("name", "");
        System.out.println("FragGroupCreatedBy:-" + createdBy);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recshowgroup);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading, please wait...");
        progressDialog.show();

        Call<Member> showGroup = APIClient.getInstance().showGroup(createdBy);

        showGroup.enqueue(new Callback<Member>() {
            @Override
            public void onResponse(Call<Member> call, Response<Member> response) {
                if (response.isSuccessful()) {
                    ArrayList al = new ArrayList();
                    groupMembersList = response.body().getMemberDetails();

                    for (int i = 0; i < groupMembersList.size(); i++) {
                        String groupname = groupMembersList.get(i).getGROUP_NAME();
                        String groupmem = groupMembersList.get(i).getGROUP_MEMBER();
                        String groupdate = groupMembersList.get(i).getCREATED_DATE();
                        String groupcreatedby = groupMembersList.get(i).getCREATED_BY();
                        al.add(new MemberDetails(groupname, groupmem, groupdate, groupcreatedby));

                    }

                    showGroupAdapter = new ShowGroupAdapter(getContext(), al);
                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setAdapter(showGroupAdapter);
                    progressDialog.dismiss();

                }
            }

            @Override
            public void onFailure(Call<Member> call, Throwable t) {
                progressDialog.dismiss();
            }
        });


        return rootView;


    }
}
