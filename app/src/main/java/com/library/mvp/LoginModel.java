package com.library.mvp;

import com.library.base.ApiClient;
import com.library.base.BaseEntity;
import com.simplelibrary.mvp.BaseModel;

import io.reactivex.Observable;

/**
 * 说明：
 * Created by jjs on 2018/11/27
 */

public class LoginModel extends BaseModel<LoginPersenter> implements LoginContract.Model {


    public LoginModel(LoginPersenter persenter) {
        super(persenter);
    }

    @Override
    public Observable<BaseEntity> login(String phone, String sms) {
        return ApiClient.getApi().login(phone);
    }
}
