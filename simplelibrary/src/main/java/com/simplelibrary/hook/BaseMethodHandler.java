package com.simplelibrary.hook;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by felear on 2018/4/25.
 */

public abstract class BaseMethodHandler {

    protected abstract void beforeHood(Object realObject, Method method, Object[] args, OnBeforeListener listener);

    protected abstract void afterHood(Object realObject, Method method, Object[] args);

    public Object innerHood(final Object realObject, final Method method, final Object[] args) throws Throwable {
        Object result = null;
        // 返回true时拦截hook
        beforeHood(realObject, method, args, new OnBeforeListener() {
            @Override
            public void onBefore(boolean hasIntercept) {
                if (!hasIntercept) {
                    try {
                        method.invoke(realObject, args);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    afterHood(realObject, method, args);
                }
            }
        });

        return result;
    }

    protected interface OnBeforeListener {

        void onBefore(boolean hasIntercept);
    }

}
