package com.library.dialog;

import android.graphics.Color;

import com.library.R;
import com.simplelibrary.dialog.BaseDialog;

/**
 * 说明：
 * Created by jjs on 2018/11/21
 */

public class DialogDemo extends BaseDialog {
    //声明可变参数变量
    String str = "初始化";
    int color = Color.GREEN;

    public DialogDemo() {
        //构造方法中加载layout
        setLayoutId(R.layout.item_dialog);
    }

    @Override
    protected void initView() {
        super.initView();
        //执行具体操作-取赋值变量
        mViewHolder.setText(R.id.tv1, str)
                .setBackgroundColor(R.id.iv1, color);
    }

    //更新数据
    public void updateData(String str, int color) {
        this.str = str;
        this.color = color;
        updateView();
    }
}
