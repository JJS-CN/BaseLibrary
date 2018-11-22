package com.simplelibrary.dialog;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseViewHolder;


/**
 * 内容通过 setArguments(); 进行添加，传入bundle
 * 在initView方法中执行对View的操作
 * Created by jjs on 2018/5/28
 */

public class BaseDialog extends DialogFragment {
    private int mLayoutId;
    private OnCustomListener mCustomListener;
    public BaseViewHolder mViewHolder;


    /**
     * 设置布局id
     */
    public BaseDialog setLayoutId(@LayoutRes int layoutId) {
        mLayoutId = layoutId;
        return this;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mRootView = inflater.inflate(mLayoutId, container);
        mViewHolder = new BaseViewHolder(mRootView);
        updateView();
        if (mCustomListener != null) {
            mCustomListener.onCustom(mViewHolder, this);
        }
        return mRootView;
    }

    //继承方式使用时，在构造方法中初始化数据，initView中解析绑定数据
    //展示后需要更新UI时，需判断viewholder是否为空，因为调用show()到onCreateView需要一段时间
    protected void initView() {
    }

    //做一次非空判断，从走initview
    public void updateView() {
        if (mViewHolder != null) {
            initView();
        }
    }

    //会在初始化时调用一次，可通过viewHolder拿到所有控件
    public BaseDialog setCustomListener(OnCustomListener customListener) {
        this.mCustomListener = customListener;
        return this;
    }

    public interface OnCustomListener {
        void onCustom(BaseViewHolder holder, BaseDialog dialog);
    }

    public void show(FragmentActivity activity) {
        super.show(activity.getSupportFragmentManager(), "");
    }
}
