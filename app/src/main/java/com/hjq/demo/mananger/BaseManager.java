package com.hjq.demo.mananger;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;

public abstract class BaseManager {

    protected Context mContext;
    protected Handler mHandler;

    protected BaseManager(Context context) {
        // TODO: 2016/12/15 一直持有activity的对象是否可行？
        mContext = context;
        HandlerThread thread = new HandlerThread(getClass().getSimpleName() + " work thread");
        thread.start();
        mHandler = new Handler(thread.getLooper());
    }
}
