package com.wifi.ethereumtracker.ext;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import timber.log.Timber;

public class MyDbHelper extends SQLiteOpenHelper {

    public MyDbHelper(Context context){
        super(context, "db_ether", null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Timber.d("MA DATABASE IS NOT CREATED! YEYT!");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
