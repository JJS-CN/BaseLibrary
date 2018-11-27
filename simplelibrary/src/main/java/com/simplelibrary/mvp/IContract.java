package com.simplelibrary.mvp;

import android.support.annotation.StringRes;

import com.uber.autodispose.AutoDisposeConverter;

/**
 * 说明：
 * Created by jjs on 2018/11/22
 */

public interface IContract {
    interface IView {

        void showShortToast(CharSequence charSequence);

        void showShortToast(@StringRes int stringsId);

        void showLoadDialog();

        void dismissLoadDialog();

        void showHttpStatusView(int status);

        void onError(int status, String message);

        void close();

        <T> AutoDisposeConverter<T> bindAutoDispose();
    }

    interface IPersenter {
        void subscribe();

        void unSubscribe();

        <T> AutoDisposeConverter<T> bindAutoDispose();
    }

    interface IModel {
        void subscribe();

        void unSubscribe();
    }
}
