package com.library;

import com.library.base.LoginActivity;
import com.simplelibrary.base.BaseApplication;

/**
 * 说明：
 * Created by jjs on 2018/11/21
 */

public class APP extends BaseApplication {
    {
        loginAct = LoginActivity.class;
        Host_Http="http://www.oajxs.com/";
    }

    {
        isDebug = true;
    }
}
