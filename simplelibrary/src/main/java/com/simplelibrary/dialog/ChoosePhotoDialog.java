package com.simplelibrary.dialog;

import android.view.View;

import com.simplelibrary.R;
import com.simplelibrary.utils.ChoosePhotoUtils;

/**
 * Created by dangdang on 2018/11/24.
 */

public class ChoosePhotoDialog extends BaseDialog {
    protected ChoosePhotoUtils mChoosePhotoUtils;
    private ChoosePhotoUtils.OnChooseListener mOnChooseListener;
    private int mAspectX, mAspectY;


    {
        setLayoutId(R.layout.dialog_choose_photo_default);
        mChoosePhotoUtils=new ChoosePhotoUtils();
    }


    @Override
    protected void initView() {
        super.initView();

        if (mLayoutId == R.layout.dialog_choose_photo_default){
            mViewHolder.getView(R.id.tv_pw_title)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mChoosePhotoUtils.requestCamera();
                            dismiss();
                        }
                    });
            mViewHolder.getView(R.id.tv_pw_confirm)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mChoosePhotoUtils.requestAlbum();  dismiss();
                        }
                    });
            mViewHolder.getView(R.id.tv_pw_cancel)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dismiss();
                        }
                    });
        }

        mChoosePhotoUtils.setChooseListener(mOnChooseListener)
                .setAspectXY(mAspectX,mAspectY);
    }

    public void setOnChooseListener(ChoosePhotoUtils.OnChooseListener onChooseListener) {
        mOnChooseListener = onChooseListener;
    }

    public void setAspectXY(int aspectX,int aspectY) {
        mAspectX = aspectX;
        mAspectY = aspectY;
    }

}
