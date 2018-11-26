package com.library;

import com.simplelibrary.BaseConst;
import com.simplelibrary.base.BaseApplication;

/**
 * 说明：
 * Created by jjs on 2018/11/21
 */

public class APP extends BaseApplication {
    @Override
    public BaseConst initConst() {
        return new Const();
    }
}