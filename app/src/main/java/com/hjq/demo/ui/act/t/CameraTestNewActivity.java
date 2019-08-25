package com.hjq.demo.ui.act.t;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hjimi.api.iminect.ImiDevice;
import com.hjimi.api.iminect.ImiDeviceAttribute;
import com.hjimi.api.iminect.ImiDeviceState;
import com.hjimi.api.iminect.ImiFrameMode;
import com.hjimi.api.iminect.ImiNect;
import com.hjimi.api.iminect.ImiPixelFormat;
import com.hjq.demo.R;
import com.hjq.demo.ui.imi.DecodePanel;
import com.hjq.demo.ui.imi.GLPanel;
import com.hjq.demo.ui.imi.SimpleViewer;

import java.lang.ref.WeakReference;


public class CameraTestNewActivity extends BaseTestActivity {

    private static final String TAG = "MainActivity";

    private SurfaceView mColorView;
    private GLPanel mGLColorPanel;
    private GLPanel mGLDepthPanel;
    private DecodePanel mDecodePanel;
    private Surface mSurface;
    private TextView mTVAttrs;
    private Button btn_Exit;

    private boolean bExiting = false;

    private ImiNect m_ImiNect = null;
    private MainListener mainlistener;
    private ImiDevice mDevice;
    private SimpleViewer mColorViewer;
    private SimpleViewer mDepthViewer;
    private ImiDeviceAttribute mDeviceAttribute = null;

    private static final int DEVICE_OPEN_SUCCESS = 0;
    private static final int DEVICE_OPEN_FALIED = 1;
    private static final int DEVICE_DISCONNECT = 2;
    private static final int MSG_EXIT = 5;

    class MyHandler extends Handler {
        WeakReference<CameraTestNewActivity> mActivity;
        public MyHandler(CameraTestNewActivity activity) {
            mActivity = new WeakReference<CameraTestNewActivity>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            CameraTestNewActivity mainActivity = mActivity.get();
            switch (msg.what)
            {
                case DEVICE_OPEN_FALIED:
                case DEVICE_DISCONNECT:
                    mainActivity.showMessageDialog((String) msg.obj);
                    break;
                case DEVICE_OPEN_SUCCESS:
                    mainActivity.runViewer();
                    break;
                case MSG_EXIT:
                    mainActivity.finish();
                    break;
            }
        }
    }

    private MyHandler MainHandler = new MyHandler(this);

    private void showMessageDialog(String errMsg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(errMsg);
        builder.setPositiveButton("quit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                dialog.dismiss();
                bExiting = true;
                new Thread(new ExitRunnable()).start();
            }
        });
        builder.show();
    }

    private void runViewer() {

        mTVAttrs.setText("Device SerialNumber : " + mDeviceAttribute.getSerialNumber());

        mColorViewer = new SimpleViewer(mDevice, ImiDevice.ImiStreamType.COLOR);
        mDepthViewer = new SimpleViewer(mDevice, ImiDevice.ImiStreamType.DEPTH);

        mColorView.setVisibility(View.GONE);
        mGLColorPanel.setVisibility(View.VISIBLE);
        mColorViewer.setGLPanel(mGLColorPanel);

        mDepthViewer.setGLPanel(mGLDepthPanel);

        mColorViewer.onStart();
        mDepthViewer.onStart();
    }

    private class MainListener implements ImiDevice.OpenDeviceListener, ImiDevice.DeviceStateListener {

        @Override
        public void onOpenDeviceSuccess() {
            //open device success.
            mDeviceAttribute = mDevice.getAttribute();

            MainHandler.sendEmptyMessage(DEVICE_OPEN_SUCCESS);
        }

        @Override
        public void onOpenDeviceFailed(String errorMsg) {
            //open device falied.
            MainHandler.sendMessage(MainHandler.obtainMessage(DEVICE_OPEN_FALIED, errorMsg));
        }

        @Override
        public void onDeviceStateChanged(String deviceInfo, ImiDeviceState state) {
            if(state == ImiDeviceState.IMI_DEVICE_STATE_CONNECT) {
                Toast.makeText(CameraTestNewActivity.this, deviceInfo + " CONNECT", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(CameraTestNewActivity.this, deviceInfo + " DISCONNECT", Toast.LENGTH_LONG).show();
            }
        }
    }

    private class OpenDeviceRunnable implements Runnable {

        @Override
        public void run() {
            //get iminect instance.

            ImiNect.initialize();
            mDevice = ImiDevice.getInstance();
            mainlistener = new MainListener();

            ImiFrameMode colorMode = new ImiFrameMode(ImiPixelFormat.IMI_PIXEL_FORMAT_IMAGE_RGB24, 640, 480, 30);
            mDevice.setFrameMode(ImiDevice.ImiStreamType.COLOR, colorMode);

            ImiFrameMode depthMode = new ImiFrameMode(ImiPixelFormat.IMI_PIXEL_FORMAT_DEP_16BIT, 640, 480, 30);
            mDevice.setFrameMode(ImiDevice.ImiStreamType.DEPTH, depthMode);

            mDevice.open(CameraTestNewActivity.this, ImiDevice.ImiStreamType.COLOR.toNative() | ImiDevice.ImiStreamType.DEPTH.toNative(), mainlistener);

            mDevice.addDeviceStateListener(mainlistener);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_color_depth_view;
    }

    @Override
    protected int getTitleId() {
        return 0;
    }

    @Override
    protected void initView() {
        mTVAttrs = (TextView) findViewById(R.id.tv_show_attrs);
        mColorView = (SurfaceView) findViewById(R.id.color_view);

        mColorView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                mSurface = surfaceHolder.getSurface();
                mDecodePanel = new DecodePanel();
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                mDecodePanel.stopDecoder();
            }
        });

        mGLColorPanel = (GLPanel) findViewById(R.id.sv_color_view);

        mGLDepthPanel = (GLPanel) findViewById(R.id.sv_depth_view);

        btn_Exit = (Button) findViewById(R.id.button_Exit);
        btn_Exit.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bExiting) {
                    Log.d("@@@@@", "EXITING...");
                    return;
                }

                bExiting = true;
                new Thread(new ExitRunnable()).start();
            }
        });

        new Thread(new OpenDeviceRunnable()).start();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getBottomBarId() {
        return R.id.bottom_bar;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mDepthViewer != null) {
            mDepthViewer.onResume();
        }

        if (mColorViewer != null) {
            mColorViewer.onResume();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    private class ExitRunnable implements Runnable {

        @Override
        public void run() {
            if (mDepthViewer != null) {
                mDepthViewer.onPause();
            }

            if (mColorViewer != null) {
                mColorViewer.onPause();
            }

            if (mDepthViewer != null) {
                mDepthViewer.onDestroy();
            }

            //destroy color viewer.
            if (mColorViewer != null) {
                mColorViewer.onDestroy();
            }

            if(mDevice != null) {
                mDevice.close();
                mDevice = null;
                ImiDevice.destroy();
            }

            ImiNect.destroy();

            MainHandler.sendEmptyMessage(MSG_EXIT);
        }
    }

    private void Exit() {

        finish();

//        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    public void onBackPressed() {
        Log.d("@@@@@", "refuse back to exit");
        return;
    }

    @Override
    protected void onTestSuccess() {
        super.onTestSuccess();
        btn_Exit.performClick();
    }

    @Override
    protected void onTestFailed() {
        super.onTestFailed();
        btn_Exit.performClick();
    }
}