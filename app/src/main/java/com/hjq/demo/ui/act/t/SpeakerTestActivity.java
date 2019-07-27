package com.hjq.demo.ui.act.t;

import android.content.Context;
import android.media.AudioManager;
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
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

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

    AudioManager audioManager;
    int currentVolume;
    int minVolume;
    int maxVolume;

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
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        minVolume = audioManager.getStreamMinVolume(AudioManager.STREAM_MUSIC);
        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
//        audioManager.setStreamVolume();
        level.setMax(maxVolume - minVolume);
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
            mMediaPlayer.setVolume(1f, 0f);
//            mMediaPlayer.start();
        } else if(v.getId() == R.id.right_channel) {
            currentChannel = 1;
            mMediaPlayer.setVolume(0f, 1f);
//            mMediaPlayer.start();
        } else if(v.getId() == R.id.stereo_channel) {
            currentChannel = 2;
            mMediaPlayer.setVolume(1f, 1f);
//            mMediaPlayer.start();
        }

        mMediaPlayer.start();

        disposable = Observable.intervalRange(minVolume, maxVolume - minVolume + 1, 0, 1, TimeUnit.SECONDS)
                .doOnNext(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        level.setProgress((int)(aLong - minVolume));
                    }
                })
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, aLong.intValue(), 0/*AudioManager.FLAG_SHOW_UI*/);
                        float aFloat = 0.1f * aLong;
                        switch (currentChannel) {
                            case 0:
//                                mMediaPlayer.setVolume(aFloat, 0);
//                                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, aLong.intValue(), AudioManager.FLAG_SHOW_UI);
                                break;
                            case 1:
//                                mMediaPlayer.setVolume(0, aFloat);
                                break;
                            default:
//                                mMediaPlayer.setVolume(aFloat, aFloat);
                                break;
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Timber.e(throwable, throwable.getMessage());
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        handler.sendEmptyMessageDelayed(MSG_STOP_MP, 1000);
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
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0);
    }
}
