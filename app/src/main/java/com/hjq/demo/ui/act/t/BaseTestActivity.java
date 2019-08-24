package com.hjq.demo.ui.act.t;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import androidx.annotation.Nullable;

import com.hjq.demo.R;
import com.hjq.demo.common.MyActivity;
import com.hjq.demo.common.MyLazyFragment;
import com.hjq.demo.mananger.TestManager;
import com.hjq.demo.model.StateEnum;
import com.hjq.demo.model.TestItemModel;
import com.hjq.demo.ui.act.frag.CameraTestFragment;
import com.hjq.demo.ui.act.frag.ForgTestFragment;
import com.hjq.demo.ui.act.frag.HPTestFragment;
import com.hjq.demo.ui.act.frag.KeyboardTestFragment;
import com.hjq.demo.ui.act.frag.LanTestFragment;
import com.hjq.demo.ui.act.frag.PrinterTestFragment;
import com.hjq.demo.ui.act.frag.RecordTestFragment;
import com.hjq.demo.ui.act.frag.ScanGunTestFragment;
import com.hjq.demo.ui.act.frag.SerialTestFragment;
import com.hjq.demo.ui.act.frag.SpeakerTestFragment;
import com.hjq.demo.ui.act.frag.Touch2TestFragment;
import com.hjq.demo.ui.act.frag.TouchTestFragment;
import com.hjq.demo.ui.act.frag.VideoTestFragment;
import com.hjq.demo.ui.act.frag.WifiTestFragment;
import com.hjq.demo.ui.widget.BottomBar;
import com.hjq.demo.utils.GsonUtil;

abstract public class BaseTestActivity extends MyActivity {

    static public String KEY_TEST_ITEM = "TEST_ITEM";
    protected boolean isTestRunning = false;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    protected TestItemModel testItem;
    protected MyLazyFragment fragment;

    @Override
    protected void initLayout() {
        super.initLayout();

        Intent intent = getIntent();
        String extraJson = intent.getStringExtra(BaseTestActivity.KEY_TEST_ITEM);
        testItem = GsonUtil.getGson().fromJson(extraJson, TestItemModel.class);
        if(null != getTitleBar() && null != testItem) {
            getTitleBar().setTitle(testItem.getTitle().value());
        }

        if(getBottomBarId() > 0) {
            if (findViewById(getBottomBarId()) instanceof BottomBar) {
                ((BottomBar) findViewById(getBottomBarId())).setClickListener(new BottomBar.OnClickListener() {
                    @Override
                    public void onLeftClick(View v) {
//                        ToastUtils.show("left is clicked");
                        onTestStart();
                    }

                    @Override
                    public void onMiddleClick(View v) {
//                        ToastUtils.show("middle is clicked");
                        onTestSuccess();
                    }

                    @Override
                    public void onRightClick(View v) {
//                        ToastUtils.show("right is clicked");
                        onTestFailed();
                    }
                });
            }

            if(getBodyId() > 0) {
                addBody();
            }
        }
    }

    @Nullable
    public BottomBar getBottomBar() {
        if (getBottomBarId() > 0 && findViewById(getBottomBarId()) instanceof BottomBar) {
            return findViewById(getBottomBarId());
        }
        return null;
    }

    // 标题栏
    protected abstract int getBottomBarId();

    protected int getBodyId(){
        return 0;
    }

    protected void onTestStart(){
        if(null != testItem) {
            testItem.setState(StateEnum.NOT_TEST);
            TestManager.getInstance().updateTest(testItem);
        }
    };

    protected void onTestEnd() {

    }

    protected void onTestSuccess() {
        if(null != testItem) {
            testItem.setState(StateEnum.SUCCESS);
            TestManager.getInstance().updateTest(testItem);
        }
        finish();
    }

    protected void onTestFailed() {
        if(null != testItem) {
            testItem.setState(StateEnum.FAILED);
            TestManager.getInstance().updateTest(testItem);
        }
        finish();
    }

    private void addBody() {
        switch (testItem.getTitle()) {
            case TOUCH1:
                fragment = new TouchTestFragment();
                break;
            case TOUCH2:
                fragment = new Touch2TestFragment();
                break;
            case SPEAKER:
                fragment = new SpeakerTestFragment();
                break;
            case VIDEO:
                fragment = new VideoTestFragment();
                break;
            case RECORD:
                fragment = new RecordTestFragment();
                break;
            case HEADPHONE:
                fragment = new HPTestFragment();
                break;
            case WIFI:
                fragment = new WifiTestFragment();
                break;
            case LAN:
                fragment = new LanTestFragment();
                break;
            case FORG:
                fragment = new ForgTestFragment();
                break;
            case KEYBOARD:
                fragment = new KeyboardTestFragment();
                break;
            case SCAN_GUN:
                fragment = new ScanGunTestFragment();
                break;
            case PRINTER:
                fragment = new PrinterTestFragment();
                break;
            case CAMERA:
                fragment = new CameraTestFragment();
                break;
            case SERIAL:
                fragment = new SerialTestFragment();
                break;
        }

        if(null != fragment) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.frag_container, fragment);
            transaction.commit();
        }
    }
}
