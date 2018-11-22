package com.library.base;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * 说明：
 * Created by jjs on 2018/11/22
 */

public interface ApiService {
    //登陆接口【1：微信 2：facebook 3:普通登陆 4.注册】
    @POST("frontLogin?loginType=3&phoneCode=123456")
    @FormUrlEncoded
    Observable<BaseEntity> login(
            @Field("phoneNumber") String phoneNumber);
}
