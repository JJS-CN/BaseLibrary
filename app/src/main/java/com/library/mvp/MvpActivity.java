package com.library.mvp;

import android.view.View;
import android.widget.Toast;

import com.library.R;
import com.simplelibrary.base.BaseActivity;

/**
 * 说明：
 * Created by jjs on 2018/11/27
 */

public class MvpActivity extends BaseActivity<LoginPersenter> implements LoginContract.View {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected LoginPersenter createPersenter() {
        return new LoginPersenter(this);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void loadData() {
        findViewById(R.id.email_sign_in_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPersenter.login("18607917251", "");
                    }
                });
    }

    @Override
    public void isLogin() {
        Toast.makeText(this, "LoginSuccess", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(int status, String message) {
        super.onError(status, message);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
