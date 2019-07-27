package com.hjq.demo.ui.act.frag;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hjq.demo.R;
import com.hjq.demo.common.MyLazyFragment;
import com.hjq.demo.ui.act.t.BaseTestActivity;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

import static android.content.Intent.ACTION_HEADSET_PLUG;

public class HPTestFragment extends MyLazyFragment<BaseTestActivity> {

    @BindView(R.id.hp_state)
    TextView hpState;

    @BindView(R.id.level)
    ProgressBar level;

    @BindView(R.id.left_channel)
    Button btnLeftCh;

    @BindView(R.id.right_channel)
    Button btnRightCh;

    @BindView(R.id.stereo_channel)
    Button btnStereo;

    private AudioManager audoManager;

    private MediaPlayer mMediaPlayer;
    private int currentChannel = 0;  //0:L  1:R  2:Stereo

    private static final int MSG_STOP_MP = 0x1002;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(null != mMediaPlayer) {
                mMediaPlayer.stop();
                mMediaPlayer = null;
            }

        }
    };

    Disposable disposable;

    public static HPTestFragment newInstance() {
        return new HPTestFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_test_hp;
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
        audoManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        Boolean isHeadsetOn = audoManager.isWiredHeadsetOn();
        updateState(isHeadsetOn);
    }

    private void updateState(boolean isHeadsetOn) {
        if(isHeadsetOn) {
            hpState.setText("耳机已插入");
        } else {
            hpState.setText("耳机未插入");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Timber.d("onResume");

        IntentFilter intentFilter = new IntentFilter();//注册广播接收信号
        intentFilter.addAction(ACTION_HEADSET_PLUG);//蓝牙状态改变的广播
        getContext().registerReceiver(mReceiver, intentFilter);//用BroadcastReceiver 来取得结果
    }

    @Override
    public void onPause() {
        super.onPause();
        Timber.d("onPause");
        getContext().unregisterReceiver(mReceiver);

        if(null != disposable && !disposable.isDisposed()) {
            disposable.dispose();
        }

        if(null != mMediaPlayer) {
            mMediaPlayer.stop();
            mMediaPlayer = null;
        }

        handler.removeCallbacksAndMessages(null);
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Timber.d("action:%s", action);
            if (action.equals(ACTION_HEADSET_PLUG)) {
                if(intent.hasExtra("state")) {
                    if(intent.getIntExtra("state", 0) == 0) {
//                        Toast.makeText(context, "headset not connected", Toast.LENGTH_LONG).show();
                        updateState(false);
                    } else if(intent.getIntExtra("state", 0) == 1) {
//                        Toast.makeText(context, "headset connected", Toast.LENGTH_LONG).show();
                        updateState(true);
                    }
                }
            }
        }
    };

    @OnClick({R.id.left_channel, R.id.right_channel, R.id.stereo_channel})
    void onClick(View v) {
        if(null != disposable && !disposable.isDisposed()) {
            disposable.dispose();
            disposable = null;
        }
        if(null != mMediaPlayer) {
            mMediaPlayer.stop();
            mMediaPlayer = null;
        }
        mMediaPlayer=MediaPlayer.create(getContext(), R.raw.stereo_test);
        mMediaPlayer.setLooping(true);
        if(v.getId() == R.id.left_channel) {
//直接创建，不需要设置setDataSource
            currentChannel = 0;
//            mMediaPlayer.setVolume(1, 0);
//            mMediaPlayer.start();
        } else if(v.getId() == R.id.right_channel) {
            currentChannel = 1;
//            mMediaPlayer.setVolume(0, 1);
//            mMediaPlayer.start();
        } else if(v.getId() == R.id.stereo_channel) {
            currentChannel = 2;
//            mMediaPlayer.setVolume(1, 1);
//            mMediaPlayer.start();
        }

        mMediaPlayer.start();

        disposable = Observable.intervalRange(0L, 11L, 0, 1, TimeUnit.SECONDS)
                .doOnNext(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        level.setProgress((int)(level.getMax() * aLong / 10));
                    }
                })
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        float aFloat = 0.1f * aLong;
                        switch (currentChannel) {
                            case 0:
                                mMediaPlayer.setVolume(aFloat, 0);
                                break;
                            case 1:
                                mMediaPlayer.setVolume(0, aFloat);
                                break;
                            default:
                                mMediaPlayer.setVolume(aFloat, aFloat);
                                break;
                        }

                        if(aLong >= 10) {
                            handler.sendEmptyMessageDelayed(MSG_STOP_MP, 1000);
                        }
                    }
                });
    }
}
