package com.simplelibrary;

import android.support.annotation.AnimRes;

import com.chad.library.adapter.base.BaseViewHolder;
import com.simplelibrary.dialog.BaseDialog;
import com.simplelibrary.widget.IHttpStatus;

/**
 * 说明：
 * Created by jjs on 2018/11/22
 */

public class BaseConst {


    public static final class Default {
        //是否开启debug模式---控制关键性log输出;接口请求地址
        public static boolean isDebug = false;
        //登陆activity
        public static Class loginAct;
        //服务器路径，和图片加载路径
        public static String Host_Http = "";
        public static String Host_Photo = "";
        //开启debug，优先使用此host，为空时再选用正式服务器host
        public static String Host_Http_Debug = "";
        public static String Host_Photo_Debug = "";

        /*** activity相关参数 */
        //状态栏图标颜色
        public static boolean Default_StatusBarDarkFont = false;
        //重复点击间隔---毫秒
        public static int mClicksInspectMillis = 200;

        //左滑相关参数 windowIsTranslucent
        public static float mLeftMoveTrigger = 0.1f;//屏幕左侧0.1f触发左滑手势，根据屏幕宽度动态计算
        public static float mLeftMoveFinish = 0.25f;//屏幕左侧0.4f触发手势finish，根据屏幕宽度动态计算
        public static int mActivityBackground = 0xFFF5F5F5;//设置DecorView的背景色
        public static boolean hasResetActivityBackground = true;
        //动画相关
        @AnimRes
        public static int mActivityAnimOpen = R.anim.anim_activity_open;
        @AnimRes
        public static int mActivityAnimClose = R.anim.anim_activity_close;

        //提示
        public static String mDoubleExitToast = "再按一次退出程序";
        public static BaseDialog mLoadingDilag = new BaseDialog()
                .setLayoutId(R.layout.dialog_base_loading)
                .setCustomListener(new BaseDialog.OnCustomListener() {
                    @Override
                    public void onCustom(BaseViewHolder holder, BaseDialog dialog) {

                    }
                });

        public static IHttpStatus mBaseHttpStatus = new IHttpStatus() {
            @Override
            public int LoadingLayout() {
                return 0;
            }

            @Override
            public int NetWorkErrorLayout() {
                return 0;
            }

            @Override
            public int ErrorLayout() {
                return 0;
            }

            @Override
            public int NothingLayout() {
                return 0;
            }

        };
    }

    public static final class HttpStatus {
        public static final int Status_SUCCESS = 0;//加载成功
        public static final int Status_LOADING = 1;//加载中
        public static final int Status_NOTHING = 2;//无数据
        public static final int Status_ERROR = 3;//错误
        public static final int Status_NETWORK = 4;//网络异常
        public static final int Status_DEBUG = 5;//debug情况下，展示具体异常信息

    }
}
