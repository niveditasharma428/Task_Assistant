package com.example.admin.task_assistant;

import android.Manifest;
import android.app.Activity;
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
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Profile extends AppCompatActivity implements View.OnClickListener {

    TextView tvMainEmail, tvMobile, tvMainName,tvCount;
    AlertDialog alertdialog;
    int PICK_IMAGE_REQUEST = 111;
    Button upload;
    ImageView imageView;
    String name,email,mobile,count,usertyp;
    SharedPreferences.Editor editor;
    SharedPreferences pref1;
    SharedPreferences pref;
    ProgressDialog progressDialog;

    SharedPreferences prefm;
    SharedPreferences.Editor editorm;

    String cheqImageChange="";

    private static String LOGOUT_URL = "https://orgone.solutions/task/blogout.php";
    private static String IMAGE_URL = "https://orgone.solutions/task/imgchk.php";
    private static String PROFILE_URL = "https://orgone.solutions/task/profile.php";

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

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_white_24dp);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(Profile.this, Dashboard.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition( R.anim.slide_in_left, R.anim.slide_out_right);

            }
        });

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

        imageView.setOnClickListener(this);

        mobile =pref.getString("mobile", "");
        tvMobile.setText(mobile);

        count =pref.getString("count", "");
        tvCount.setText(count);

        usertyp = pref.getString("usertyp", "");


        /* prefm =getSharedPreferences("Picture", MODE_PRIVATE);
         String mImageUri = prefm.getString("Image", "");
         System.out.println("Image1:-"+mImageUri);

        if (!TextUtils.isEmpty(mImageUri)) {
            Picasso.with(getApplicationContext()).load("https://orgone.solutions/task/"+mImageUri)
                    .fit()
                    .into(imageView);
        } else {
            imageView.setImageResource(R.drawable.profile_girl);
        }

*/
        progressDialog = new ProgressDialog(Profile.this);
        progressDialog.setMessage("Loading, please wait...");
        progressDialog.show();
        int i = 0;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, PROFILE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(login.this, response, Toast.LENGTH_SHORT).show();
                try {
                    progressDialog.dismiss();
                    JSONArray jsonObj = new JSONArray(response);
                    final int numberOfItemsInResp = jsonObj.length();
                    for (int i = 0; i < numberOfItemsInResp; i++) {
                        JSONObject jsonObject = (JSONObject) jsonObj.get(i);

                        String name = jsonObject.getString("name");
                        tvMainName.setText(name);

                        String email = jsonObject.getString("email");
                        tvMainEmail.setText(email);

                        String mobile1 = jsonObject.getString("mobile");
                        tvMobile.setText(mobile1);

                        String img = jsonObject.getString("USER_PHOTO");

                        if(jsonObject.getString("USER_PHOTO").equals(""))
                        {
                            imageView.setImageResource(R.drawable.pro1);
                        }
                        else {

                            Picasso.with(getApplicationContext()).load("https://orgone.solutions/task/image/"+jsonObject.getString("USER_PHOTO"))
                                    .fit()
                                    .into(imageView);
                        }

                        prefm =getSharedPreferences("Picture", MODE_PRIVATE);
                        editorm = prefm.edit();
                        editorm.putString("Image", img);
                        editorm.commit();

                    }

                } catch (JSONException e) {
                    Toast.makeText(Profile.this, response, Toast.LENGTH_LONG).show();
                }

            }

        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();

                        Toast.makeText(Profile.this, error.toString(), Toast.LENGTH_LONG).show();

                    }

                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mobile", mobile);
                params.put("usertyp", usertyp);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(Profile.this);
        requestQueue.add(stringRequest);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                upload();

                //converting image to base64 string
               /* ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageBytes = baos.toByteArray();
                final String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
*/
                //     System.out.println("Image:-"+imageString);

            }

        });

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

            if (resultCode == Activity.RESULT_OK) {
                System.out.println("Divyacam6");
                if (requestCode == PICK_IMAGE_REQUEST) {

                    System.out.println("Divyacam7");

                    // Get the User Selected Image as Bitmap from the static method of ImagePicker class
                    Bitmap bitmap = ImagePicker.getImageFromResult(Profile.this, resultCode, data);

                    System.out.println("Divyacam8");

                    imageView.setImageBitmap(bitmap);
                    // Upload the Bitmap to ImageView
                    //    user_photo.setImageBitmap(bitmap);
                    System.out.println("Divyacam9");
                    // Get the converted Bitmap as Base64ImageString from the static method of Helper class
                    cheqImageChange = Utilities.getBase64ImageStringFromBitmap(bitmap);

                    System.out.println("Divyacam10:-" + cheqImageChange);
                }
            }

        }
    }

    private void upload(){

        RequestQueue requestQueue = new Volley().newRequestQueue(getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.POST, IMAGE_URL, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.getInt("success") == 1) {


                        Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();

                        JSONObject jsonObjectInfo = jsonObject.getJSONObject("USER_IMAGE");

                        String Upload_Image = jsonObjectInfo.getString("USER_PHOTO");

                        System.out.println("Upload:-"+Upload_Image);


                       Picasso.with(getApplicationContext()).load("https://orgone.solutions/task/image/"+Upload_Image)
                       // Picasso.with(getApplicationContext()).load(Upload_Image)
                                .into(imageView);

                    }

                }catch (JSONException e) {
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
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("cheqImageChange", cheqImageChange);
                param.put("mobile", mobile);
                param.put("usertyp", usertyp);

                return param;
            }

        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        requestQueue.add(postRequest);

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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {

       // imageSelect();
        pickImage();
    }

    private void pickImage() {

        if (Build.VERSION.SDK_INT <= 24) {
            // Intent with Image Picker Apps from the static method of ImagePicker class
            Intent chooseImageIntent = ImagePicker.getImagePickerIntent(getApplicationContext());

            // Start Activity with Image Picker Intent
            startActivityForResult(chooseImageIntent, PICK_IMAGE_REQUEST);
            System.out.println("Divyacam4");
        } else {
            Intent chooseImageIntent = ImagePicker.getImagePickerIntent(getApplicationContext());

            // Start Activity with Image Picker Intent
            startActivityForResult(chooseImageIntent, PICK_IMAGE_REQUEST);
            System.out.println("Divyacam4");
        }

    }
}
