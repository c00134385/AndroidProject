package com.hjq.demo.ui.act.t;

import android.view.View;

import com.hjq.demo.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class ScreenTestActivity extends BaseTestActivity {
    @BindView(R.id.foreground)
    View view;

    Disposable disposable;

    @Override
    protected int getBottomBarId() {
        return R.id.bottom_bar;
    }

    @Override
    protected void onTestStart() {
        super.onTestStart();

        view.bringToFront();
        view.setVisibility(View.VISIBLE);

        final List<Integer> colors = new ArrayList<>();
        colors.add(R.color.red);
        colors.add(R.color.green);
        colors.add(R.color.blue);
        colors.add(R.color.black);

        if(null != disposable && !disposable.isDisposed()) {
            disposable.dispose();
        }

        disposable = Observable.intervalRange(0, colors.size() + 1, 1, 3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        try {
                            int color = colors.get(aLong.intValue());
                            view.setBackgroundResource(color);
                        } catch (Exception e) {
                            Timber.e(e, e.getMessage());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        onTestEnd();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        onTestEnd();
                    }
                });

    }

    @Override
    protected void onTestEnd() {
        super.onTestEnd();
        view.setVisibility(View.GONE);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_test_screen;
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
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(null != disposable && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}
