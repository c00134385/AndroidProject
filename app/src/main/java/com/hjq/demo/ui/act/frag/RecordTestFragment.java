package com.hjq.demo.ui.act.frag;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hjq.demo.R;
import com.hjq.demo.common.MyLazyFragment;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import timber.log.Timber;

public class RecordTestFragment extends MyLazyFragment {

    @BindView(R.id.time)
    TextView time;


    @BindView(R.id.record)
    Button btnRecord;

    @BindView(R.id.playback)
    Button btnPlayback;

    byte[] mAudioData;
    AudioRecord mAudioRecord;

    MediaRecorder mMediaRecorder;
    String filePath;

    MediaPlayer mMediaPlayer;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_test_record;
    }

    @Override
    protected int getTitleId() {
        return 0;
    }

    @Override
    protected void initView() {
        Timber.d("");

    }

    @Override
    protected void initData() {
        Timber.d("");
        int mSampleRateInHZ = 8000;
        int mChannelConfig = AudioFormat.CHANNEL_IN_STEREO;
        int mAudioFormat = AudioFormat.ENCODING_PCM_8BIT;
        int mRecorderBufferSize = AudioRecord.getMinBufferSize(mSampleRateInHZ, mChannelConfig, mAudioFormat);
        mAudioData = new byte[320];
        mAudioRecord = new AudioRecord(MediaRecorder.AudioSource.VOICE_COMMUNICATION, mSampleRateInHZ, mChannelConfig, mAudioFormat, mRecorderBufferSize);
//        mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, mSampleRateInHZ, mChannelConfig, mAudioFormat, mRecorderBufferSize * 2
//                , AudioTrack.MODE_STREAM);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    private Handler MainHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {

            }
        }
    };


    @OnClick({R.id.record, R.id.playback})
    void onClick(View v) {
        if(R.id.record == v.getId()) {
            if(null == mMediaRecorder) {
                startRecord();
                btnRecord.setText("停止录音");
            } else {
                stopRecord();
                btnRecord.setText("开始录音");
            }

        } else if (R.id.playback == v.getId()) {
            if(null == mMediaPlayer) {
                startPlayback();
                btnPlayback.setText("停止播放");
            } else {
                stopPlayback();
                btnPlayback.setText("开始播放");
            }
        }
    }

    private void startRecord() {
        // 开始录音
        /* ①Initial：实例化MediaRecorder对象 */
        if (mMediaRecorder == null)
            mMediaRecorder = new MediaRecorder();
        try {
            /* ②setAudioSource/setVedioSource */
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);// 设置麦克风
            /*
             * ②设置输出文件的格式：THREE_GPP/MPEG-4/RAW_AMR/Default THREE_GPP(3gp格式
             * ，H263视频/ARM音频编码)、MPEG-4、RAW_AMR(只支持音频且音频编码要求为AMR_NB)
             */
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            /* ②设置音频文件的编码：AAC/AMR_NB/AMR_MB/Default 声音的（波形）的采样 */
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            String fileName = "111111.m4a";
            String audioSaveDir = ContextCompat.getDataDir(getContext()).getAbsolutePath();
//            String audioSaveDir = Environment.getExternalStorageDirectory().getAbsolutePath();
//            if (!FileUtils.isFolderExist(FileUtils.getFolderName(audioSaveDir))) {
//                FileUtils.makeFolders(audioSaveDir);
//            }
            filePath = audioSaveDir + File.separator +  fileName;
            /* ③准备 */
            mMediaRecorder.setOutputFile(filePath);
            mMediaRecorder.prepare();
            /* ④开始 */
            mMediaRecorder.start();
        } catch (IllegalStateException e) {
            Timber.e("call startAmr(File mRecAudioFile) failed!" + e.getMessage());
        } catch (IOException e) {
            Timber.e("call startAmr(File mRecAudioFile) failed!" + e.getMessage());
        }
    }

    private void stopRecord() {
        if(null != mMediaRecorder) {
            mMediaRecorder.stop();
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
    }


    private void startPlayback() {
        mMediaPlayer=MediaPlayer.create(getContext(), Uri.fromFile(new File(filePath)));
        mMediaPlayer.setLooping(false);
        mMediaPlayer.start();
    }

    private void stopPlayback() {
        if(null != mMediaPlayer) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

}
