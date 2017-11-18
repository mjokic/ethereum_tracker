package com.wifi.ethereumtracker.ext;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.wifi.ethereumtracker.app.model.SourceModel;

import timber.log.Timber;

public class MyDbHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "ether.db";
    public static final int DB_VERSION = 1;

    public MyDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        Timber.d("CALLED?!");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Timber.d("Creating table Sources");
        db.execSQL(SourceModel.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
