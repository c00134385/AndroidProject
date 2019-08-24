package com.hjq.demo.ui.act.frag;

import android.view.View;

import com.hjq.demo.R;
import com.hjq.demo.common.MyLazyFragment;
import com.hjq.demo.ui.widget.PaintView;

import butterknife.BindView;

public class TouchTestFragment extends MyLazyFragment {

    @BindView(R.id.paint_view)
    PaintView paintView;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_test_touch;
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
        paintView.setVisibility(View.VISIBLE);
        paintView.setBackgroundResource(R.color.white);
    }
}
