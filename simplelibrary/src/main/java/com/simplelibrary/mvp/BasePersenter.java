package com.simplelibrary.mvp;

/**
 * 说明：
 * Created by jjs on 2018/11/22
 */

public class BasePersenter<T extends IMvpContract.IView> implements IMvpContract.IPersenter {
    protected T mView;

    @Override
    public void subscribe() {
    }

    @Override
    public void unSubscribe() {
        mView = null;
    }
}
