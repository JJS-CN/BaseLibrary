package com.library.mvp;

import com.library.base.BaseEntity;
import com.simplelibrary.mvp.IContract;

import io.reactivex.Observable;

/**
 * 说明：
 * Created by jjs on 2018/11/27
 */

public interface LoginContract {
    public interface Persenter extends IContract.IPersenter {
        void login(String phone,String sms);
    }

    public interface View extends IContract.IView {
        void isLogin();
    }

    public interface Model extends IContract.IModel {
        Observable<BaseEntity> login(String phone, String sms);
    }
}
