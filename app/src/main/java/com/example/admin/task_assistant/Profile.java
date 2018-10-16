package com.example.admin.task_assistant;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
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

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import de.hdodenhof.circleimageview.CircleImageView;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Profile extends AppCompatActivity implements View.OnClickListener {

    TextView tvMainEmail, tvMobile, tvMainName,tvCount;
    AlertDialog alertdialog;
    ImageView img;
    int PICK_IMAGE_REQUEST = 111;
    Bitmap bitmap;
    Button upload;
    ImageView imageView;
    String name,email,mobile,count;
    SharedPreferences.Editor editor;
    SharedPreferences pref1;
    SharedPreferences pref;

    private static String LOGOUT_URL = "https://orgone.solutions/task/blogout.php";
    private static String IMAGE_URL = "https://orgone.solutions/task/blogout.php";

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

        setContentView(R.layout.activity_profile);

        pref1 = getApplication().getSharedPreferences("Options_Admin", MODE_PRIVATE);
        pref=getApplication().getSharedPreferences("Options", MODE_PRIVATE);

        tvMainName = (TextView) findViewById(R.id.profile_name);
        tvMainEmail = (TextView) findViewById(R.id.profile_email);
        tvMobile = (TextView) findViewById(R.id.profile_mobile);
        tvCount = (TextView) findViewById(R.id.count);

        imageView= (ImageView) findViewById(R.id.profile_image);
        upload= (Button) findViewById(R.id.upload);
        Button logout = (Button) findViewById(R.id.buttonLogout);
       // img=(ImageView) findViewById(R.id.back_img);


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
/*        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile.this, Dashboard.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });*/

        imageView.setOnClickListener(this);

        name = pref.getString("name", "");
        tvMainName.setText(name);

        email = pref.getString("email", "");
        tvMainEmail.setText(email);

        mobile =pref.getString("mobile", "");
        tvMobile.setText(mobile);

        count =pref.getString("count", "");
        tvCount.setText(count);


        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = new ProgressDialog(Profile.this);
                progressDialog.setMessage("Uploading, please wait...");
                progressDialog.show();

                //converting image to base64 string
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageBytes = baos.toByteArray();
                final String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);

                //sending image to server
                StringRequest request = new StringRequest(Request.Method.POST, IMAGE_URL, new Response.Listener<String>(){
                    @Override
                    public void onResponse(String s) {
                        progressDialog.dismiss();
                        if(s.equals("true")){
                            Toast.makeText(Profile.this, "Image Uploaded Successful", Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(Profile.this, "Some error occurred!", Toast.LENGTH_LONG).show();
                        }
                    }
                },new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        
                        Toast.makeText(Profile.this, "Some error occurred -> "+volleyError, Toast.LENGTH_LONG).show();;
                    }
                }) {
                    //adding parameters to send
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> parameters = new HashMap<String, String>();
                        parameters.put("image", imageString);
                        return parameters;
                    }
                };

                RequestQueue rQueue = Volley.newRequestQueue(Profile.this);
                rQueue.add(request);
            }
        });


    }

    public void imageSelect() {
        permissionsCheck();
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
        } else {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
        }
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    public void permissionsCheck() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            return;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();

            try {
                //getting image from gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                //Setting image to ImageView
                imageView.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void logout() {

        name = pref.getString("name", "");
        tvMainName.setText(name);
        email = pref.getString("email", "");
        tvMainEmail.setText(email);
        mobile =pref.getString("mobile", "");
        tvMobile.setText(mobile);
        count =pref.getString("count", "");
        tvCount.setText(count);


         RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
         StringRequest stringRequest  =new StringRequest(Request.Method.POST, LOGOUT_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response){
                try {
                    JSONObject jsonObject=new JSONObject(response);

                    if(jsonObject.getInt("success")==1)

                    {
                        Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                        pref = getApplicationContext().getSharedPreferences("Options", MODE_PRIVATE);
                        editor=pref.edit();
                        editor.putString("name", name);
                        editor.putString("email", email);
                        editor.putString("mobile", mobile);
                        editor.remove("name");
                        editor.remove("email");
                        editor.remove("mobile");
                        editor.clear();
                        editor.commit();

                        Intent intent=new Intent(Profile.this, Signin.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);
                    }

                    else if(jsonObject.getInt("success")==2){

                        Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                        pref = getApplicationContext().getSharedPreferences("Options", MODE_PRIVATE);
                        editor=pref.edit();
                        editor.putString("name", name);
                        editor.putString("email", email);
                        editor.putString("mobile", mobile);
                        editor.remove("name");
                        editor.remove("email");
                        editor.remove("mobile");
                        editor.clear();
                        editor.commit();

                        Intent intent=new Intent(Profile.this, AdminLogin.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);
                    }

                    else

                        Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                }
                catch (JSONException e) {
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
                param.put(Config.KEY_MOBILE,mobile);

                return param;
            }
        };

        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);

    }

    public void onBackPressed() {
        Intent back = new Intent(getApplicationContext(), Dashboard.class);
        back.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        back.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(back);
        overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);
    }


    @Override
    public void onClick(View view) {

        imageSelect();
    }
}
