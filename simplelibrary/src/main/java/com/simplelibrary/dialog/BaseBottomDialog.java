package com.simplelibrary.dialog;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseViewHolder;
import com.simplelibrary.R;

/**
 * 说明：
 * Created by jjs on 2018/7/21
 */

public class BaseBottomDialog extends BottomSheetDialogFragment {
    private View mRootView;
    private BottomSheetBehavior mBehavior;
    private boolean hasExpand;//是否需要最大化显示
    private OnCustomListener mCustomListener;
    private int mLayoutId;
    public BaseViewHolder mViewHolder;

    {
        setLayoutId(R.layout.dialog_choose_photo_default);
    }
    /**
     * 设置布局id
     */
    public BaseBottomDialog setLayoutId(@LayoutRes int layoutId) {
        mLayoutId = layoutId;
        return this;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(mLayoutId, container);
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

    public BaseBottomDialog setCustomListener(OnCustomListener customListener) {
        this.mCustomListener = customListener;
        return this;
    }

    public interface OnCustomListener {
        void onCustom(BaseViewHolder holder, BottomSheetDialogFragment dialog);
    }

    public BaseBottomDialog setHasExpand(boolean hasExpand) {
        this.hasExpand = hasExpand;
        return this;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (hasExpand) {
            if (mBehavior == null) {
                mBehavior = BottomSheetBehavior.from((View) mRootView.getParent());
                mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        }
    }

    public void show(FragmentActivity activity) {
        super.show(activity.getSupportFragmentManager(), "");
    }
}
