package com.simplelibrary.base;

import android.app.Application;
import android.content.ComponentName;
import android.os.StrictMode;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.simplelibrary.BaseConst;
import com.simplelibrary.hook.HookFactory;

/**
 * 说明：
 * Created by jjs on 2018/11/21
 */

public abstract class BaseApplication extends Application {

    protected abstract BaseConst initConst();

    @Override
    public void onCreate() {
        super.onCreate();
        BaseConst bConst = initConst();
        if (bConst==null){
            throw  new NullPointerException("Your must init Const, in the Application!!");
        }

        if (BaseConst.Default.isDebug) {
            //开启严苛模式，系统将在运行时严格的对io等操作进行检查。所以只在开发模式开启
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
        }
        Utils.init(this);
        LogUtils.getConfig().setLogSwitch(BaseConst.Default.isDebug);
        if (BaseConst.Default.loginAct != null) {
            // hook 登录跳转
            ComponentName componentName = new ComponentName(getPackageName(), BaseConst.Default.loginAct.getName());
            HookFactory.hookIActivityManager(Thread.currentThread().getContextClassLoader()
                    , componentName);
        }
    }
}
