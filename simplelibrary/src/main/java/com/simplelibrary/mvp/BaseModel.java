package com.simplelibrary.mvp;

/**
 * 说明：
 * Created by jjs on 2018/11/27
 */

public class BaseModel<P extends BasePersenter> implements IContract.IModel {
    protected P mPersenter;



    public BaseModel(P persenter) {
        mPersenter = persenter;
    }

    @Override
    public void subscribe() {
        if (mPersenter != null) {
            mPersenter.subscribe();
        }
    }

    @Override
    public void unSubscribe() {
        mPersenter = null;
        if (mPersenter != null) {
            mPersenter.unSubscribe();
            mPersenter = null;
        }
    }
}
