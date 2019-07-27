package com.hjq.demo.ui.act.t;

import android.view.View;

import com.hjq.demo.R;
import com.hjq.demo.ui.widget.PaintView;

import butterknife.BindView;

public class TouchTestActivity extends BaseTestActivity {

    @BindView(R.id.paint_view)
    PaintView paintView;

    @Override
    protected int getBottomBarId() {
        return R.id.bottom_bar;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_test_touch;
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
        paintView.setVisibility(View.VISIBLE);
        paintView.setBackgroundResource(R.color.white);
    }

    @Override
    protected void onTestStart() {
        super.onTestStart();
    }

    @Override
    protected void onTestEnd() {
        super.onTestEnd();
    }
}
