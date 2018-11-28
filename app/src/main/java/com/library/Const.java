package com.library;

import com.library.base.LoginActivity;
import com.simplelibrary.BaseConst;

/**
 * 说明：由于参数需要在类初始化时进行更换，所以需要在applicaiton中执行一次类的创建
 * Created by jjs on 2018/11/24
 */

public class Const extends BaseConst {
    static {
        Default.isDebug = true;
        Default.Host_Http = "http://www.oajxs.com/";
        Default.loginAct = LoginActivity.class;
    }
}
