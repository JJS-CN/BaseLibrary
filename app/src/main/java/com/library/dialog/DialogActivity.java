package com.library.dialog;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.SPUtils;
import com.chad.library.adapter.base.BaseViewHolder;
import com.library.R;
import com.simplelibrary.annotation.NeedLogin;
import com.simplelibrary.annotation.NeedPermission;
import com.simplelibrary.dialog.BaseBottomDialog;
import com.simplelibrary.dialog.BaseDialog;
import com.simplelibrary.dialog.InputDialog;
import com.simplelibrary.sp.BaseUserSp;

import butterknife.ButterKnife;
import butterknife.OnClick;



@NeedLogin
@NeedPermission(value = {PermissionConstants.CAMERA, PermissionConstants.STORAGE})
public class DialogActivity extends AppCompatActivity {
    DialogDemo dialogDemo;
    BottomDialogDemo mBottomDialogDemo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        dialogDemo = new DialogDemo();
        dialogDemo.updateData("修改", Color.MAGENTA);
        mBottomDialogDemo = new BottomDialogDemo();
        mBottomDialogDemo.updateData("修改22232", Color.MAGENTA);
        SPUtils.getInstance(BaseUserSp.KEY_User).put(BaseUserSp.KEY_User,"111");
    }

    @OnClick({R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn1:

                new BaseDialog().setCustomListener(new BaseDialog.OnCustomListener() {
                    @Override
                    public void onCustom(BaseViewHolder holder, BaseDialog dialog) {
                        holder.setText(R.id.tv1, "22222");
                    }
                }).setLayoutId(R.layout.item_dialog).show(this);
                break;
            case R.id.btn2:
                startActivity(new Intent(this,DialogActivity.class));
              //  dialogDemo.show(this);
                break;
            case R.id.btn3:
                new BaseBottomDialog().setLayoutId(R.layout.item_dialog)
                        .setCustomListener(new BaseBottomDialog.OnCustomListener() {
                            @Override
                            public void onCustom(BaseViewHolder holder, BottomSheetDialogFragment dialog) {
                                holder.setText(R.id.tv1, "baseBottom");
                            }
                        })
                        .show(this);
                break;
            case R.id.btn4:
                mBottomDialogDemo.show(this);
                break;
            case R.id.btn5:
                new InputDialog()
                        .setData("姓名", "请输入姓名", "jjs")
                        .setCheckText("确定")
                        .setInputLimit(5, InputType.TYPE_CLASS_NUMBER)
                        .setInputListener(new InputDialog.OnInputListener() {
                            @Override
                            public void onInput(String input, InputDialog dialog) {
                                if (TextUtils.isEmpty(input)) {
                                    Toast.makeText(DialogActivity.this, "请输入", Toast.LENGTH_SHORT).show();
                                } else {
                                    Log.e("input", input);
                                    dialog.dismiss();
                                }
                            }
                        })
                        .setCustomListener(new BaseDialog.OnCustomListener() {
                            @Override
                            public void onCustom(BaseViewHolder holder, BaseDialog dialog) {
                                holder.getView(R.id.tv_check).setBackgroundColor(Color.BLUE);
                            }
                        })
                        .show(this);
                break;
        }
    }
}
