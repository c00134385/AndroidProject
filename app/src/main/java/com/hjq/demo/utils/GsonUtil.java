package com.hjq.demo.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtil {
    public static GsonBuilder getGsonBuilder() {
        return new GsonBuilder().serializeNulls()
                .setDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    public static Gson getGson() {
        return getGsonBuilder().create();
    }
}
