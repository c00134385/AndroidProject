package com.hjq.demo.ui.act.t;

import android.view.View;

import androidx.annotation.Nullable;

import com.hjq.demo.common.MyActivity;
import com.hjq.demo.ui.widget.BottomBar;
import com.hjq.toast.ToastUtils;

import timber.log.Timber;

abstract public class BaseTestActivity extends MyActivity {

    @Override
    protected void initLayout() {
        super.initLayout();
        if(getBottomBarId() > 0) {
            if (findViewById(getBottomBarId()) instanceof BottomBar) {
                ((BottomBar) findViewById(getBottomBarId())).setClickListener(new BottomBar.OnClickListener() {
                    @Override
                    public void onLeftClick(View v) {
                        ToastUtils.show("left is clicked");
                    }

                    @Override
                    public void onMiddleClick(View v) {
                        ToastUtils.show("middle is clicked");
                    }

                    @Override
                    public void onRightClick(View v) {
                        ToastUtils.show("right is clicked");
                    }
                });
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

}
