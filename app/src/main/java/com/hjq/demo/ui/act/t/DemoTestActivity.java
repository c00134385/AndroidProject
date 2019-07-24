package com.hjq.demo.ui.act.t;

import com.hjq.demo.R;

public class DemoTestActivity extends BaseTestActivity {
    @Override
    protected int getLayoutId() {
//        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return R.layout.activity_test_demo;
    }

    @Override
    protected int getTitleId() {
        return R.id.top_bar;
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getBottomBarId() {
        return R.id.bottom_bar;
    }
}
