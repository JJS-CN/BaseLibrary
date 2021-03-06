package com.simplelibrary.http.interceptor;

import java.io.IOException;
import java.net.URLEncoder;

import okhttp3.Request;

/**
 * 说明：固定参数拼接在url中，请求时会自动处理
 * Created by jjs on 2018/7/2.
 */

public class FixedInterceptor extends BaseInterceptor {
    @Override
    protected Request _intercept(Request request) throws IOException {
        if (canInjectIntoBody(request)) {
            String[] urls = request.url().toString().split("\\?");
            if (urls.length >= 2) {
                String[] params = urls[1].split("&");
                for (int i = 0; i < params.length; i++) {
                    String[] keyValue = params[i].split("=");
                    this.addFieldParam(URLEncoder.encode(keyValue[0], "UTF-8"), URLEncoder.encode(keyValue[1], "UTF-8"));
                }
            }
            request = request.newBuilder().url(urls[0]).build();
        }
        return request;
    }


}
