package com.example.catimages;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CatDBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "CatImagesDB";
    private static final String TABLE_NAME = "catimages";
    private static final String IMAGE_ID = "id";
    private static final String IMAGE_URL = "url";

    public CatDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + IMAGE_ID + " TEXT,"
                + IMAGE_URL + " TEXT NOT NULL UNIQUE"+ ")";
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // Drop older table if exist
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        // Create tables again
        onCreate(sqLiteDatabase);
    }

    public void addCatsToDB(String _id, String url)
    {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            //Create a new map of values, where column names are the keys
            ContentValues cValues = new ContentValues();
            cValues.put(IMAGE_ID, _id);
            cValues.put(IMAGE_URL, url);
            // Insert the new row, returning the primary key value of the new row
            try {
                long newRowId = db.insert(TABLE_NAME, null, cValues);
            }catch (Exception eg){
            }
            db.close();
        }catch (Exception ex)
        {

        }
    }

    public List<LocalCats> getAllCatImages()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<LocalCats> arrayList = new ArrayList<LocalCats>();
        String query = "SELECT * FROM "+ TABLE_NAME;
        Cursor cursor = db.rawQuery(query,null);
        try{
        while (cursor.moveToNext()){
            String id = cursor.getString(0);   //0 is the number of id column in your database table
            Bitmap url = Utilities.stringToBitmap(cursor.getString(1));
            LocalCats cats1 = new LocalCats(id,url);
            arrayList.add(cats1);
        }}catch (Exception ex){}
        return arrayList;
    }
}
