package com.hjq.demo.ui.act.t;

import android.widget.FrameLayout;
import android.widget.TextView;

import com.hjq.demo.R;

import butterknife.BindView;

public class DemoTestActivity extends BaseTestActivity {

    @BindView(R.id.test_item_name)
    TextView testItemName;

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
        if(null != testItem) {
            testItemName.setText(testItem.getTitle().value());
        }
    }

    @Override
    protected int getBodyId() {
        return R.id.frag_container;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
