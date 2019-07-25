package com.hjq.demo.ui.act.t;

import android.widget.Button;
import android.widget.SeekBar;

import com.hjq.demo.R;

import butterknife.BindView;

public class SpeakerTestActivity extends BaseTestActivity {

    @BindView(R.id.level)
    SeekBar level;

    @BindView(R.id.left_channel)
    Button btnLeftCh;

    @BindView(R.id.right_channel)
    Button btnRightCh;

    @BindView(R.id.stereo_channel)
    Button btnStereo;

    @Override
    protected int getBottomBarId() {
        return R.id.bottom_bar;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_test_speaker;
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


}
