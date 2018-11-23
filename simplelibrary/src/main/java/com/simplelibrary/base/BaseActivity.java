package com.simplelibrary.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.blankj.utilcode.util.KeyboardUtils;
import com.simplelibrary.mvp.IContract;
import com.simplelibrary.utils.RxAutoDisposeUtils;
import com.uber.autodispose.AutoDisposeConverter;


/**
 * 说明：
 * Created by jjs on 2018/11/23
 */

public abstract class BaseActivity<P extends IContract.IPersenter> extends AppCompatActivity implements IContract.IView {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
    }

    @Override
    protected void onPause() {
        super.onPause();
        KeyboardUtils.hideSoftInput(this);
    }

    protected abstract int getLayoutId();

    @Override
    public void showShortToast(CharSequence charSequence) {

    }

    @Override
    public void showShortToast(int stringsId) {

    }

    @Override
    public void showLoadDialog() {

    }

    @Override
    public void dismissLoadDialog() {

    }

    @Override
    public void showHttpStatusView(int status) {

    }

    @Override
    public void onError(int status, String message) {

    }

    @Override
    public void close() {
        finish();
    }

    @Override
    public <T> AutoDisposeConverter<T> bindAutoDispose() {
        return RxAutoDisposeUtils.bindAutoDispose(this);
    }


}
