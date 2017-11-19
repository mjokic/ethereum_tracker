package com.wifi.ethereumtracker.ext;

import com.google.gson.TypeAdapterFactory;
import com.ryanharter.auto.value.gson.GsonTypeAdapterFactory;

@GsonTypeAdapterFactory
public abstract class RetrofitGsonTypeAdapterFactory implements TypeAdapterFactory {

    public static TypeAdapterFactory create(){
        return new AutoValueGson_RetrofitGsonTypeAdapterFactory();
    }

}