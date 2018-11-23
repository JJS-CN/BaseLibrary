package com.simplelibrary.utils;


import android.arch.lifecycle.LifecycleOwner;

import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.AutoDisposeConverter;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;



/**
 * 说明：
 * Created by jjs on 2018/11/23
 */

public class RxAutoDisposeUtils {
    private RxAutoDisposeUtils() {
        throw new IllegalStateException("Can't instance the RxAutoDisposeUtils");
    }

    public static <T> AutoDisposeConverter<T> bindAutoDispose(LifecycleOwner lifecycleOwner) {
        return AutoDispose.autoDisposable(
                AndroidLifecycleScopeProvider.from(lifecycleOwner)
        );
    }
}
