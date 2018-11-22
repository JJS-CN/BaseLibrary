package com.simplelibrary.http.adapter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;

import io.reactivex.annotations.Nullable;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Created by felear on 2018/6/22.
 */

public class DlcRxJavaFactory extends CallAdapter.Factory {
    private static final DlcRxJavaFactory ourInstance = new DlcRxJavaFactory();
    private final RxJava2CallAdapterFactory mFactory;

    public static DlcRxJavaFactory getInstance() {
        return ourInstance;
    }

    private DlcRxJavaFactory() {

        mFactory = RxJava2CallAdapterFactory.create();

    }

    @Nullable
    @Override
    public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        CallAdapter<?, ?> callAdapter = mFactory.get(returnType, annotations, retrofit);
        return (CallAdapter<?, ?>) Proxy.newProxyInstance(CallAdapter.class.getClassLoader()
                , new Class[]{CallAdapter.class}, new DlcRxJavaAdapter(callAdapter));
    }


}
