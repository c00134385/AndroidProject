package com.hjq.demo.ui.act.t;

import com.hjq.demo.R;
import com.hjq.demo.common.MyActivity;

public class BaseTestActivity extends MyActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_screen;
    }

    @Override
    protected int getTitleId() {
        return 0;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }
}
