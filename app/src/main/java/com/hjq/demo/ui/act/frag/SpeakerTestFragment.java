package com.hjq.demo.ui.act.frag;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import com.hjq.demo.R;
import com.hjq.demo.common.MyLazyFragment;
import com.hjq.demo.ui.act.t.BaseTestActivity;

import butterknife.BindView;
import timber.log.Timber;

public class SpeakerTestFragment extends MyLazyFragment<BaseTestActivity> {

    @BindView(R.id.start_play)
    Button startPlay;

    @BindView(R.id.volume_level)
    SeekBar volumeControl;

    private MediaPlayer mMediaPlayer;
    private AudioManager audioManager;
    int currentVolume;
    int minVolume;
    int maxVolume;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_test_speaker;
    }

    @Override
    protected int getTitleId() {
        return 0;
    }

    @Override
    protected void initView() {
        startPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != mMediaPlayer) {
                    mMediaPlayer.stop();
                    mMediaPlayer = null;
                }
                mMediaPlayer=MediaPlayer.create(getContext(), R.raw.beep);
                mMediaPlayer.start();
            }
        });

        volumeControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Timber.d("");
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, seekBar.getProgress(), AudioManager.FLAG_PLAY_SOUND);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Timber.d("");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Timber.d("");
            }
        });
    }

    @Override
    protected void initData() {
        audioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        minVolume = audioManager.getStreamMinVolume(AudioManager.STREAM_MUSIC);
        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        volumeControl.setMax(maxVolume);
        volumeControl.setProgress(currentVolume);
    }

    @Override
    public void onResume() {
        super.onResume();
        Timber.d("onResume");

    }

    @Override
    public void onPause() {
        super.onPause();
        if(null != mMediaPlayer) {
            mMediaPlayer.stop();
            mMediaPlayer = null;
        }

        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, AudioManager.FLAG_PLAY_SOUND);
    }
}
