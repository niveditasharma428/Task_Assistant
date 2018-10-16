
package com.example.admin.task_assistant;

        import android.app.AlertDialog;
        import android.app.ProgressDialog;
        import android.content.Context;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.support.v7.widget.AppCompatButton;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
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
        import org.json.JSONException;
        import org.json.JSONObject;
        import java.util.HashMap;
        import java.util.Map;
        import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
        import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Forgot_Password extends AppCompatActivity implements View.OnClickListener {

    //Creating views

    private EditText editTextPhone;
    private EditText editTextConfirmOtp;
    private Button buttonSubmit;
    private Button buttonConfirm;
    private TextView textView;

    //Volley RequestQueue
    private RequestQueue requestQueue;

    String mobile,usertyp;

    private static String FORGOT_PASSWORD_URL = "https://orgone.solutions/task/fotp.php";
    private static String CONFIRM_OTP_URL = "https://orgone.solutions/task/fcheckotp.php";

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    public void attachBaseContext(Context newBase){
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("font/Montserrat-Light.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        setContentView(R.layout.activity_forgot__password);

        pref=getApplication().getSharedPreferences("Options",MODE_PRIVATE);
        mobile = pref.getString("mobile", "");
        //usertyp = pref.getString("usertyp", "");
        usertyp = getIntent().getStringExtra("usertyp");

        //Initializing Views
        editTextPhone = (EditText) findViewById(R.id.editTextMobile);
        buttonSubmit = (Button) findViewById(R.id.submit);

        //Initializing the RequestQueue
        requestQueue = Volley.newRequestQueue(this);

        //Adding a listener to button
        buttonSubmit.setOnClickListener(this);
    }

    //This method would confirm the otp
    private void confirmOtp() throws JSONException {
        //Creating a LayoutInflater object for the dialog box
        LayoutInflater li = LayoutInflater.from(this);
        //Creating a view to get the dialog box
        View confirmDialog = li.inflate(R.layout.dialog_confirm, null);

        //Initizliaing confirm button fo dialog box and edittext of dialog box
        buttonConfirm = (AppCompatButton) confirmDialog.findViewById(R.id.buttonConfirm);
        editTextConfirmOtp = (EditText) confirmDialog.findViewById(R.id.editTextOtp);

        //Creating an alertdialog builder
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        //Adding our dialog box to the view of alert dialog
        alert.setView(confirmDialog);

        //Creating an alert dialog
        final AlertDialog alertDialog = alert.create();

        //Displaying the alert dialog
        alertDialog.show();

        //On the click of the confirm button from alert dialog
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Hiding the alert dialog
                alertDialog.dismiss();

                //Displaying a progressbar
                final ProgressDialog loading = ProgressDialog.show(Forgot_Password.this, "Authenticating", "Please wait while we check the entered code", false,false);

                //Getting the user entered otp from edittext
                final String otp = editTextConfirmOtp.getText().toString().trim();

                //Creating an string request
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                StringRequest postRequest = new StringRequest(Request.Method.POST,CONFIRM_OTP_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                JSONObject jsonObject= null;
                                try {
                                    jsonObject = new JSONObject(response);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                try {
                                    if(jsonObject.getInt("success")==1){
                                        //dismissing the progressbar
                                        loading.dismiss();

                                        Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                                        Intent intent=new Intent(Forgot_Password.this,New_Password.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);

                                    }else{
                                        //Displaying a toast if the otp entered is wrong
                                        Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                                        Intent intent=new Intent(Forgot_Password.this,Forgot_Password.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);

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
                                Toast.makeText(Forgot_Password.this, error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<String, String>();
                        //Adding the parameters otp and username
                        params.put(Config.KEY_OTP, otp);
                        params.put(Config.KEY_MOBILE, mobile);
                        params.put(Config.KEY_USERTYPE, usertyp);
                        return params;
                    }
                };

                //Adding the request to the queue
                int socketTimeout = 30000;
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                postRequest.setRetryPolicy(policy);
                requestQueue.add(postRequest);
            }
        });
    }

    //this method will register the user
    private void forgot() {

        //Displaying a progress dialog
        final ProgressDialog loading = ProgressDialog.show(this, "Registering", "Please wait...", false, false);

        //Getting user data
        mobile = editTextPhone.getText().toString().trim();

        //Again creating the string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, FORGOT_PASSWORD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        try {
                            //Creating the json object from the response
                            JSONObject jsonObject=new JSONObject(response);

                            //If it is success
                            if(jsonObject.getInt("success")==1){

                                Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                                //Asking user to confirm otp
                                JSONObject jsonObjectInfo=jsonObject.getJSONObject("User");
                                String usertyp = jsonObjectInfo.getString("usertyp");
                                confirmOtp();

                                pref = getApplicationContext().getSharedPreferences("Options", MODE_PRIVATE);
                                editor=pref.edit();

                                editor.putString("mobile", mobile);
                                editor.putString("usertyp", usertyp);

                                editor.commit();
                            }else{
                                //If not successful user may already have registered
                                Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        Toast.makeText(Forgot_Password.this, error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding the parameters to the request
                params.put(Config.KEY_MOBILE, mobile);
                params.put(Config.KEY_USERTYPE, usertyp);
                return params;
            }
        };

        //Adding request  the queue
        requestQueue.add(stringRequest);
    }

    @Override
    public void onClick(View v) {
        //Calling register method on register button click
        forgot();
    }

}
