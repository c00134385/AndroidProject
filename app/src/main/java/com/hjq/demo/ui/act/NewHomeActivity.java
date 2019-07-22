package com.hjq.demo.ui.act;

import android.app.Dialog;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hjq.demo.R;
import com.hjq.demo.common.MyActivity;
import com.hjq.demo.mananger.OrthManager;
import com.hjq.demo.utils.ScreenUtils;
import com.hjq.dialog.MessageDialog;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;

public class NewHomeActivity extends MyActivity {
    @BindView(R.id.root_view)
    View rootView;
    @BindView(R.id.img_machine)
    ImageView imgMachine;

    @BindView(R.id.tv_info)
    TextView tvInfo;

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
        return R.layout.activity_newhome;
    }

    @Override
    protected int getTitleId() {
        return R.id.tb_home_title;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.img_machine, R.id.img_network, R.id.img_android,
            R.id.img_hardware, R.id.img_gotest, R.id.btn_exit})
    void onClick(View v) {
//        String str = String.format("v:%s", v.getClass().getSimpleName());
//        ToastUtils.show(str);
        switch (v.getId()) {
            case R.id.img_machine:
                startActivity(MachineActivity.class);
                break;
            case R.id.img_network:
                startActivity(NetworkActivity.class);
                break;
            case R.id.img_android:
                startActivity(AndroidActivity.class);
                break;
            case R.id.img_hardware:
                startActivity(HardwareActivity.class);
                break;
            case R.id.img_gotest:
//                goTest1();
                startActivity(TestHomeActivity.class);
                break;
            case R.id.btn_exit:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int screenWidth = ScreenUtils.getScreenWidth(this);
        int screenHeight = ScreenUtils.getScreenHeight(this);
        int x = (int)event.getX();
        int y = (int)event.getY();

        if(event.getAction() == MotionEvent.ACTION_UP) {
            if(x < screenWidth/3 && y <screenHeight) {
                if(0 == validTouchCount) {
                    startTouchTime = System.currentTimeMillis();
                }
                validTouchCount++;
            } else {
                validTouchCount = 0;
            }
        }

        if(validTouchCount >= 3 && System.currentTimeMillis() - startTouchTime < TimeUnit.SECONDS.toMillis(2)) {
            if(findViewById(R.id.con_gotest).getVisibility() != View.VISIBLE) {
                findViewById(R.id.con_gotest).setVisibility(View.VISIBLE);
            } else {
                findViewById(R.id.con_gotest).setVisibility(View.GONE);
            }
            validTouchCount = 0;
        } else if(System.currentTimeMillis() - startTouchTime > TimeUnit.SECONDS.toMillis(2)) {
            validTouchCount = 0;
        }

        StringBuilder sb = new StringBuilder();
        String info = String.format("action:%d\nx:%f\ny:%f\n", event.getAction(), event.getX(), event.getY());
        sb.append(info);
        sb.append(String.format("screen width:%d height:%d\n", screenWidth, screenHeight));
        tvInfo.setText(sb.toString());
        return super.onTouchEvent(event);
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
                        handler.sendEmptyMessageDelayed(MSG_CHECK_ORTH, TimeUnit.MINUTES.toMillis(1));
                    }

                    @Override
                    public void onCancel(Dialog dialog) {
//                        ToastUtils.show("取消了");
                        handler.sendEmptyMessageDelayed(MSG_CHECK_ORTH, TimeUnit.MINUTES.toMillis(1));
                    }
                });
        builder.show();
    }
}
