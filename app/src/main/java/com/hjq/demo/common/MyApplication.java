package com.hjq.demo.common;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.hjq.demo.BuildConfig;
import com.hjq.demo.other.EventBusManager;
import com.hjq.image.ImageLoader;
import com.hjq.toast.ToastUtils;
import com.hjq.umeng.UmengClient;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;
import timber.log.Timber;

/**
 *    author : Android
 *    github : https://github.com/
 *    time   : 2018/10/18
 *    desc   : 项目中的 Application 基类
 */
public class MyApplication extends Application {

    private static MyApplication ourInstance;
    public static MyApplication getInstance() {
        return ourInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ourInstance = MyApplication.this;

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new CrashReportingTree());
        }

        initSDK(this);
//        initManagers();
    }

    /**
     * 初始化一些第三方框架
     */
    private void initSDK(Application application) {
        /**
         * 必须在 Application 的 onCreate 方法中执行 BGASwipeBackHelper.init 来初始化滑动返回
         * 第一个参数：应用程序上下文
         * 第二个参数：如果发现滑动返回后立即触摸界面时应用崩溃，请把该界面里比较特殊的 View 的 class 添加到该集合中，目前在库中已经添加了 WebView 和 SurfaceView
         */
        BGASwipeBackHelper.init(application, null);

        // 初始化吐司工具类
        ToastUtils.init(application);

        // 初始化图片加载器
        ImageLoader.init(application);

        // 初始化 EventBus
        EventBusManager.init();

        // 初始化友盟 SDK
        UmengClient.init(application);
    }

    private void initManagers() {
//        MachineManager.init(MyApplication.this);
//        NetworkManager.init(MyApplication.this);
//        HardwareManager.init(MyApplication.this);
//        OrthManager.init(MyApplication.this);
//        OrthManager.getInstance().start();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // 使用 Dex分包
        MultiDex.install(this);
    }

    /** A tree which logs important information for crash reporting. */
    private static class CrashReportingTree extends Timber.Tree {
        @Override protected void log(int priority, String tag, @NonNull String message, Throwable t) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return;
            }

            Log.e(tag, message);
            Log.e(tag, t.getMessage());

//            FakeCrashLibrary.log(priority, tag, message);
//
//            if (t != null) {
//                if (priority == Log.ERROR) {
//                    FakeCrashLibrary.logError(t);
//                } else if (priority == Log.WARN) {
//                    FakeCrashLibrary.logWarning(t);
//                }
//            }
        }
    }
}