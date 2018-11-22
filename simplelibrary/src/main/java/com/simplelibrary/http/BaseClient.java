package com.simplelibrary.http;

import com.blankj.utilcode.util.Utils;
import com.simplelibrary.base.BaseApplication;
import com.simplelibrary.http.adapter.DlcRxJavaFactory;
import com.simplelibrary.http.converter.DlcGsonConverterFactory;
import com.simplelibrary.http.cookie.NovateCookieManger;
import com.simplelibrary.http.interceptor.FixedInterceptor;
import com.simplelibrary.http.interceptor.LoggerInterceptor;
import com.simplelibrary.http.interceptor.TokenInterceptor;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * 说明：client,由于ApiService每个app都不相同，所以需要子类继承父类，传入apiService类；
 * 并实现单例；
 * Created by jjs on 2018/7/2.
 */

public abstract class BaseClient<T> {

    private T api;


    private OkHttpClient buildOkHttp() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new TokenInterceptor());
        builder.addInterceptor(new FixedInterceptor());
        builder.addInterceptor(new LoggerInterceptor());
        builder.cookieJar(new NovateCookieManger(Utils.getApp()));
        return builder.build();
    }

    /**
     * 获取对应的Service
     */
    public T create(Class<T> service) {
        if (api == null) {
            api = new Retrofit.Builder()
                    .baseUrl(BaseApplication.Host_Http)
                    .client(buildOkHttp())
                    .addCallAdapterFactory(DlcRxJavaFactory.getInstance())
                    .addConverterFactory(DlcGsonConverterFactory.create())
                    .build().create(service);
        }
        return api;
    }

}
