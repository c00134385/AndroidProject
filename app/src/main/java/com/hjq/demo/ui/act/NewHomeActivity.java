package com.hjq.demo.ui.act;

import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.hjq.demo.R;
import com.hjq.demo.common.MyActivity;
import com.hjq.demo.service.DeviceUploadService;
import com.hjq.demo.ui.widget.CardView1;

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

    int width;
    int height;

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
//                    processHideInfo();
                }

                @Override
                public void onRightClick(View v) {
                    Timber.d("onRightClick v:%s", v.getClass().getSimpleName());
                }
            });
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Timber.d("action:%d x:%f y:%f", event.getAction(), event.getX(), event.getY());
        if(event.getAction() == MotionEvent.ACTION_DOWN && (event.getX() > width/2) && (event.getY() > height / 2)) {
            processHideInfo();
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void initData() {
        Intent intent = new Intent(this, DeviceUploadService.class);
        startService(intent);

        width = getResources().getDisplayMetrics().widthPixels;
        height = getResources().getDisplayMetrics().heightPixels;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void processHideInfo() {

        if(System.currentTimeMillis() - startTouchTime > TimeUnit.SECONDS.toMillis(2)) {
            validTouchCount = 0;
        }

        if(validTouchCount == 0) {
            startTouchTime = System.currentTimeMillis();
        }
        validTouchCount++;

        if(validTouchCount >= 5 && System.currentTimeMillis() - startTouchTime < TimeUnit.SECONDS.toMillis(2)) {
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
    }

    @Override
    protected void onPause() {
        super.onPause();
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
