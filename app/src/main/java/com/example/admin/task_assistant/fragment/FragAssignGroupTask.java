package com.example.admin.task_assistant.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.task_assistant.GroupTaskAssistant;
import com.example.admin.task_assistant.Network.APIClient;
import com.example.admin.task_assistant.R;
import com.example.admin.task_assistant.model.GroupDetails;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragAssignGroupTask extends Fragment {

    SharedPreferences pref;
    Bundle bundle;
    TextView txt_groupname, txt_groupmem;
    EditText etDes;
    Spinner spn_priority;
    Button btn_assign, btn_cancel;
    String[] priority = {"High", "Low"};
    String createdBY, prioritySel;
    ProgressDialog progressDialog;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragassigngroup, container, false);

        pref = getContext().getSharedPreferences("Options", getContext().MODE_PRIVATE);
        createdBY = pref.getString("name", "");
        txt_groupmem = (TextView) rootView.findViewById(R.id.txt_membername);
        txt_groupname = (TextView) rootView.findViewById(R.id.txt_groupname);
        spn_priority = (Spinner) rootView.findViewById(R.id.spinner1);
        btn_assign = (Button) rootView.findViewById(R.id.buttonAssign);
        btn_cancel = (Button) rootView.findViewById(R.id.cancel);
        etDes = (EditText) rootView.findViewById(R.id.etDes);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading, please wait...");


        bundle = this.getArguments();

        final String groupname = bundle.getString("groupname");
        final String groupmember = bundle.getString("groupmembers");
        System.out.println("DivyaAssignTask:-" + groupmember + "---" + groupname);


        String grp = groupmember.substring(0, groupmember.indexOf('-'));
        System.out.println("DivyaGroupmem:-" + grp);

        txt_groupname.setText(groupname);
        txt_groupmem.setText(grp);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.
                R.layout.simple_spinner_dropdown_item, priority);

        spn_priority.setAdapter(adapter);

        spn_priority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                prioritySel = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btn_assign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();


                if (TextUtils.isEmpty(etDes.getText().toString())) {
                    etDes.setError("Please Enter Task Description");
                } else {

                    Call<GroupDetails> createTask = APIClient.getInstance().createTask(etDes.getText().toString(), prioritySel, createdBY, txt_groupname.getText().toString(), txt_groupmem.getText().toString());

                    createTask.enqueue(new Callback<GroupDetails>() {
                        @Override
                        public void onResponse(Call<GroupDetails> call, Response<GroupDetails> response) {
                            if (response.isSuccessful()) {
                                GroupDetails groupDetails = response.body();

                                if (groupDetails.getSuccess().equalsIgnoreCase("1")) {
                                    Toast.makeText(getContext(), groupDetails.getMessage(), Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(getContext(), GroupTaskAssistant.class);
                                    i.putExtra("groupmember", groupmember);
                                    i.putExtra("groupname", groupname);
                                    getContext().startActivity(i);
                                    progressDialog.dismiss();
                                } else {
                                    Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();

                                }
                            }

                        }

                        @Override
                        public void onFailure(Call<GroupDetails> call, Throwable t) {
                            progressDialog.dismiss();

                        }
                    });
                }

            }
        });


        return rootView;
    }
}
