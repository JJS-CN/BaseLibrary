package com.library.dialog;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseViewHolder;
import com.library.R;
import com.library.base.ApiClient;
import com.library.base.BaseEntity;
import com.library.base.RxObserver;
import com.simplelibrary.adapter.PictureSelcterAdapter;
import com.simplelibrary.annotation.NeedLogin;
import com.simplelibrary.annotation.NeedPermission;
import com.simplelibrary.dialog.BaseBottomDialog;
import com.simplelibrary.dialog.BaseDialog;
import com.simplelibrary.dialog.InputDialog;
import com.simplelibrary.sp.BaseUserSp;
import com.simplelibrary.utils.ChoosePhotoUtils;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.OnClick;


@NeedLogin
@NeedPermission(value = {PermissionConstants.CAMERA, PermissionConstants.STORAGE})
public class DialogActivity extends AppCompatActivity {
    DialogDemo dialogDemo;
    BottomDialogDemo mBottomDialogDemo;
    ChoosePhotoUtils choose;
    private RecyclerView mRecyclerView;
    private PictureSelcterAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        dialogDemo = new DialogDemo();
        dialogDemo.updateData("修改", Color.MAGENTA);
        mBottomDialogDemo = new BottomDialogDemo();
        mBottomDialogDemo.updateData("修改22232", Color.MAGENTA);
        mRecyclerView = findViewById(R.id.rv_content);
        SPUtils.getInstance(BaseUserSp.KEY_User).put(BaseUserSp.KEY_User, "111");
        choose = new ChoosePhotoUtils()
                .setChooseListener(new ChoosePhotoUtils.OnChooseListener() {
                    @Override
                    public void onChoose(File originalFile, File compressFile) {
                        LogUtils.e(originalFile.getPath() + "==" + compressFile.getPath());
                    }
                })
                .setAspectXY(2, 1);
        adapter = new PictureSelcterAdapter(this, R.layout.item_pic, new ChoosePhotoDialog1()) {
            @Override
            protected void convertPhoto(BaseViewHolder helper, PhotoEntity item) {
                Glide.with(DialogActivity.this).load(item.isFile() ? item.getFile() : item.getUrl()).into((ImageView) helper.getView(R.id.iv_bg));
            }

            @Override
            protected void convertAdd(BaseViewHolder helper) {
                helper.setImageResource(R.id.iv_bg, R.mipmap.ic_launcher);
            }
        };
        adapter.setDeleteViewId(R.id.iv_del);
        adapter.setAspectXY(200, 200);
        adapter.setSelectLimit(8);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
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
                ApiClient.getApi().login("18607917251")
                        .subscribe(new RxObserver<BaseEntity>(null) {
                            @Override
                            protected void onSuccess(BaseEntity data) {

                            }
                        });
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
