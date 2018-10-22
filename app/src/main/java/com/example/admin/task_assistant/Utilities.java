package com.example.admin.task_assistant;

import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class Utilities {

    public static String getBase64ImageStringFromBitmap(Bitmap bitmap) {
        String imgString;

        if (bitmap != null) {


            int compressionRatio = 25; //1 == originalImage, 2 = 50% compression, 4=25% compress

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressionRatio, byteArrayOutputStream);


            byte[] profileImage = byteArrayOutputStream.toByteArray();


            imgString = Base64.encodeToString(profileImage, Base64.NO_WRAP);

        } else {
            imgString = "";
        }


        return imgString;
    }

}
