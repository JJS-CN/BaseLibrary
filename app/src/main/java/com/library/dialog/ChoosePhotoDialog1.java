package com.library.dialog;

import android.view.View;


import com.library.R;
import com.simplelibrary.dialog.ChoosePhotoDialog;

/**
 * Created by dangdang on 2018/11/24.
 */

public class ChoosePhotoDialog1 extends ChoosePhotoDialog {

    {
        setLayoutId(R.layout.item_pw_dialog);
    }

    @Override
    protected void initView() {
        super.initView();
        mViewHolder.getView(R.id.tv_pw_title1)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mChoosePhotoUtils.requestCamera();
                        dismiss();
                    }
                });
        mViewHolder.getView(R.id.tv_pw_confirm1)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mChoosePhotoUtils.requestAlbum();
                        dismiss();
                    }
                });
        mViewHolder.getView(R.id.tv_pw_cancel1)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                });


    }


}
