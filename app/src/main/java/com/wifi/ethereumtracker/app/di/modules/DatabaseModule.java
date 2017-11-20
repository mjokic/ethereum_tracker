package com.wifi.ethereumtracker.app.di.modules;

import android.content.Context;

import com.squareup.sqlbrite2.BriteDatabase;
import com.squareup.sqlbrite2.SqlBrite;
import com.wifi.ethereumtracker.ext.MyDbHelper;

import dagger.Module;
import dagger.Provides;
import io.reactivex.schedulers.Schedulers;

@Module
public class DatabaseModule {

    private final Context context;

    public DatabaseModule(Context context) {
        this.context = context;
    }

    @Provides
    MyDbHelper providesMyDbHelper() {
        return new MyDbHelper(context);
    }

    @Provides
    BriteDatabase providesBriteDatabase(MyDbHelper myDbHelper) {
        SqlBrite sqlBrite = new SqlBrite.Builder().build();
        return sqlBrite.wrapDatabaseHelper(myDbHelper, Schedulers.io());
    }

}
