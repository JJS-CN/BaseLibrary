package com.simplelibrary.hook;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by felear on 2018/4/25.
 */

public class BaseProxyHook extends BaseHook implements InvocationHandler {


    public <T extends BaseClassHandler> BaseProxyHook(T classHandler) {
        super(classHandler);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        // mEnable 是否可以hook
        BaseMethodHandler methodHandler;
        if (mEnable && (methodHandler = mClassHandler.getMethod(method.getName())) != null) {
            // hook方法---由于activity跳转需要要给result。这里设定为空时给1
            Object obj = methodHandler.innerHood(mRealObject, method, args);
            return obj == null ? 1 : obj;
        }

        return method.invoke(mRealObject, args);
    }


}
