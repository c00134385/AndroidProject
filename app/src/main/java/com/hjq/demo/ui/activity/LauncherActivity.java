package com.hjq.demo.ui.activity;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;

import com.gyf.barlibrary.BarHide;
import com.hjq.demo.R;
import com.hjq.demo.common.MyActivity;
import com.hjq.demo.common.MyApplication;
import com.hjq.demo.mananger.CameraManager;
import com.hjq.demo.mananger.EthernetManager;
import com.hjq.demo.mananger.HardwareManager;
import com.hjq.demo.mananger.MachineManager;
import com.hjq.demo.mananger.NetworkManager;
import com.hjq.demo.mananger.OrthManager;
import com.hjq.demo.mananger.RkManager;
import com.hjq.demo.mananger.SerialManager;
import com.hjq.demo.mananger.TestManager;
import com.hjq.demo.mananger.WifiAdmin;
import com.hjq.demo.ui.act.NewHomeActivity;
import com.hjq.demo.utils.CommonUtils;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject
 *    time   : 2018/10/18
 *    desc   : 启动界面
 */
public final class LauncherActivity extends MyActivity
        implements OnPermission, Animation.AnimationListener {

    @BindView(R.id.iv_launcher_bg)
    View mImageView;
    @BindView(R.id.iv_launcher_icon)
    View mIconView;
    @BindView(R.id.iv_launcher_name)
    View mTextView;

    private static final String[] permissions = new String[]{
            Permission.READ_EXTERNAL_STORAGE,
            Permission.WRITE_EXTERNAL_STORAGE,
            Permission.CAMERA,
            Permission.ACCESS_FINE_LOCATION,
            Permission.ACCESS_COARSE_LOCATION,
            Permission.RECORD_AUDIO};

    private static long startTimestamp;
    private boolean isInitialized = false;

    private static final int MSG_CHECK_PERMISSION = 0x1000;
    private static final int MSG_WAITING = 0x1001;
    private static final int MSG_INIT_JOB = 0x1002;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_CHECK_PERMISSION:
//                    requestPermission();
                    break;
                case MSG_INIT_JOB:
                    initJobManager();
                    break;
                case MSG_WAITING:
                    if(System.currentTimeMillis() - startTimestamp < TimeUnit.SECONDS.toMillis(2) || !isInitialized) {
                        handler.sendEmptyMessageDelayed(MSG_WAITING, 500);
                    } else {
                        goHome();
                    }
                    break;
            }

        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_launcher;
    }

    @Override
    protected int getTitleId() {
        return 0;
    }

    @Override
    protected void initView() {
        //初始化动画
        initStartAnim();
        //设置状态栏和导航栏参数
        getStatusBarConfig()
                .fullScreen(true)//有导航栏的情况下，activity全屏显示，也就是activity最下面被导航栏覆盖，不写默认非全屏
                .hideBar(BarHide.FLAG_HIDE_STATUS_BAR)//隐藏状态栏
                .transparentNavigationBar()//透明导航栏，不写默认黑色(设置此方法，fullScreen()方法自动为true)
                .init();
    }

    @Override
    protected void initData() {
    }

    private static final int ANIM_TIME = 1000;

    /**
     * 启动动画
     */
    private void initStartAnim() {
        // 渐变展示启动屏
        AlphaAnimation aa = new AlphaAnimation(0.4f, 1.0f);
        aa.setDuration(ANIM_TIME * 2);
        aa.setAnimationListener(this);
        mImageView.startAnimation(aa);

        ScaleAnimation sa = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(ANIM_TIME);
        mIconView.startAnimation(sa);

        RotateAnimation ra = new RotateAnimation(180, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        ra.setDuration(ANIM_TIME);
        mTextView.startAnimation(ra);
    }

    private void requestPermission() {
        XXPermissions.with(this)
//                .permission(Permission.Group.STORAGE)
                .permission(permissions)
                .request(this);
    }

    /**
     * {@link OnPermission}
     */

    @Override
    public void hasPermission(List<String> granted, boolean isAll) {
        startTimestamp = System.currentTimeMillis();
        handler.sendEmptyMessage(MSG_INIT_JOB);
    }

    private void initJobManager() {
        isInitialized = false;
        Observable<Integer> observable = Observable.just(2)
                .map(new Function<Integer, Integer>() {
                    @Override
                    public Integer apply(Integer integer) throws Exception {
                        CameraManager.init(MyApplication.getInstance());
                        MachineManager.init(MyApplication.getInstance());
                        NetworkManager.init(MyApplication.getInstance());
                        HardwareManager.init(MyApplication.getInstance());
                        OrthManager.init(MyApplication.getInstance());
                        TestManager.init(MyApplication.getInstance());
                        WifiAdmin.init(MyApplication.getInstance());
                        EthernetManager.init(MyApplication.getInstance());
                        RkManager.init(MyApplication.getInstance());
                        SerialManager.init(MyApplication.getInstance());
                        return integer;
                    }
                })
                .doOnNext(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        CameraManager.getInstance().start();
                        OrthManager.getInstance().start();
                    }
                })
                .subscribeOn(Schedulers.io());

        observable
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        Timber.d("init completed.");
                        isInitialized = true;
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Timber.d("init integer:%d. isMainThread:%b", integer, CommonUtils.isMainThread());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Timber.e(throwable, throwable.getMessage());
                    }
                });

        handler.sendEmptyMessage(MSG_WAITING);
    }
    private void goHome() {
        startActivity(NewHomeActivity.class);
        finish();
    }

    @Override
    public void noPermission(List<String> denied, boolean quick) {
        if (quick) {
            toast("没有权限访问文件，请手动授予权限");
            XXPermissions.gotoPermissionSettings(LauncherActivity.this, true);
        } else {
            toast("请先授予文件读写权限");
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    requestPermission();
                }
            }, 1000);
        }
    }

    @Override
    public void onBackPressed() {
        //禁用返回键
        //super.onBackPressed();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (XXPermissions.isHasPermission(LauncherActivity.this, permissions)) {
            hasPermission(null, true);
        }else {
            requestPermission();
        }
    }

    @Override
    public boolean isSupportSwipeBack() {
        //不使用侧滑功能
        return false;
    }

    /**
     * {@link Animation.AnimationListener}
     */

    @Override
    public void onAnimationStart(Animation animation) {}

    @Override
    public void onAnimationEnd(Animation animation) {
        requestPermission();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {}

    /**
     * Android 8.0踩坑记录：Only fullscreen opaque activities can request orientation
     * https://www.jianshu.com/p/d0d907754603
     */
    @Override
    protected void initOrientation() {
        // 注释父类的固定屏幕方向方法
        // super.initOrientation();
    }
}