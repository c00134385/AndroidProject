package com.hjq.demo.ui.act.t;

import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.hjq.demo.R;

import butterknife.BindView;
import timber.log.Timber;

public class KeyTestActivity extends BaseTestActivity {

    @BindView(R.id.volume_add)
    TextView volumeAdd;

    @BindView(R.id.volume_sub)
    TextView volumeSub;

    @BindView(R.id.home_key)
    TextView homeKey;

    @Override
    protected int getBottomBarId() {
        return R.id.bottom_bar;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_test_key;
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Timber.d("");
        if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            if(event.getAction() == KeyEvent.ACTION_DOWN) {
                volumeAdd.setBackgroundResource(R.color.red);
            } else if(event.getAction() == KeyEvent.ACTION_UP) {
                volumeAdd.setBackgroundResource(R.color.white);
            }
        } else if(keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            if(event.getAction() == KeyEvent.ACTION_DOWN) {
                volumeSub.setBackgroundResource(R.color.red);
            } else if(event.getAction() == KeyEvent.ACTION_UP) {
                volumeSub.setBackgroundResource(R.color.white);
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
