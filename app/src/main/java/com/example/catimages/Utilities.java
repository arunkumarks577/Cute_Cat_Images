package com.example.catimages;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class Utilities {

    public static void bitmapToStringandAddtoDB(Bitmap bmp, String _id) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, os);
        byte[] bytes = os.toByteArray();
        String bitmap = Base64.encodeToString(bytes, Base64.DEFAULT);
        MainActivity.catDBHelper.addCatsToDB(_id, bitmap);
    }

    public static Bitmap stringToBitmap(String str) {
        byte[] bytesImage = Base64.decode(str, Base64.DEFAULT);
        InputStream inputStream  = new ByteArrayInputStream(bytesImage);
        Bitmap bitmap  = BitmapFactory.decodeStream(inputStream);
        return bitmap;
    }
}
