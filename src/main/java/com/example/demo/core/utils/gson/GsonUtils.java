package com.example.demo.utils.gson;

import com.example.demo.utils.gson.adapters.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDateTime;

public class GsonUtils {
    public static Gson getGson() {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .excludeFieldsWithoutExposeAnnotation()
                .create();
    }
}
