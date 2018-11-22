package com.simplelibrary.http.converter;


import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.io.IOException;
import java.lang.reflect.Type;

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
      /*  String resp = value.string();
        StringReader reader = new StringReader(resp);
        JsonReader jsonReader = gson.newJsonReader(reader);
        jsonReader.setLenient(true);
        try {
            T t = adapter.read(jsonReader);
            if (t instanceof BaseResp) {
                try {
                    Field dataField = t.getClass().getDeclaredField("data");
                    dataField.setAccessible(true);
                    if (dataField.get(t) == null) {
                        // throw new ConnectException("服务器异常");
                    }
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    Log.e(TAG, "convert: " + e);
                }
            }
            return adapter.read(jsonReader);
        } catch (JsonParseException pe) {
            // 容错，兼容后台data:[]（只支持一层）
            JsonElement jEle = new JsonParser().parse(resp);
            JsonObject jObj = jEle.getAsJsonObject();
            if (jObj.has("data") && jObj.get("data").isJsonArray() && jObj.get("data").getAsJsonArray().size() == 0) {
                jObj.remove("data");
                jObj.add("data", null);
            }
            T t = null;
            try {
                t = gson.fromJson(jObj.toString(), typeOfT);
            } catch (Exception e) {
                Log.e(TAG, "convert: " + e);
                BaseResp baseResp = new BaseResp();
                baseResp.code = -199;
                baseResp.msg = "json解析失败";
                t = (T) baseResp;
            }

            return t;
        } finally {
            value.close();
        }
*/
        return null;
    }
}