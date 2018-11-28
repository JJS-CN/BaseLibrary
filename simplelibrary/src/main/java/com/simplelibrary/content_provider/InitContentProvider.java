package com.simplelibrary.content_provider;

import android.content.ComponentName;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.simplelibrary.BaseConst;
import com.simplelibrary.hook.HookFactory;

import java.io.IOException;
import java.util.Enumeration;

import dalvik.system.DexFile;

/**
 * 说明：框架初始化
 * Created by jjs on 2018/11/27
 */

public class InitContentProvider extends ContentProvider {
    @Override
    public boolean onCreate() {
        Utils.init(getContext());
        getClassName2(AppUtils.getAppPackageName());
        if (BaseConst.Default.isDebug) {
            //开启严苛模式，系统将在运行时严格的对io等操作进行检查。所以只在开发模式开启
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
        }

        LogUtils.getConfig().setLogSwitch(BaseConst.Default.isDebug);
        if (BaseConst.Default.loginAct != null) {
            // hook 登录跳转
            ComponentName componentName = new ComponentName(AppUtils.getAppPackageName(), BaseConst.Default.loginAct.getName());
            HookFactory.hookIActivityManager(Thread.currentThread().getContextClassLoader()
                    , componentName);
        }
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    public void getClassName2(String packageName) {
        try {
            DexFile df = new DexFile(getContext().getPackageCodePath());//通过DexFile查找当前的APK中可执行文件
            Enumeration<String> enumeration = df.entries();//获取df中的元素  这里包含了所有可执行的类名 该类名包含了包名+类名的方式
            while (enumeration.hasMoreElements()) {//遍历
                String className = (String) enumeration.nextElement();

                if (className.contains(packageName)) {//在当前所有可执行的类里面查找包含有该包名的所有类
                    try {
                        if (BaseConst.class.isAssignableFrom(Class.forName(className))) {
                            Log.e("newInstance", "init:" + className);
                            Class.forName(className).newInstance();
                            return;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
