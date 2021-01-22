package com.example.catimages;

import android.graphics.Bitmap;

public class LocalCats {

    private String id;
    private Bitmap bitmap;

    public LocalCats(String id, Bitmap bitmap)
    {
        this.id= id;
        this.bitmap= bitmap;
    }

    public String getId() {
        return id;
    }

    public Bitmap getBitmapUrl() {
        return bitmap;
    }
}
