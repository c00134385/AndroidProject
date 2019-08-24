package com.hjq.demo.ui.act.frag;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.VideoView;

import com.hjq.demo.R;
import com.hjq.demo.common.MyLazyFragment;
import com.hjq.demo.ui.act.t.BaseTestActivity;

import butterknife.BindView;
import timber.log.Timber;

public class VideoTestFragment extends MyLazyFragment<BaseTestActivity> {

    @BindView(R.id.start_play)
    Button startPlay;

    @BindView(R.id.stop_play)
    Button stopPlay;

    @BindView(R.id.video_view)
    VideoView videoView;

    private MediaPlayer mMediaPlayer;
    private Uri uri;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_test_video;
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
//                if(null != mMediaPlayer) {
//                    mMediaPlayer.stop();
//                    mMediaPlayer = null;
//                }
//                mMediaPlayer=MediaPlayer.create(getContext(), R.raw.beep);
//                mMediaPlayer.start();
                videoView.setVideoPath(uri.toString());
                videoView.start();
            }
        });

        stopPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.stopPlayback();
            }
        });
    }

    @Override
    protected void initData() {
        uri = Uri.parse("android.resource://" + getContext().getPackageName() + "/" + R.raw.s);
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
    }
}
