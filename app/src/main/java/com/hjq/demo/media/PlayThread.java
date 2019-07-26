package com.hjq.demo.media;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioTrack;

public class PlayThread extends Thread{

    // 采样率
    private int mSampleRateInHz = 16000;
    // 单声道
    private int mChannelConfig = AudioFormat.CHANNEL_OUT_MONO;
    // 双声道（立体声）
    // private int mChannelConfig = AudioFormat.CHANNEL_OUT_STEREO;

    private static final String TAG = "PlayThread";
    private Activity mActivity;
    private AudioTrack mAudioTrack;
    private byte[] data;
    private String mFileName;


    @Override
    public void run() {
        super.run();
    }
}
