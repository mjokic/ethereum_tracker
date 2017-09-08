package com.wifi.ethereumtracker.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wifi.ethereumtracker.model.Profile;

import java.util.List;

public class DbHelper {

    SQLiteDatabase db;

    public DbHelper(Context context){
        this.db = context.openOrCreateDatabase("db_ether", context.MODE_PRIVATE, null);
        createTable();
    }

    private void createTable(){
        this.db.execSQL("CREATE TABLE IF NOT EXISTS info(data BLOB);");
    }

    public void saveProfiles(List<Profile> profiles){
        ContentValues contentValues = new ContentValues();
        contentValues.put("data", new Gson().toJson(profiles).getBytes());

        this.db.insert("info", "data", contentValues);

        closeDbConn();
    }

    public List<Profile> getProfiles(){
        Cursor cursor = db.rawQuery("SELECT * FROM info", null);
        cursor.moveToLast();

        byte[] blob = cursor.getBlob(cursor.getColumnIndex("data"));
        cursor.close();

        String json = new String(blob);

        List<Profile> profiles = new Gson().fromJson(json, new TypeToken<List<Profile>>(){}.getType());

        closeDbConn();

        return profiles;
    }

    private void closeDbConn(){
        this.db.close();
    }

}
