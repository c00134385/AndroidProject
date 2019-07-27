package com.hjq.demo.ui.act.t;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import androidx.annotation.Nullable;

import com.hjq.demo.common.MyActivity;
import com.hjq.demo.mananger.TestManager;
import com.hjq.demo.model.StateEnum;
import com.hjq.demo.model.TestItemModel;
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

    @Override
    protected void initLayout() {
        super.initLayout();
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
        }
    }

    @Override
    protected void initView() {
        Intent intent = getIntent();
        String extraJson = intent.getStringExtra(BaseTestActivity.KEY_TEST_ITEM);
        testItem = GsonUtil.getGson().fromJson(extraJson, TestItemModel.class);
        if(null != getTitleBar()) {
            getTitleBar().setTitle(testItem.getTitle().value());
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
    }

    protected void onTestFailed() {
        if(null != testItem) {
            testItem.setState(StateEnum.FAILED);
            TestManager.getInstance().updateTest(testItem);
        }
    }
}
