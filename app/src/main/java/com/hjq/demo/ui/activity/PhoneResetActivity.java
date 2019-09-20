package com.hjq.demo.ui.activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hjq.demo.R;
import com.hjq.demo.common.MyActivity;
import com.hjq.demo.helper.InputTextHelper;
import com.hjq.widget.CountdownView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 *    author : Android
 *    github : https://github.com/
 *    time   : 2019/04/20
 *    desc   : 更换手机号
 */
public final class PhoneResetActivity extends MyActivity {

    @BindView(R.id.et_phone_reset_phone)
    EditText mPhoneView;
    @BindView(R.id.et_phone_reset_code)
    EditText mCodeView;

    @BindView(R.id.cv_phone_reset_countdown)
    CountdownView mCountdownView;

    @BindView(R.id.btn_phone_reset_commit)
    Button mCommitView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_phone_reset;
    }

    @Override
    protected int getTitleId() {
        return R.id.tb_phone_reset_title;
    }

    @Override
    protected void initView() {
        new InputTextHelper.Builder(this)
                .setMain(mCommitView)
                .addView(mPhoneView)
                .addView(mCodeView)
                .build();
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.cv_phone_reset_countdown, R.id.btn_phone_reset_commit})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cv_phone_reset_countdown:
                if (mPhoneView.getText().toString().length() != 11) {
                    // 重置验证码倒计时控件
                    mCountdownView.resetState();
                    toast(getString(R.string.common_phone_input_error));
                } else {
                    // 获取验证码
                    toast(getString(R.string.common_send_code_succeed));
                }
                break;
            case R.id.btn_phone_reset_commit:
                if (mPhoneView.getText().toString().length() != 11) {
                    toast(getString(R.string.common_phone_input_error));
                } else {
                    // 更换手机号
                    toast(getString(R.string.phone_reset_commit_succeed));
                    finish();
                }
                break;
        }
    }
}