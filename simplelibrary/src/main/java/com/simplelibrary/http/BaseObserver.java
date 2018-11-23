package com.simplelibrary.http;

import com.simplelibrary.Const;
import com.simplelibrary.base.BaseApplication;
import com.simplelibrary.mvp.IContract;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;

/**
 * 说明：接受消息回调---子类实现，并进一步限定泛型的范围
 * 注意：在onComplete和onError之间可能只调用其中一个，可能全部都调用，所以注意方法的书写
 * 这里要把dialog关闭都写上
 * Created by aa on 2017/9/20.
 */

public abstract class BaseObserver<T extends IBaseEntity> implements Observer<T> {
    private IContract.IView mView;

    public BaseObserver(IContract.IView mView) {
        this.mView = mView;
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        showLoadDialog();
        updateStatusView(Const.Http.Status_LOADING);
    }

    @Override
    public void onNext(@NonNull T t) {
        if (t.isSuccess()) {
            onSuccess(t);
            if (t.getList() != null && t.getList().size() == 0) {
                updateStatusView(Const.Http.Status_NOTHING);
            } else {
                updateStatusView(Const.Http.Status_SUCCESS);
            }

        } else {
            updateStatusView(Const.Http.Status_ERROR);
            onError(t.code(), t.message());
        }
    }

    @Override
    public void onError(@NonNull Throwable e) {
        if (BaseApplication.isDebug) {
            e.printStackTrace();
        }
        int code = 1;

        if (e instanceof HttpException) {
            code = ((HttpException) e).code(); // 状态码 404 500 502
            updateStatusView(Const.Http.Status_NETWORK);
        } else {
            updateStatusView(Const.Http.Status_ERROR);
        }
        onError(code, "网络请求失败" + code);
    }

    protected void onError(int status, String msg) {
        dismissLoadDialog();
        if (hasToast()) {
            if (mView != null) {
                mView.showShortToast(msg);
            }
        }
    }

    @Override
    public void onComplete() {
        dismissLoadDialog();
    }

    protected void showLoadDialog() {
        if (mView != null && hasLoading()) {
            mView.showLoadDialog();
        }
    }

    protected void dismissLoadDialog() {
        if (mView != null) {
            mView.dismissLoadDialog();
        }
    }

    protected abstract void onSuccess(T data);


    private void updateStatusView(int status) {
        if (mView != null) {
            mView.showHttpStatusView(status);
        }
    }

    /**
     * 外部重写此方法，来控制是否展示错误提示；
     */
    protected boolean hasToast() {
        return true;
    }

    protected boolean hasLoading() {
        return true;
    }
}
