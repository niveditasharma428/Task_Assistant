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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.task_assistant.Network.APIClient;
import com.example.admin.task_assistant.R;
import com.example.admin.task_assistant.adapter.GroupViewTaskAdapter;
import com.example.admin.task_assistant.model.GroupTask;
import com.example.admin.task_assistant.model.GroupTaskDetails;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragViewTask extends Fragment {
    RecyclerView recyclerView;
    SharedPreferences pref;
    String createdBy;
    List<GroupTaskDetails> groupTaskDetailsList;
    Bundle bundle;
    GroupViewTaskAdapter groupViewTaskAdapter;
    ProgressDialog progressDialog;
    TextView txtnorec;
    ImageView no_record;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragviewtask, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view3);
        txtnorec = (TextView) rootView.findViewById(R.id.txtnorec);
        no_record=(ImageView) rootView.findViewById(R.id.no_record);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading, please wait...");
        progressDialog.show();

        bundle = this.getArguments();

        final String groupname = bundle.getString("groupname");
        final String groupmember = bundle.getString("groupmembers");
        final String alstring = bundle.getString("al");

        pref = getContext().getSharedPreferences("Options", getContext().MODE_PRIVATE);
        createdBy = pref.getString("name", "");

        System.out.println("GroupViewTask:-" + groupmember + "-" + groupname + "--" + createdBy);

        Call<GroupTask> showGroupTask = APIClient.getInstance().groupTaskAssignView(groupname, groupmember);

        showGroupTask.enqueue(new Callback<GroupTask>() {
            @Override
            public void onResponse(Call<GroupTask> call, Response<GroupTask> response) {

                if (response.isSuccessful()) {

                    GroupTask groupTask = response.body();
                    ArrayList al = new ArrayList();
                    if (groupTask.getSuccess().equalsIgnoreCase("1")) {
                        groupTaskDetailsList = groupTask.getGroupTaskDetails();
                        System.out.println("DivyaGroupTaskDetailsNo:-" + groupTaskDetailsList.size());
                        if (groupTaskDetailsList.size() == 0) {
                            txtnorec.setVisibility(View.VISIBLE);
                            no_record.setVisibility(View.VISIBLE);

                        } else {
                            txtnorec.setVisibility(View.GONE);
                            no_record.setVisibility(View.GONE);
                        }
                        for (int i = 0; i < groupTaskDetailsList.size(); i++) {
                            String TASK_ID = groupTaskDetailsList.get(i).getTASK_ID().toString();
                            String TASK_DES = groupTaskDetailsList.get(i).getTASK_DES();
                            String TASK_COMMENT = groupTaskDetailsList.get(i).getTASK_COMMENT();
                            String TASK_PRIORITY = groupTaskDetailsList.get(i).getTASK_PRIORITY();
                            String TASK_STATUS = groupTaskDetailsList.get(i).getTASK_STATUS();
                            String TASK_GROUP = groupTaskDetailsList.get(i).getTASK_GROUP();
                            String assign = groupTaskDetailsList.get(i).getTASK_ASSIGN();
                            String TASK_ASSIGN = assign.substring(0, assign.indexOf('-'));

                            al.add(new GroupTaskDetails(TASK_ID, TASK_DES, TASK_COMMENT, TASK_PRIORITY, TASK_STATUS, TASK_GROUP, TASK_ASSIGN));

                            System.out.println("DivyaAL:-" + TASK_ID + TASK_ASSIGN);
                        }

                        groupViewTaskAdapter = new GroupViewTaskAdapter(getContext(), al, alstring, groupmember, groupname);
                        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.setAdapter(groupViewTaskAdapter);
                        progressDialog.dismiss();


                    }
                }
            }

            @Override
            public void onFailure(Call<GroupTask> call, Throwable t) {
                progressDialog.dismiss();
            }
        });

        return rootView;
    }

}
