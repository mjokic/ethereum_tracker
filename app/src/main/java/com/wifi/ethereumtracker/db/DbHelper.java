package com.wifi.ethereumtracker.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wifi.ethereumtracker.model.ProfileOld;

import java.util.List;

public class DbHelper {

    final SQLiteDatabase db;

    public DbHelper(Context context){
        this.db = context.openOrCreateDatabase("db_ether", context.MODE_PRIVATE, null);
        createTable();
    }

    private void createTable(){
        this.db.execSQL("CREATE TABLE IF NOT EXISTS info(data BLOB);");
    }

    public void saveProfiles(List<ProfileOld> profileOlds){
        ContentValues contentValues = new ContentValues();
        contentValues.put("data", new Gson().toJson(profileOlds).getBytes());

        this.db.insert("info", "data", contentValues);

        closeDbConn();
    }

    public List<ProfileOld> getProfiles(){
        Cursor cursor = db.rawQuery("SELECT * FROM info", null);
        cursor.moveToLast();

        byte[] blob = cursor.getBlob(cursor.getColumnIndex("data"));
        cursor.close();

        String json = new String(blob);

        List<ProfileOld> profileOlds = new Gson().fromJson(json, new TypeToken<List<ProfileOld>>(){}.getType());

        closeDbConn();

        return profileOlds;
    }

    private void closeDbConn(){
        this.db.close();
    }

}
