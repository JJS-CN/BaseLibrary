package com.simplelibrary.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Toast;

import com.blankj.utilcode.util.KeyboardUtils;
import com.gyf.barlibrary.ImmersionBar;
import com.simplelibrary.R;
import com.simplelibrary.mvp.BasePersenter;
import com.simplelibrary.mvp.IContract;
import com.simplelibrary.utils.RxAutoDisposeUtils;
import com.uber.autodispose.AutoDisposeConverter;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * 说明：
 * Created by jjs on 2018/11/23
 */

public abstract class BaseActivity<P extends BasePersenter<IContract.IView>> extends AppCompatActivity implements IContract.IView {

    private Unbinder mUnBinder;
    protected P mPersenter;

    private long mStart;
    protected boolean mDoubleClick2Exit = false;//是否需要双击退出拦截提示
    protected boolean mTranslucentStatus = false;//透明状态栏
    protected boolean mStatusBarHidden = false;//隐藏状态栏
    @ColorInt
    protected int mStatusBarColor = -1;//状态栏的颜色 int值
    protected boolean hasFitsSystemWindows = true;//是否需要向下挤压
    protected boolean hasStatusBarDarkFont = BaseApplication.Default_StatusBarDarkFont;//状态栏的图标是否为 黑颜色

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (mStatusBarHidden) {
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        mUnBinder = ButterKnife.bind(this);

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
        initView();
    }

    protected abstract void initView();

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

    protected abstract int getLayoutId();

    protected abstract P createPersenter();

    protected void onActivityResultOK(int requestCode, Intent data) {

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
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mStart = mend;
            }
        }
    }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            onActivityResultOK(requestCode, data);
        }
    }

}
