package com.simplelibrary.sp;

import android.text.TextUtils;

import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 说明：用户数据本地管理，需要用户继承此类，并填写对应泛型
 * Created by jjs on 2018/11/21
 */

public abstract class BaseUserSp<T> {
    private SPUtils mSPUtils;
    public static final String KEY_User = "BaseUserSp";
    public static final String KEY_Token = "Token";
    private static final String KEY_Account = "Account";

    public BaseUserSp() {
        mSPUtils = SPUtils.getInstance(KEY_User);
    }

    public void setUserEntity(T t) {
        mSPUtils.put(KEY_User, new Gson().toJson(t));
    }

    public T getUserEntity() {
        String user = mSPUtils.getString(KEY_User);
        if (TextUtils.isEmpty(user)) {
            return null;
        }
        return new Gson().fromJson(user, new TypeToken<T>() {
        }.getType());
    }

    public void setToken(String token) {
        mSPUtils.put(KEY_Token, token);
    }

    public String getToken() {
        return mSPUtils.getString(KEY_Token);
    }

    public void setAccount(String account) {
        mSPUtils.put(KEY_Account, account);
    }

    public String getAccount() {
        return mSPUtils.getString(KEY_Account);
    }

    public void clear() {
        mSPUtils.clear(true);
    }
}
