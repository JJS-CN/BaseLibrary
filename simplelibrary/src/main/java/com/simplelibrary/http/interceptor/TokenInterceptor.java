package com.simplelibrary.http.interceptor;

import android.text.TextUtils;

import com.blankj.utilcode.util.SPUtils;
import com.simplelibrary.sp.BaseUserSp;

import java.io.IOException;

import okhttp3.Request;

/**
 * 说明：将token参数添加至Header中
 * Created by jjs on 2018/7/2.
 */

public class TokenInterceptor extends BaseInterceptor {
    @Override
    protected Request _intercept(Request request) throws IOException {
        String token = SPUtils.getInstance(BaseUserSp.KEY_User).getString(BaseUserSp.KEY_Token);
        if (!TextUtils.isEmpty(token)) {
            Request.Builder builder1 = request
                    .newBuilder();
            builder1.addHeader("Authorization", token);
            request = builder1.build();
        }
        return request;
    }
}
