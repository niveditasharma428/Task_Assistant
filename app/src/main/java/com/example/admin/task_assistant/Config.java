package com.example.admin.task_assistant;

/**
 * Created by Belal on 11/18/2015.
 */
public class Config {
    //URLs to register.php and confirm.php file
    public static final String REGISTER_URL = "http://simplifiedcoding.16mb.com/AndroidOTP/register.php";
    public static final String CONFIRM_URL = "http://simplifiedcoding.16mb.com/AndroidOTP/confirm.php";

    //Keys to send username, password, phone and otp
    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_OCCUPATION = "occupation";
    public static final String KEY_PASSWORD = "admin_pass";
    public static final String KEY_MOBILE = "mobile";
    public static final String KEY_COMMENT = "TASK_COMMENT";
    public static final String KEY_TASK_ID = "TASK_ID";
    public static final String KEY_CREATED_BY = "CREATED_BY";
    public static final String KEY_TASK_STATUS = "TASK_STATUS";
    public static final String KEY_USERTYPE = "usertyp";
    public static final String KEY_NEW_PASSWORD = "password";
    public static final String KEY_CPASSWORD = "cpassword";
    public static final String KEY_UMOBILE = "umobile";
    public static final String KEY_UCODE = "ucode";
    public static final String KEY_OTP = "otp";

    //JSON Tag from response from server
    public static final String TAG_RESPONSE= "ErrorMessage";
}
