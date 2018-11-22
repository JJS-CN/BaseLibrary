package com.simplelibrary.hook;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.SPUtils;
import com.simplelibrary.activity.PermissionFailActivity;
import com.simplelibrary.annotation.NeedLogin;
import com.simplelibrary.annotation.NeedPermission;
import com.simplelibrary.sp.BaseUserSp;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by felear on 2018/4/25.
 */

public class IActivityManagerHandler extends BaseClassHandler {

    private static final String TAG = "IActivityManagerHandler";
    private ComponentName mComponentName;

    //外部传入APP的login页面
    public void init(ComponentName componentName) {
        mComponentName = componentName;
    }

    // 传入classloader
    @Override
    public void hook(BaseHook baseHook, ClassLoader classLoader) throws Throwable {

        if (!(baseHook instanceof BaseProxyHook)) {
            Log.e(TAG, "BaseProxyHook");
            return;
        }

        BaseProxyHook baseProxyHook = (BaseProxyHook) baseHook;
        Field gDefault = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Class<?> amClass = Class.forName("android.app.ActivityManager");
            gDefault = amClass.getDeclaredField("IActivityManagerSingleton");
        } else {
            Class<?> amClass = Class.forName("android.app.ActivityManagerNative");
            gDefault = amClass.getDeclaredField("gDefault");
        }
        gDefault.setAccessible(true);
        Object iAmSingleton = gDefault.get(null);
        Log.e(TAG, "initHook: " + iAmSingleton);

        Class<?> singleTonClass = Class.forName("android.util.Singleton");
        Field mInstance = singleTonClass.getDeclaredField("mInstance");
        mInstance.setAccessible(true);

        Object ams = mInstance.get(iAmSingleton);

        // 获取IActivityManager类
        Class<?> iActivityManagerClass = Class.forName("android.app.IActivityManager");
        baseProxyHook.setRealObject(ams);
        Object mAms = Proxy.newProxyInstance(classLoader
                , new Class[]{iActivityManagerClass}
                , baseProxyHook);

        mInstance.set(iAmSingleton, mAms);

        Log.e(TAG, "initHook: " + ams);


    }


    @Override
    protected void initMethod() {

        addMethod("startActivity", new startActivityHandler());
    }

    class startActivityHandler extends BaseMethodHandler {

        @Override
        protected void beforeHood(Object realObject, Method method, final Object[] args, final OnBeforeListener listener) {
            // 找出intent

            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof Intent) {
                    final Intent arg = (Intent) args[i];
                    // 判断是否在名单中
                    ComponentName component = arg.getComponent();
                    //通过Uri调取外部地址时获取不到包信息，所以判断为null时return
                    if (component == null) {
                        listener.onBefore(false);
                        return;
                    }
                    try {
                        Class<?> tClass = Class.forName(component.getClassName());
                        final NeedLogin annLogin = tClass.getAnnotation(NeedLogin.class);
                        NeedPermission annPermission = tClass.getAnnotation(NeedPermission.class);
                        if (annLogin == null && annPermission == null) {
                            //不做任何操作
                            listener.onBefore(false);
                        } else if (annPermission != null) {
                            final int finalI1 = i;
                            //先请求权限，再判断是否登陆
                            PermissionUtils.permission(annPermission.value())
                                    .callback(new PermissionUtils.SimpleCallback() {
                                        @Override
                                        public void onGranted() {
                                            if (annLogin != null) {
                                                if (TextUtils.isEmpty(SPUtils.getInstance(BaseUserSp.KEY_User).getString(BaseUserSp.KEY_User))) {
                                                    //如果未登陆，替换目标activity
                                                    Intent intent = new Intent();
                                                    intent.putExtra("intent", arg);
                                                    intent.setComponent(mComponentName);
                                                    args[finalI1] = intent;
                                                }
                                            }
                                            listener.onBefore(false);
                                        }

                                        @Override
                                        public void onDenied() {
                                            //todo 授权失败，跳转透明activity。模拟dialog弹窗引导用户去app设置页给予权限
                                            ComponentName componentName = new ComponentName(AppUtils.getAppPackageName(), PermissionFailActivity.class.getName());
                                            Intent intent = new Intent();
                                            intent.putExtra("intent", arg);
                                            intent.setComponent(componentName);
                                            args[finalI1] = intent;
                                            listener.onBefore(false);
                                        }
                                    }).request();

                        } else {
                            //判断是否需要登陆
                            if (TextUtils.isEmpty(SPUtils.getInstance(BaseUserSp.KEY_User).getString(BaseUserSp.KEY_User))) {
                                //如果未登陆，替换目标activity
                                Intent intent = new Intent();
                                intent.putExtra("intent", arg);
                                intent.setComponent(mComponentName);
                                args[i] = intent;
                            }
                            listener.onBefore(false);
                        }

                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        @Override
        protected void afterHood(Object realObject, Method method, Object[] args) {

        }
    }
}
