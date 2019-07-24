package com.hjq.demo.ui.act;

import android.view.View;

import com.hjq.demo.R;
import com.hjq.demo.common.MyActivity;
import com.hjq.demo.ui.act.t.DemoTestActivity;
import com.hjq.demo.ui.widget.CardView;

import butterknife.BindView;
import butterknife.OnClick;

public class TestHomeActivity extends MyActivity {

    @BindView(R.id.card_screen)
    CardView card_screen;

    @BindView(R.id.card_network)
    CardView card_network;

    @BindView(R.id.card_speaker)
    CardView card_speaker;

    @BindView(R.id.card_keypad)
    CardView card_keypad;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_testhome;
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

    @OnClick({R.id.card_screen, R.id.card_network, R.id.card_speaker, R.id.card_keypad})
    void onClick(View view) {
//        ToastUtils.show("view:%s" + view.getClass().getSimpleName());

        if(view instanceof CardView) {
            CardView cv = (CardView) view;
            cv.updateState();
        }
        switch (view.getId()) {
            case R.id.card_screen:
                startActivity(DemoTestActivity.class);
                break;

            case R.id.card_network:
                startActivity(DemoTestActivity.class);
                break;

            case R.id.card_speaker:
                break;

            case R.id.card_keypad:
                break;
        }
    }
}
