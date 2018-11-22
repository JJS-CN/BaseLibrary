package com.simplelibrary.http;

import java.util.List;

/**
 * 说明：
 * Created by jjs on 2018/11/22
 */

public interface IBaseEntity {
    //服务器请求返回的状态码
    int code();

    //判断是否请求成功。服务器都会设定一个统一成功码
    boolean isSuccess();

    //错误内容
    String message();

    //获取列表
    List<?> getList();
}
