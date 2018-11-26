package com.simplelibrary.base;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.AnimRes;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.gyf.barlibrary.ImmersionBar;
import com.simplelibrary.BaseConst;
import com.simplelibrary.R;
import com.simplelibrary.dialog.BaseDialog;
import com.simplelibrary.mvp.BasePersenter;
import com.simplelibrary.mvp.IContract;
import com.simplelibrary.utils.RxAutoDisposeUtils;
import com.uber.autodispose.AutoDisposeConverter;

import java.io.Serializable;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * 说明：
 * Created by jjs on 2018/11/23
 */

public abstract class BaseActivity<P extends BasePersenter<IContract.IView>> extends AppCompatActivity implements IContract.IView {

    /*** 两次点击提示退出 */
    private long mStart;
    protected boolean mDoubleClick2Exit = false;//是否需要双击退出拦截提示
    /*** 状态栏相关 */
    protected boolean mTranslucentStatus = false;//透明状态栏
    protected boolean mStatusBarHidden = false;//隐藏状态栏
    @ColorInt
    protected int mStatusBarColor = -1;//状态栏的颜色 int值
    protected boolean hasFitsSystemWindows = true;//是否需要向下挤压
    protected boolean hasStatusBarDarkFont = BaseConst.Default.Default_StatusBarDarkFont;//状态栏的图标是否为 黑颜色
    /*** Touch相关 */
    private long mClicksInspectLastTime;//记录上次点击时间
    protected boolean hasClicksInspect = true; //重复点击拦截
    protected boolean hasEventHideInput = true;//是否在点击事件未被消耗情况关闭软件盘
    protected boolean hasLeftMoveExit = false;//是否需要左滑关闭 由于windowIsTranslucent问题，window无法回收使内存消耗暴增，不建议使用
    protected boolean hasResetActivityBackground = BaseConst.Default.hasResetActivityBackground;
    private float mLeftMoveDownX = -1;//初始按下值

    /*** 动画 */
    @AnimRes
    protected int mActivityAnimOpen = BaseConst.Default.mActivityAnimOpen;
    @AnimRes
    protected int mActivityAnimClose = BaseConst.Default.mActivityAnimClose;

    /***  屏幕宽高  ***/
    private int mWindowWidth = -1;
    private int mWindowHeight = -1;
    /*** 常量 */
    private Unbinder mUnBinder;
    protected P mPersenter;
    private BaseDialog mLoadingDilaog = BaseConst.Default.mLoadingDilag;

    /*** httpStatus */
    private FrameLayout contentParent;
    private SparseArray<View> mStatusArray;
    private SparseArray<View> mMarginStatusArray;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (mStatusBarHidden) {
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());


