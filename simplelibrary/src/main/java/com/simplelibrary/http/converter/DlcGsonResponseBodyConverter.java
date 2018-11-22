package com.simplelibrary.http.converter;


import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.simplelibrary.http.IBaseEntity;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Converter;

final class DlcGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;
    private final Type typeOfT;
    private static final String TAG = "DlcGsonResponseBodyConv";

    DlcGsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter, Type typeOfT) {
        this.gson = gson;
        this.adapter = adapter;
        this.typeOfT = typeOfT;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        JsonReader jsonReader = gson.newJsonReader(value.charStream());
        try {
            return adapter.read(jsonReader);
        } catch (final JsonParseException e) {
            IBaseEntity entity = new IBaseEntity() {
                @Override
                public int code() {
                    return -1;
                }

                @Override
                public boolean isSuccess() {
                    return false;
                }

                @Override
                public String message() {
                    return "json解析失败";
                }

                @Override
                public List<?> getList() {
                    return null;
                }
            };
            return (T) entity;
        } finally {
            value.close();
        }
    }
}