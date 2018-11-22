package com.simplelibrary.http.adapter;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.CallAdapter;

/**
 * Created by felear on 2018/6/22.
 */

public class DlcRxJavaAdapter implements InvocationHandler {

    private final CallAdapter<?, ?> mCallAdapter;

    public DlcRxJavaAdapter(CallAdapter<?, ?> callAdapter) {
        mCallAdapter = callAdapter;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object invoke = method.invoke(mCallAdapter, args);
        if (invoke instanceof Observable) {
            Observable observable = (Observable) invoke;
            invoke = observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        }
        return invoke;
    }
}
