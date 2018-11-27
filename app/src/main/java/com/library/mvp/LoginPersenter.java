package com.library.mvp;

import com.library.base.BaseEntity;
import com.library.base.RxObserver;
import com.simplelibrary.mvp.BasePersenter;

/**
 * 说明：
 * Created by jjs on 2018/11/27
 */

public class LoginPersenter extends BasePersenter<LoginContract.View, LoginModel> implements LoginContract.Persenter {


    LoginPersenter(LoginContract.View view) {
        super(view);
    }

    @Override
    protected LoginModel createModel() {
        return new LoginModel(this);
    }

    @Override
    public void login(String phone, String sms) {
        mModel.login(phone, sms)
                .as(bindAutoDispose())
                .subscribe(new RxObserver<BaseEntity>(mView) {
                    @Override
                    protected void onSuccess(BaseEntity data) {
                        mView.isLogin();
                    }
                });
    }
}
