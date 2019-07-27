package com.hjq.demo.ui.act.t;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.hjq.demo.R;
import com.hjq.demo.media.HomeListener;

import butterknife.BindView;
import timber.log.Timber;

public class KeyTestActivity extends BaseTestActivity {

    public static final String TAG = KeyTestActivity.class.getSimpleName();


    @BindView(R.id.volume_add)
    CheckedTextView volumeAdd;

    @BindView(R.id.volume_sub)
    CheckedTextView volumeSub;

    @BindView(R.id.home_key)
    CheckedTextView homeKey;

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

    private HomeListener mHomeListen = null;

    @Override
    protected void initView() {
        super.initView();
        mHomeListen = new HomeListener(this);
        mHomeListen.setInterface(new HomeListener.KeyFun() {
            @Override
            public void recent() {
                Log.d(TAG, "recent");
            }

            @Override
            public void longHome() {
                Log.d(TAG, "longHome");
            }

            @Override
            public void home() {
                Log.d(TAG, "home");
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        Timber.d("keyCode:%d event:%d", keyCode, event.getAction());
        if(keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            volumeAdd.setChecked(false);
        } else if(keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            volumeSub.setChecked(false);
        } else if(keyCode == KeyEvent.KEYCODE_HOME) {
            homeKey.setChecked(false);
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Timber.d("keyCode:%d event:%d", keyCode, event.getAction());
        if(keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            volumeAdd.setChecked(true);
        } else if(keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            volumeSub.setChecked(true);
        } else if(keyCode == KeyEvent.KEYCODE_HOME) {
            homeKey.setChecked(true);
        }
//        if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
//            if(event.getAction() == KeyEvent.ACTION_DOWN) {
//                volumeAdd.setBackgroundResource(R.color.red);
//            } else if(event.getAction() == KeyEvent.ACTION_UP) {
//                volumeAdd.setBackgroundResource(R.color.white);
//            }
//        } else if(keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
//            if(event.getAction() == KeyEvent.ACTION_DOWN) {
//                volumeSub.setBackgroundResource(R.color.red);
//            } else if(event.getAction() == KeyEvent.ACTION_UP) {
//                volumeSub.setBackgroundResource(R.color.white);
//            }
//        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHomeListen.startListen();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHomeListen.stopListen();
    }
}
