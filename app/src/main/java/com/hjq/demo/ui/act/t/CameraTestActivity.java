package com.hjq.demo.ui.act.t;

import android.hardware.Camera;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.hjq.demo.R;
import com.hjq.image.ImageLoader;
import com.hjq.toast.ToastUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class CameraTestActivity extends BaseTestActivity {

    @BindView(R.id.camera_preview)
    SurfaceView mSurfaceView;

    @BindView(R.id.picture_view)
    ImageView mPicView;

    @BindView(R.id.take_preview)
    Button btnTakePreview;

    @BindView(R.id.take_picture)
    Button btnTakePicture;

    SurfaceHolder mSurfaceHolder;

    Camera mCamera;

    @Override
    protected int getBottomBarId() {
        return R.id.bottom_bar;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_test_camera;
    }

    @Override
    protected int getTitleId() {
        return R.id.top_bar;
    }

    @Override
    protected void initData() {
        mSurfaceHolder = mSurfaceView.getHolder();// 取得holder
        mSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                Timber.d("surfaceCreated");
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                Timber.d("surfaceChanged width:%d height:%d", width, height);
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                Timber.d("surfaceDestroyed");
            }
        }); // holder加入回调接口
        mSurfaceHolder.setKeepScreenOn(true);
    }

    @OnClick({R.id.take_picture, R.id.take_preview})
    void onClick(View view) {
        if(view.getId() == R.id.take_preview) {
            openCamera();
        } else if(view.getId() == R.id.take_picture && null != mCamera) {
            mCamera.takePicture(null, null, mPicture);
        }
    }

    private void openCamera() {
        if (mCamera != null) {
//                    freeCameraResource();
        }

        int count = Camera.getNumberOfCameras();
        Timber.d("camera count:%d", count);

        mCamera = Camera.open(0);
        if (mCamera == null) {
            ToastUtils.show("open camera failed.");
            return;
        }

        Camera.Parameters parameters = mCamera.getParameters();// 获得相机参数

//                List<Camera.Size> mSupportedPreviewSizes = parameters.getSupportedPreviewSizes();
//                List<Camera.Size> mSupportedVideoSizes = parameters.getSupportedVideoSizes();
//                optimalSize = CameraUtils.getOptimalVideoSize(mSupportedVideoSizes,
//                        mSupportedPreviewSizes, height, width);

        Camera.Size size = parameters.getPreviewSize();
        Timber.d("h:%d w:%d", size.height, size.width);
        resize(size.width, size.height);
        try {
//            mCamera.setDisplayOrientation(90);
            mCamera.setPreviewDisplay(mSurfaceHolder);
            mCamera.startPreview();// 开始预览
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void resize(int width, int height) {
        {
            ViewGroup.LayoutParams params = mSurfaceView.getLayoutParams();
            params.width = width * params.height / height;
            mSurfaceView.setLayoutParams(params);
        }
        {
//            ViewGroup.LayoutParams params = mPicView.getLayoutParams();
//            params.width = width * params.height / height;
//            mPicView.setLayoutParams(params);
        }
    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(final byte[] data, Camera camera) {
            Timber.d("");
            Observable.just(getCacheDir().getPath() + File.separator + "IMG_" + System.currentTimeMillis() + ".jpg")
                    .doOnNext(new Consumer<String>() {
                        @Override
                        public void accept(String s) throws Exception {
                            File mediaFile = new File(s);
                            FileOutputStream fos = null;
                            try {
                                fos = new FileOutputStream(mediaFile);
                                fos.write(data);
                                fos.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                                Timber.d(e, e.getMessage());
                            }
                            return;
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String s) throws Exception {
//                            mPicView.setRotation();
//                            mPicView.setImageURI(Uri.fromFile(new File(s)));
                            ImageLoader.loadImage(mPicView, Uri.fromFile(new File(s)).toString());
                        }
                    });

            mCamera.startPreview();
        }
    };
}
