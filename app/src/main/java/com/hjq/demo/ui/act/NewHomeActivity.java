package com.hjq.demo.ui.act;

import android.app.Dialog;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.hjq.demo.R;
import com.hjq.demo.common.MyActivity;
import com.hjq.demo.mananger.OrthManager;
import com.hjq.demo.ui.widget.CardView1;
import com.hjq.dialog.MessageDialog;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import timber.log.Timber;

public class NewHomeActivity extends MyActivity implements View.OnClickListener {

    @BindView(R.id.tv_info)
    TextView tvInfo;

    @BindView(R.id.cv_android)
    CardView1 cvAndroid;

    @BindView(R.id.cv_machine)
    CardView1 cvMachine;

    @BindView(R.id.cv_network)
    CardView1 cvNetwork;

    @BindView(R.id.cv_hardware)
    CardView1 cvHardware;

    @BindView(R.id.cv_test)
    CardView1 cvTest;

    @BindView(R.id.btn_exit)
    Button btnExit;


    private long startTouchTime;
    private int validTouchCount;

    private static final int MSG_CHECK_ORTH = 0x1003;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_CHECK_ORTH:
                    if(!OrthManager.getInstance().isOrthValid()) {
                        showDialog();
                    }
                    break;
            }

        }
    };
    @Override
    protected int getLayoutId() {
        return R.layout.activity_home_new;
    }

    @Override
    protected int getTitleId() {
        return R.id.top_bar;
    }

    @Override
    protected void initView() {
        cvAndroid.setOnClickListener(this);
        cvMachine.setOnClickListener(this);
        cvNetwork.setOnClickListener(this);
        cvHardware.setOnClickListener(this);
        cvTest.setOnClickListener(this);
        btnExit.setOnClickListener(this);
        if(null != getTitleBar()) {
            TitleBar titleBar = getTitleBar();
            titleBar.setOnTitleBarListener(new OnTitleBarListener() {
                @Override
                public void onLeftClick(View v) {
                    Timber.d("onLeftClick v:%s", v.getClass().getSimpleName());
                }

                @Override
                public void onTitleClick(View v) {
                    Timber.d("onTitleClick v:%s", v.getClass().getSimpleName());
                    processHideInfo();
                }

                @Override
                public void onRightClick(View v) {
                    Timber.d("onRightClick v:%s", v.getClass().getSimpleName());
                }
            });
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    private void processHideInfo() {

        if(System.currentTimeMillis() - startTouchTime > TimeUnit.SECONDS.toMillis(2)) {
            validTouchCount = 0;
        }

        if(validTouchCount == 0) {
            startTouchTime = System.currentTimeMillis();
        }
        validTouchCount++;

        if(validTouchCount >= 3 && System.currentTimeMillis() - startTouchTime < TimeUnit.SECONDS.toMillis(2)) {
            if(findViewById(R.id.cv_test).getVisibility() != View.VISIBLE) {
                findViewById(R.id.cv_test).setVisibility(View.VISIBLE);
            } else {
                findViewById(R.id.cv_test).setVisibility(View.INVISIBLE);
            }
            validTouchCount = 0;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        validTouchCount = 0;
        handler.sendEmptyMessage(MSG_CHECK_ORTH);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacksAndMessages(null);
    }

    private void showDialog() {
        MessageDialog.Builder builder = new MessageDialog.Builder(this)
                .setTitle("提示") // 标题可以不用填写
                .setMessage("请确认是否已授权？")
                .setConfirm("确定")
                .setCancel("取消") // 设置 null 表示不显示取消按钮
                .setCancelable(false)
//                .setAutoDismiss(false) // 设置点击按钮后不关闭对话框
                .setListener(new MessageDialog.OnListener() {

                    @Override
                    public void onConfirm(Dialog dialog) {
//                        ToastUtils.show("确定了");
//                        handler.sendEmptyMessageDelayed(MSG_CHECK_ORTH, TimeUnit.SECONDS.toMillis(1));
                        handler.sendEmptyMessage(MSG_CHECK_ORTH);
                    }

                    @Override
                    public void onCancel(Dialog dialog) {
//                        ToastUtils.show("取消了");
//                        handler.sendEmptyMessageDelayed(MSG_CHECK_ORTH, TimeUnit.SECONDS.toMillis(1));
                        handler.sendEmptyMessage(MSG_CHECK_ORTH);
                    }
                });
        builder.show();
    }

    @Override
    public void onClick(View v) {
        Timber.d("v:%s", v.getClass().getSimpleName());
        switch (v.getId()) {
            case R.id.cv_machine:
                startActivity(MachineActivity.class);
                break;
            case R.id.cv_network:
                startActivity(NetworkActivity.class);
                break;
            case R.id.cv_android:
                startActivity(AndroidActivity.class);
                break;
            case R.id.cv_hardware:
                startActivity(HardwareActivity.class);
                break;
            case R.id.cv_test:
                startActivity(NewTestHomeActivity.class);
                break;
            case R.id.btn_exit:
                finish();
                break;
        }
    }
}