        if (!mStatusBarHidden) {
            if (mTranslucentStatus) {
                //设置透明色
                mStatusBarColor = 0x00000000;
                hasFitsSystemWindows = false;
            } else {
                //设置具体颜色
                if (mStatusBarColor == -1) {
                    mStatusBarColor = getResources().getColor(R.color.colorPrimary);
                }
            }
            ImmersionBar.with(this)
                    .statusBarColorInt(mStatusBarColor)
                    .statusBarDarkFont(hasStatusBarDarkFont)
                    .fitsSystemWindows(hasFitsSystemWindows)
                    .keyboardEnable(true)
                    .init();
        }
        if (hasLeftMoveExit) {
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            mWindowWidth = dm.widthPixels;
            mWindowHeight = dm.heightPixels;
            if (mDecorView == null) {
                mDecorView = this.getWindow().getDecorView();
            }
        }
        if (hasResetActivityBackground) {
            if (mDecorView == null) {
                mDecorView = this.getWindow().getDecorView();
            }
            mDecorView.setBackgroundColor(BaseConst.Default.mActivityBackground);
        }
        initView();
        loadData();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        mUnBinder = ButterKnife.bind(this);
    }

    protected abstract int getLayoutId();

    protected abstract P createPersenter();

    protected abstract void initView();

    protected abstract void loadData();

    protected void onActivityResultOK(int requestCode, Intent data) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mStart = 0;
    }

    @Override
    protected void onPause() {
        super.onPause();
        KeyboardUtils.hideSoftInput(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImmersionBar.with(this).destroy();
        if (mPersenter != null) {
            mPersenter.unSubscribe();
        }
        if (mUnBinder != null) {
            mUnBinder.unbind();
        }
    }

    /*** 跳转相关 **/
    public void readyGo(Class<?> clazz, String key, String value, int requestCode) {
        Bundle bundle = new Bundle();
        bundle.putString(key, value);
        readyGo(clazz, bundle, requestCode);
    }

    public void readyGo(Class<?> clazz, String key, boolean value, int requestCode) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(key, value);
        readyGo(clazz, bundle, requestCode);
    }

    public void readyGo(Class<?> clazz, String key, double value, int requestCode) {
        Bundle bundle = new Bundle();
        bundle.putDouble(key, value);
        readyGo(clazz, bundle, requestCode);
    }

    public void readyGo(Class<?> clazz, String key, Serializable value, int requestCode) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(key, value);
        readyGo(clazz, bundle, requestCode);
    }

    public void readyGo(Class<?> clazz) {
        readyGo(clazz, null, -1);
    }

    public void readyGo(Class<?> clazz, Bundle bundle) {
        readyGo(clazz, bundle, -1);
    }

    public void readyGo(Class<?> clazz, int requestCode) {
        readyGo(clazz, null, requestCode);
    }

    public void readyGo(Class<?> clazz, Bundle bundle, int requestCode) {
        if (clazz != null) {
            Intent intent = new Intent(this, clazz);
            if (null != bundle) {
                intent.putExtras(bundle);
            }
            if (requestCode == -1) {
                startActivity(intent);
            } else {
                startActivityForResult(intent, requestCode);
            }
        }
    }


    /**************************************  触摸相关  ******************************************/
    private View mDecorView;//用于左滑动画处理

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        /**********  快速重复点击拦截  **********/
        if (hasClicksInspect) {
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                if (System.currentTimeMillis() - mClicksInspectLastTime < BaseConst.Default.mClicksInspectMillis) {
                    Log.e("BaseActivity", "this Event has ignore,because you touch too much! ");
                    return true;
                } else {
                    mClicksInspectLastTime = System.currentTimeMillis();
                }
            }
        }
        /***********  左滑back逻辑  ***********/
        if (hasLeftMoveExit && !mDoubleClick2Exit) {
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                if (ev.getX() <= BaseConst.Default.mLeftMoveTrigger * mWindowWidth) {
                    mLeftMoveDownX = ev.getX();
                }
            } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
                //左滑返回的拖动效果
                if (mLeftMoveDownX != -1) {
                    float moveX = ev.getX() - mLeftMoveDownX;
                    mDecorView.setX(moveX < 0 ? 0 : moveX);
                }
            } else if (ev.getAction() == MotionEvent.ACTION_UP) {
                if (mLeftMoveDownX != -1 && mLeftMoveDownX <= BaseConst.Default.mLeftMoveTrigger * mWindowWidth) {
                    if (ev.getX() - mLeftMoveDownX > BaseConst.Default.mLeftMoveFinish * mWindowWidth) {
                        //执行关闭操作
                        finish();
                    } else {
                        //执行回弹操作
                        ValueAnimator animator = ValueAnimator.ofFloat(mDecorView.getX(), 0);
                        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                mDecorView.setX((Float) animation.getAnimatedValue());
                            }
                        });
                        animator.setDuration(200).start();
                    }
                    mLeftMoveDownX = -1;
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (hasEventHideInput && event.getAction() == MotionEvent.ACTION_DOWN) {
            KeyboardUtils.hideSoftInput(this);
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onBackPressed() {
        if (!mDoubleClick2Exit) {
            finish();
        } else {
            long mend = System.currentTimeMillis();
            if (mend - mStart < 2000) {
                finish();
            } else {
                showShortToast(BaseConst.Default.mDoubleExitToast);
                mStart = mend;
            }
        }
    }


    @Override
    public void showShortToast(CharSequence charSequence) {
        ToastUtils.showShort(charSequence);

    }

    @Override
    public void showShortToast(int stringsId) {
        ToastUtils.showShort(stringsId);
    }

    @Override
    public void showLoadDialog() {
        if (mLoadingDilaog != null) {
            mLoadingDilaog.show(this);
        }
    }

    @Override
    public void dismissLoadDialog() {
        if (mLoadingDilaog != null) {
            mLoadingDilaog.dismiss();
        }
    }

    @Override
    public void showHttpStatusView(int status) {
        //dialog展示时，不需要statusView的加载框
        if (BaseConst.Default.mBaseHttpStatus == null || status == BaseConst.HttpStatus.Status_LOADING && mLoadingDilaog != null && mLoadingDilaog.isVisible()) {
            return;
        }
        if (mDecorView == null) {
            mDecorView = getWindow().getDecorView();
        }
        if (contentParent == null) {
            contentParent = mDecorView.findViewById(android.R.id.content);
        }
        if (contentParent != null) {
            removeStatusView();
            View view = getStatusView(status, true);
            if (view != null) {
                contentParent.addView(view);
            }
        }
    }

    private void removeStatusView() {
        for (int i = contentParent.getChildCount() - 1; i > 0; i--) {
            contentParent.removeViewAt(i);
        }
    }

    /**
     * @param status       http状态
     * @param hasTopMargin 是否需要头部margin，true适用activity，false适用recyclerview
     */
    public View getStatusView(int status, boolean hasTopMargin) {
        View statusView = null;
        if (hasTopMargin) {
            if (mMarginStatusArray == null) {
                mMarginStatusArray = new SparseArray<>(6);
            }
            statusView = mMarginStatusArray.get(status);
        } else {
            if (mStatusArray == null) {
                mStatusArray = new SparseArray<>(6);
            }
            statusView = mStatusArray.get(status);
        }
        //新建StatusView
        if (statusView == null) {
            int layoutId = 0;
            if (status == BaseConst.HttpStatus.Status_LOADING) {
                layoutId = BaseConst.Default.mBaseHttpStatus.LoadingLayout();
            } else if (status == BaseConst.HttpStatus.Status_NOTHING) {
                layoutId = BaseConst.Default.mBaseHttpStatus.NothingLayout();
            } else if (status == BaseConst.HttpStatus.Status_NETWORK) {
                layoutId = BaseConst.Default.mBaseHttpStatus.NetWorkErrorLayout();
            } else if (status == BaseConst.HttpStatus.Status_ERROR) {
                layoutId = BaseConst.Default.mBaseHttpStatus.ErrorLayout();
            }
            statusView = View.inflate(this, layoutId, null);
            FrameLayout.MarginLayoutParams lp = new FrameLayout.MarginLayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            if (hasTopMargin) {
                lp.topMargin = getResources().getDimensionPixelSize(R.dimen.ActionBarHeight);
            }
            statusView.setLayoutParams(lp);
            if (hasTopMargin) {
                mMarginStatusArray.put(status, statusView);
            } else {
                mStatusArray.put(status, statusView);
            }
        }
        return statusView;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            onActivityResultOK(requestCode, data);
        }
    }

    /**
     * 重写start方法，加入启动动画
     */
    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        //如果2个不设置全，activity切换时会造成短暂黑屏
        if (mActivityAnimOpen != 0)
            this.overridePendingTransition(mActivityAnimOpen, R.anim.anim_activity_null);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        if (mActivityAnimOpen != 0)
            this.overridePendingTransition(mActivityAnimOpen, R.anim.anim_activity_null);
    }

    /**
     * 重写finish方法，键入关闭动画
     */
    @Override
    public void finish() {
        super.finish();
        if (mActivityAnimClose != 0)
            this.overridePendingTransition(R.anim.anim_activity_null, mActivityAnimClose);
    }

}
