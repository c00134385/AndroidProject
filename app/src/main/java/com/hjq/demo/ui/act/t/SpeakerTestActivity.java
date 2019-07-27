package com.hjq.demo.ui.act.t;

import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.hjq.demo.R;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SpeakerTestActivity extends BaseTestActivity {

    @BindView(R.id.level)
    ProgressBar level;

    @BindView(R.id.left_channel)
    Button btnLeftCh;

    @BindView(R.id.right_channel)
    Button btnRightCh;

    @BindView(R.id.stereo_channel)
    Button btnStereo;

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
    protected void initData() {

    }
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
        mMediaPlayer=MediaPlayer.create(this, R.raw.stereo_test);
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

    @Override
    protected void onPause() {
        super.onPause();

        if(null != disposable && !disposable.isDisposed()) {
            disposable.dispose();
        }

        if(null != mMediaPlayer) {
            mMediaPlayer.stop();
            mMediaPlayer = null;
        }

        handler.removeCallbacksAndMessages(null);
    }
}
