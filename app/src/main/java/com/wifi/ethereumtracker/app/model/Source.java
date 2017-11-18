package com.wifi.ethereumtracker.app.model;

import android.support.annotation.NonNull;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.squareup.sqldelight.ColumnAdapter;

import java.util.ArrayList;
import java.util.List;

@AutoValue
public abstract class Source implements SourceModel {

    private static final ColumnAdapter<List<String>, byte[]> COLUMN_ADAPTER =
            new ColumnAdapter<List<String>, byte[]>() {
                @NonNull
                @Override
                public List<String> decode(byte[] databaseValue) {
                    return new Gson().fromJson(new String(databaseValue),
                            new TypeToken<ArrayList<String>>() {
                            }.getType());
                }

                @Override
                public byte[] encode(@NonNull List<String> value) {
                    return new Gson().toJson(value).getBytes();
                }
            };

    private static final Creator<Source> CREATOR =
            (site, currencies) -> new AutoValue_Source(site, currencies);

    public static final Factory<Source> FACTORY =
            new Factory<>(CREATOR, COLUMN_ADAPTER);

    public static final Mapper<Source> MAPPER =
            new Mapper<>(FACTORY);


    public static TypeAdapter<Source> typeAdapter(Gson gson) {
        return new AutoValue_Source.GsonTypeAdapter(gson);
    }
}
