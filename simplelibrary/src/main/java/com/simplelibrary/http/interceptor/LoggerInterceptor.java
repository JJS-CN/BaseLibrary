package com.simplelibrary.http.interceptor;

import com.blankj.utilcode.util.LogUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSource;

/**
 * 说明：将token参数添加至Header中
 * Created by jjs on 2018/7/2.
 */

public class LoggerInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        sendLogRequest(request);
        Response response = chain.proceed(request);
        //打印接收参数
        sendLogResponse(response);
        return response;
    }


    /**
     * 开始解析发送参数
     */
    private void sendLogRequest(Request request) throws IOException {
        if (request != null) {
            String body = "";
            if (request.body() != null) {
                Buffer buffer = new Buffer();
                request.body().writeTo(buffer);
                //编码设为UTF-8
                Charset charset = Charset.forName("UTF-8");
                MediaType contentType = request.body().contentType();
                if (contentType != null) {
                    charset = contentType.charset(Charset.forName("UTF-8"));
                }
                body = buffer.readString(charset);
                //如果你出现中文参数乱码情况，请进行URL解码处理！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！
                body = URLEncoder.encode(body, "UTF-8");
            }
            LogUtils.i("发送----" + "method:" + request.method() + "  url:" + request.url() + "  body:" + body);
        }
    }

    /**
     * 开始解析服务器返回参数
     */
    private void sendLogResponse(Response response) throws IOException {
        String rBody = "";
        if (response != null && response.body() != null) {
            BufferedSource source = response.body().source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            Buffer buffer = source.buffer();

            Charset charset = Charset.forName("UTF-8");
            MediaType contentType = response.body().contentType();
            if (contentType != null) {
                try {
                    charset = contentType.charset(Charset.forName("UTF-8"));
                } catch (UnsupportedCharsetException e) {
                    e.printStackTrace();
                }
            }
            rBody = buffer.clone().readString(charset);
        }
        LogUtils.i("接收：" + rBody.toString());
    }
}
