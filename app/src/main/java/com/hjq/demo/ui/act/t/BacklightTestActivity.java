package com.hjq.demo.ui.act.t;

import android.widget.SeekBar;

import com.hjq.demo.R;
import com.hjq.demo.utils.BacklightUtils;

import butterknife.BindView;
import timber.log.Timber;

public class BacklightTestActivity extends BaseTestActivity {

    @BindView(R.id.level)
    SeekBar seekBar;

    private int currentBrightness = 0;

    @Override
    protected int getBottomBarId() {
        return R.id.bottom_bar;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_test_backlight;
    }

    @Override
    protected int getTitleId() {
        return R.id.top_bar;
    }

    @Override
    protected void initView() {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                BacklightUtils.setBrightness(BacklightTestActivity.this, progress);
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

        seekBar.setMax(255);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        BacklightUtils.setBrightness(BacklightTestActivity.this, currentBrightness);
    }

    @Override
    protected void initData() {
        currentBrightness = BacklightUtils.getScreenBrightness(this);
        BacklightUtils.setBrightness(BacklightTestActivity.this, currentBrightness + 1);
        seekBar.setProgress(currentBrightness);
    }

    @Override
    protected void onTestStart() {
        super.onTestStart();

//        Observable.intervalRange(0, 11, 1, 1, TimeUnit.SECONDS)
//                .doOnNext(new Consumer<Long>() {
//                    @Override
//                    public void accept(Long aLong) throws Exception {
//                        level.setProgress((int)(level.getMax() * aLong / 10));
//                    }
//                })
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<Long>() {
//                    @Override
//                    public void accept(Long aLong) throws Exception {
//                        int value = (int)(aLong * 255 / 10);
//                        if(value > 255) {
//                            value = 255;
//                        }
//
//                        BacklightUtils.setBrightness(BacklightTestActivity.this, value);
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//                        Timber.e(throwable, throwable.getMessage());
//                    }
//                });
    }

    @Override
    protected void onTestEnd() {
        super.onTestEnd();
    }
}
