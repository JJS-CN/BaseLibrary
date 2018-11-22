package com.simplelibrary.base;

import android.app.Application;
import android.content.ComponentName;
import android.os.StrictMode;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.simplelibrary.hook.HookFactory;

/**
 * 说明：
 * Created by jjs on 2018/11/21
 */

public class BaseApplication extends Application {
    public boolean isDebug = true;
    public static Class loginAct;

    @Override
    public void onCreate() {
        super.onCreate();
        if (isDebug) {
            //开启严苛模式，系统将在运行时严格的对io等操作进行检查。所以只在开发模式开启
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
        }
        Utils.init(this);
        LogUtils.getConfig().setLogSwitch(isDebug);
        if (loginAct != null) {
            // hook 登录跳转
            ComponentName componentName = new ComponentName(getPackageName(), loginAct.getName());
            HookFactory.hookIActivityManager(Thread.currentThread().getContextClassLoader()
                    , componentName);
        }
    }
}
