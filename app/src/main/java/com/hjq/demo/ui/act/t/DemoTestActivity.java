package com.hjq.demo.ui.act.t;

import android.widget.FrameLayout;

import com.hjq.demo.R;

import butterknife.BindView;

public class DemoTestActivity extends BaseTestActivity {

    @BindView(R.id.frag_container)
    FrameLayout container;

    @Override
    protected int getBottomBarId() {
        return R.id.bottom_bar;
    }

    @Override
    protected int getLayoutId() {
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
    protected int getBodyId() {
        return R.id.frag_container;
    }
}
