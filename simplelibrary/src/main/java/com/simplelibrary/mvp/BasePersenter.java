package com.simplelibrary.mvp;

import com.uber.autodispose.AutoDisposeConverter;

/**
 * 说明：
 * Created by jjs on 2018/11/22
 */

public abstract class BasePersenter<V extends IContract.IView, M extends IContract.IModel> implements IContract.IPersenter {
    public V mView;
    public M mModel;

    public BasePersenter(V view) {
        this.mView = view;
        this.mModel = createModel();
    }

    protected abstract M createModel();

    @Override
    public void subscribe() {
        if (mModel != null) {
            mModel.subscribe();
        }
    }

    @Override
    public void unSubscribe() {
        mView = null;
        if (mModel != null) {
            mModel.unSubscribe();
            mModel = null;
        }
    }

    @Override
    public <T> AutoDisposeConverter<T> bindAutoDispose() {
        if (mView == null) {
            return null;
        } else {
            return mView.bindAutoDispose();
        }
    }
}
