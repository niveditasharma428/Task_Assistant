package com.example.admin.task_assistant;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class RecyclerViewAdapter3  extends RecyclerView.Adapter<RecyclerViewAdapter3.ViewHolder> {

    List<Contact3> mData1 = new ArrayList<>();
    Context context1;
    String TASK_ID,mobile,data;
    Button send;
    Spinner spinnerDropDown;
    LinearLayout rootLayout;
    SharedPreferences pref;
    public LinearLayout linearLayout;
    ArrayList<String> ContactName;


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView img;
        public TextView t1,t2,t3,t4,t5;

        ImageView del,reassign;
        LinearLayout data;

        public CardView cardView;

        public ViewHolder(View itemView) {

            super(itemView);

            t1 = (TextView) itemView.findViewById(R.id.title);
            t2 = (TextView) itemView.findViewById(R.id.priority);
            t4 = (TextView) itemView.findViewById(R.id.assign);
            del = (ImageView)itemView.findViewById(R.id.delete);
            reassign = (ImageView)itemView.findViewById(R.id.reassign);
            rootLayout= (LinearLayout) itemView.findViewById(R.id.rootLayout);
            cardView = (CardView) itemView.findViewById(R.id.cv1);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.layout1);

            ContactName=new ArrayList<String>();

        }

        @Override
        public void onClick(View view) {

        }
    }

    public RecyclerViewAdapter3(List<Contact3>getContact1,Context context1 ) {

        super();
        this.mData1 = getContact1;
        this.context1= context1;

    }

    @Override
    public RecyclerViewAdapter3.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item3, parent, false);

        pref = view.getContext().getSharedPreferences("Options", MODE_PRIVATE);
        mobile = pref.getString("mobile", "");

        return new RecyclerViewAdapter3.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final RecyclerViewAdapter3.ViewHolder holder, final int position) {

        final Contact3 mDataOBJ = mData1.get(position);

        holder.t1.setText(mDataOBJ.getTASK_TITLE());
        holder.t2.setText(mDataOBJ.getTASK_PRIORITY());

        holder.t4.setText(mDataOBJ.getASSIGN_TO());

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

                                    Snackbar.make(rootLayout, jsonObject.getString("message") + "", Snackbar.LENGTH_LONG)
                                            .show();

                                   // Toast.makeText(view.getContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();

                                }
                                else if(jsonObject.getInt("success")==1){

                                    Snackbar.make(rootLayout, jsonObject.getString("message") + "", Snackbar.LENGTH_LONG)
                                            .show();

                                   // Toast.makeText(view.getContext(), "Delete task successful", Toast.LENGTH_SHORT).show();

                                    mData1.remove(holder.getAdapterPosition());
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

        holder.reassign.setOnClickListener(new View.OnClickListener() {

            String REASSIGN_URL = "https://orgone.solutions/task/closetask_reassign.php";

            @Override
            public void onClick(final View view) {

                   LayoutInflater li = LayoutInflater.from(view.getContext());

                    View confirmDialog = li.inflate(R.layout.dialog_reassign, null);

                    send = (Button) confirmDialog.findViewById(R.id.buttonSend);
                    spinnerDropDown=(Spinner)confirmDialog.findViewById(R.id.spinner3);

                    AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());

                    alert.setView(confirmDialog);

                    final AlertDialog alertDialog = alert.create();

                    alertDialog.show();

                    String CONTACT_URL = "https://orgone.solutions/task/userdata.php";

                    int i = 0;

                    StringRequest stringRequest=new StringRequest(Request.Method.POST, CONTACT_URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try{
                                JSONArray jsonObj = new JSONArray(response);
                                final int numberOfItemsInResp = jsonObj.length();
                                ContactName.clear();
                                for (int i = 0; i < numberOfItemsInResp; i++) {
                                    JSONObject jsonObject = (JSONObject) jsonObj.get(i);

                                    String name = jsonObject.getString("name");

                                    ContactName.add( name);

                                }
                                spinnerDropDown.setAdapter(new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_dropdown_item, ContactName));

                            }catch (JSONException e){e.printStackTrace();}
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("mobile",mobile);
                            return params;
                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
                    requestQueue.add(stringRequest);


                    send.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            alertDialog.dismiss();

                            final ProgressDialog loading = ProgressDialog.show(view.getContext(), "Authenticating", "Please wait while we check the entered code", false, false);

                           final String data = spinnerDropDown.getSelectedItem().toString();

                            RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
                            StringRequest postRequest = new StringRequest(Request.Method.POST, REASSIGN_URL,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            JSONObject jsonObject = null;
                                            try {
                                                jsonObject = new JSONObject(response);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                            try {
                                                if (jsonObject.getInt("success") == 1) {

                                                    loading.dismiss();

                                                    Snackbar.make(rootLayout, jsonObject.getString("message") + "", Snackbar.LENGTH_LONG)
                                                            .show();

                                                    // Toast.makeText(view.getContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                                    mData1.remove(holder.getAdapterPosition());
                                                    notifyDataSetChanged();
                                                   // Intent intent = new Intent (context1, TaskAssign.class);
                                                   // context1.startActivity(intent);

                                                } else {

                                                    Snackbar.make(rootLayout, jsonObject.getString("message") + "", Snackbar.LENGTH_LONG)
                                                            .show();


                                                 //   Toast.makeText(view.getContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();

                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            alertDialog.dismiss();
                                            Toast.makeText(view.getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<String, String>();

                                    params.put("TASK_ID",mDataOBJ.getTASK_ID());
                                    params.put("TASK_ASSIGN",data);


                                    return params;
                                }
                            };


                            int socketTimeout = 30000;
                            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                            postRequest.setRetryPolicy(policy);
                            requestQueue.add(postRequest);
                        }
                    });


            }


        });


    }

    @Override
    public int getItemCount() {
        return mData1.size();
    }

}
