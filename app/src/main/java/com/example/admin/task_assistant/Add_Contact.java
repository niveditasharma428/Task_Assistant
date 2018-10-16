
package com.example.admin.task_assistant;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.HashMap;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Add_Contact extends AppCompatActivity {

    private final int REQUEST_CODE=99;
    public static final int REQUEST_CODE_PHONE_STATE_READ = 100;
    private int checkedPermission = PackageManager.PERMISSION_DENIED;
    private EditText phoneEditText;
    private Button sendButton,dash;
    private ImageView pickContact;
    TextView close;
    String mobile_no,mobile,usertyp,admin;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    LinearLayout rootLayout;

    private static String KEY_URL = "https://orgone.solutions/task/key_master.php";

    public void attachBaseContext(Context newBase){
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("font/Arkhip_font.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        setContentView(R.layout.activity_add__contact);

        close= (TextView) findViewById(R.id.txtclose1);
        phoneEditText = (EditText) findViewById(R.id.phone_number_edit_text);
        sendButton = (Button) findViewById(R.id.send_button);
        dash = (Button) findViewById(R.id.goto_dash);
        rootLayout= (LinearLayout) findViewById(R.id.rootLayout);
        pickContact = (ImageView) findViewById(R.id.add_contact_image_view);

        checkedPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
        if (Build.VERSION.SDK_INT >= 23 && checkedPermission != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
        } else
            checkedPermission = PackageManager.PERMISSION_GRANTED;

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mobile_no = phoneEditText.getText().toString();

                pref=getApplication().getSharedPreferences("Options",MODE_PRIVATE);
                mobile = pref.getString("mobile", "");

                phoneEditText.onEditorAction(EditorInfo.IME_ACTION_DONE);

                generate(KEY_URL,mobile_no,mobile);

            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Add_Contact.this, Dashboard.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        dash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Add_Contact.this, Dashboard.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        pickContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, REQUEST_CODE);
                overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestPermission() {
        Toast.makeText(Add_Contact.this, "Requesting permission", Toast.LENGTH_SHORT).show();
        this.requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},
                REQUEST_CODE_PHONE_STATE_READ);
    }
    private void generate(String keyUrl, final String getMobile_no, final String getMobile){

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        StringRequest stringRequest  =new StringRequest(Request.Method.POST, keyUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);

                    if(jsonObject.getInt("success")==1)
                    {
                        Snackbar.make(rootLayout, jsonObject.getString("message") + "", Snackbar.LENGTH_LONG)
                        .show();
                    }
                    else {


                        Snackbar.make(rootLayout, jsonObject.getString("message") + "", Snackbar.LENGTH_LONG)
                                .show();
                       //  Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                    }


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

                param.put("mobile_no",getMobile_no);
                param.put("mobile",getMobile);

                return param;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        if (checkedPermission != PackageManager.PERMISSION_DENIED) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            super.onActivityResult(reqCode, resultCode, data);
            switch (reqCode) {
                case (REQUEST_CODE):
                    if (resultCode == Activity.RESULT_OK) {
                        Uri contactData = data.getData();
                        Cursor c = getContentResolver().query(contactData, null, null, null, null);
                        if (c.moveToFirst()) {
                            String contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                            String hasNumber = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                            String num = "";
                            if (Integer.valueOf(hasNumber) == 1) {
                                Cursor numbers = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);
                                while (numbers.moveToNext()) {
                                    String number = numbers.getString(numbers.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                                    phoneEditText.setText(number);
                                }
                            }
                        }
                        break;
                    }
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case REQUEST_CODE_PHONE_STATE_READ:
                if (grantResults.length > 0 && grantResults[0]== PackageManager.PERMISSION_GRANTED ) {
                    checkedPermission = PackageManager.PERMISSION_GRANTED;
                }
                break;

        }
    }

    public void onBackPressed() {
        Intent back = new Intent(getApplicationContext(), Dashboard.class);
        back.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        back.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(back);
        overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
