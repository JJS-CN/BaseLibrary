package com.library.base;

import com.simplelibrary.http.BaseObserver;
import com.simplelibrary.mvp.IContract;

/**
 * 说明：实际是对泛型的进一步限制
 * Created by jjs on 2018/11/22
 */

public abstract class RxObserver<T extends BaseEntity> extends BaseObserver<T> {

    public RxObserver(IContract.IView mView) {
        super(mView);
    }

}
