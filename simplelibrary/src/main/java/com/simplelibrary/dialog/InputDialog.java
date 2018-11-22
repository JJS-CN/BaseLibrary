package com.simplelibrary.dialog;


import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.simplelibrary.R;

/**
 * 说明：单个内容输入
 * 无法满足需求时需求，可通过setCustomListener进行自定义--会在初始化时调用一次
 * Created by jjs on 2018/11/21
 */

public class InputDialog extends BaseDialog {
    private String title, hint, value, check;
    private int maxLen, inputType = 1;
    private OnInputListener mInputListener;

    public InputDialog() {
        setLayoutId(R.layout.dialog_base_input);
    }

    @Override
    protected void initView() {
        super.initView();
        //设置标题
        mViewHolder.setText(R.id.tv_title, title);
        //设置输入框参数
        final EditText mEditInput = mViewHolder.getView(R.id.edit_input);
        mEditInput.setText(value);
        mEditInput.setHint(hint);
        if (maxLen > 0) {
            mEditInput.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLen)});
        }
        mEditInput.setInputType(inputType);
        //设置按钮逻辑
        TextView mTvCheck = mViewHolder.getView(R.id.tv_check);
        if (!TextUtils.isEmpty(check)) {
            mTvCheck.setText(check);
        }
        mTvCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = mEditInput.getText().toString().trim();
                if (mInputListener != null) {
                    //用户自行判断空情况，与dialog的关闭
                    mInputListener.onInput(input, InputDialog.this);
                }
            }
        });
    }
    //设置check按钮的点击监听
    public InputDialog setInputListener(OnInputListener inputListener) {
        mInputListener = inputListener;
        return this;
    }

    //设置标题，提示语，内容值
    public InputDialog setData(String title, String hint, String value) {
        this.title = title;
        this.hint = hint;
        this.value = value;
        updateView();
        return this;
    }

    //设置edit的输入限制
    public InputDialog setInputLimit(int maxLen, int inputType) {
        this.maxLen = maxLen;
        this.inputType = inputType;
        return this;
    }

    //设置确认文本
    public InputDialog setCheckText(String check) {
        this.check = check;
        updateView();
        return this;
    }


    public interface OnInputListener {
        void onInput(String input, InputDialog dialog);

    }

}
