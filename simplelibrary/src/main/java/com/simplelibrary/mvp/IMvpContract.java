package com.simplelibrary.mvp;

import android.support.annotation.StringRes;

/**
 * 说明：
 * Created by jjs on 2018/11/22
 */

public interface IMvpContract {
    interface IView {

        void showShortToast(CharSequence charSequence);

        void showShortToast(@StringRes int stringsId);

        void showLoadDialog();

        void dismissLoadDialog();

        void showHttpStatusView(int status);
    }

    interface IPersenter {
        void subscribe();

        void unSubscribe();
    }
}
