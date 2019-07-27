package com.hjq.demo.ui.act.t;

import android.widget.ProgressBar;

import com.hjq.demo.R;
import com.hjq.demo.utils.BacklightUtils;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

public class BacklightTestActivity extends BaseTestActivity {

    @BindView(R.id.level)
    ProgressBar level;

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

    }

    @Override
    protected void initData() {
        currentBrightness = BacklightUtils.getScreenBrightness(this);
        BacklightUtils.setBrightness(BacklightTestActivity.this, currentBrightness + 1);
    }

    @Override
    protected void onTestStart() {
        super.onTestStart();

        Observable.intervalRange(0, 11, 1, 1, TimeUnit.SECONDS)
                .doOnNext(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        level.setProgress((int)(level.getMax() * aLong / 10));
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        int value = (int)(aLong * 255 / 10);
                        if(value > 255) {
                            value = 255;
                        }

                        BacklightUtils.setBrightness(BacklightTestActivity.this, value);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Timber.e(throwable, throwable.getMessage());
                    }
                });
    }

    @Override
    protected void onTestEnd() {
        super.onTestEnd();
    }
}
