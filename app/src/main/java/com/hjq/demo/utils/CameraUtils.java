package com.hjq.demo.utils;

import android.hardware.Camera;

import java.util.List;

public class CameraUtils {

    public static final int CAMERA_FACING_BACK = 0;
    public static final int CAMERA_FACING_FRONT = 1;
    public static final int CAMERA_NONE = 2;

    public static int hasBackCamera()
    {
        int numberOfCameras = Camera.getNumberOfCameras();
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == CAMERA_FACING_BACK) {
                return i;
            }
        }
        return CAMERA_NONE;
    }
    public static int hasFrontCamera()
    {
        int numberOfCameras = Camera.getNumberOfCameras();
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == CAMERA_FACING_FRONT) {
                return i;
            }
        }
        return CAMERA_NONE;
    }

    public static String getCameraPixels(int paramInt)
    {
        if (paramInt == 2)
            return "无";
        Camera localCamera = Camera.open(paramInt);
        Camera.Parameters localParameters = localCamera.getParameters();
        localParameters.set("camera-id", 1);
        List<Camera.Size> localList = localParameters.getSupportedPictureSizes();
        if (localList != null)
        {
            int heights[] = new int[localList.size()];
            int widths[] = new int[localList.size()];
            for (int i = 0; i < localList.size(); i++)
            {
                Camera.Size size = (Camera.Size) localList.get(i);
                int sizehieght = size.height;
                int sizewidth = size.width;
                heights[i] = sizehieght;
                widths[i] =sizewidth;
            }
            int pixels = getMaxNumber(heights) * getMaxNumber(widths);
            localCamera.release();
            return String.valueOf(pixels / 10000) + " 万";

        }
        else return "无";

    }

    public static int getMaxNumber(int[] paramArray)
    {
        int temp = paramArray[0];
        for(int i = 0;i<paramArray.length;i++)
        {
            if(temp < paramArray[i])
            {
                temp = paramArray[i];
            }
        }
        return temp;
    }

    public static Camera.Size getOptimalVideoSize(List<Camera.Size> supportedVideoSizes,
                                                  List<Camera.Size> previewSizes, int w, int h) {
// Use a very small tolerance because we want an exact match.
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) w / h;

// Supported video sizes list might be null, it means that we are allowed to use the preview
// sizes
        List<Camera.Size> videoSizes;
        if (supportedVideoSizes != null) {
            videoSizes = supportedVideoSizes;
        } else {
            videoSizes = previewSizes;
        }
        Camera.Size optimalSize = null;

// Start with max value and refine as we iterate over available video sizes. This is the
// minimum difference between view and camera height.
        double minDiff = Double.MAX_VALUE;

// Target view height
        int targetHeight = h;

// Try to find a video size that matches aspect ratio and the target view size.
// Iterate over all available sizes and pick the largest size that can fit in the view and
// still maintain the aspect ratio.
        for (Camera.Size size : videoSizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
                continue;
            if (Math.abs(size.height - targetHeight) < minDiff && previewSizes.contains(size)) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

// Cannot find video size that matches the aspect ratio, ignore the requirement
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : videoSizes) {
                if (Math.abs(size.height - targetHeight) < minDiff && previewSizes.contains(size)) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }
}
